package EVC.Movie.services;

import EVC.Movie.entity.Genre;
import EVC.Movie.entity.History;
import EVC.Movie.entity.Movie;
import EVC.Movie.repository.IGenreRepository;
import EVC.Movie.repository.IHistoryRepository;
import EVC.Movie.repository.IMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

@Service
public class RecommendationService {
    @Autowired
    private WekaModelService wekaModelService;

    @Autowired
    private IMovieRepository movieRepository;
    @Autowired
    private MovieServices movieServices;
    @Autowired
    private IHistoryRepository historyRepository;

    @Autowired
    private HistoryServices historyServices;

    @Autowired
    private IGenreRepository genreRepository;

    // Phương thức chuẩn bị dữ liệu ARFF với viewCount
    public String prepareDataForWeka(Long currentMovieId) {
        // Trích xuất đặc trưng của phim hiện tại
        Movie currentMovie = movieRepository.findById(currentMovieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        Set<String> currentGenres = genreRepository.findGenresByMovieId(currentMovieId);
        String currentCountry = currentMovie.getCountry().getName();

        // Lấy lịch sử xem phim và tổng hợp dữ liệu lịch sử
        List<History> histories = historyServices.getAll();
        Map<Long, Long> movieViewCountMap = new HashMap<>();
        for (History history : histories) {
            Long movieId = history.getEpisode().getMovie().getMovieId();
            movieViewCountMap.put(movieId, movieViewCountMap.getOrDefault(movieId, 0L) + history.getViewCount());
        }

        // Tạo dữ liệu ARFF
        StringBuilder arffData = new StringBuilder();
        arffData.append("@relation movie_similarity\n\n");
        arffData.append("@attribute genre_similarity numeric\n");
        arffData.append("@attribute country_similarity numeric\n");
        arffData.append("@attribute view_count numeric\n");
        arffData.append("@attribute movie_id numeric\n");
        arffData.append("\n@data\n");

        for (Map.Entry<Long, Long> entry : movieViewCountMap.entrySet()) {
            Long movieId = entry.getKey();
            if (movieId.equals(currentMovieId)) continue; // Loại trừ phim hiện tại

            // Đặc trưng của phim so sánh
            Set<String> genres = genreRepository.findGenresByMovieId(movieId);
            String country = movieRepository.findById(movieId)
                    .orElseThrow(() -> new RuntimeException("Movie not found")).getCountry().getName();

            // Tính toán mức độ tương đồng
            double genreSimilarity = calculateJaccardSimilarity(currentGenres, genres);
            double countrySimilarity = currentCountry.equals(country) ? 1.0 : 0.0;

            // Sử dụng viewCount để điều chỉnh mức độ tương đồng
            long viewCount = entry.getValue();
            double weightedGenreSimilarity = genreSimilarity * viewCount;
            double weightedCountrySimilarity = countrySimilarity * viewCount;

            // Thêm dữ liệu vào ARFF
            arffData.append(weightedGenreSimilarity).append(",");
            arffData.append(weightedCountrySimilarity).append(",");
            arffData.append(viewCount).append(",");
            arffData.append(movieId).append("\n");
        }

        // Lưu file ARFF
        String filePath = "data/movie_similarity_with_viewcount.arff";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(arffData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }

    private double calculateJaccardSimilarity(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }

    public List<Movie> getRecommendations(Long currentMovieId) {
        String arffFilePath = prepareDataForWeka(currentMovieId);
        Map<Long, Double> movieSimilarityScores = new HashMap<>();

        try {
            // Kiểm tra hoặc khởi tạo mô hình (chỉ huấn luyện nếu cần)
            initializeModel(arffFilePath);

            // Dự đoán mức độ tương đồng
            Instances instances = new DataSource(arffFilePath).getDataSet();
            instances.setClassIndex(instances.numAttributes() - 1);

            for (int i = 0; i < instances.numInstances(); i++) {
                Instance instance = instances.instance(i);
                double similarityScore = wekaModelService.predict(instance, instances);
                long movieId = (long) instance.value(instance.numAttributes() - 1);

                // Lưu điểm tương đồng
                movieSimilarityScores.put(movieId, similarityScore);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Movie> movies =movieSimilarityScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(8)
                .map(entry -> movieRepository.findById(entry.getKey()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        movies.forEach(movie -> System.out.println("Movie ID: " + movie.getMovieId()));
        return movies;
    }

    public void initializeModel(String arffFilePath) throws Exception {
        File modelFile = new File("data/recommendation.model");

        if (!modelFile.exists()) {
            wekaModelService.trainModel(arffFilePath);
        } else {
            wekaModelService.loadModel();
        }
    }

    public List<Movie> getMovieList(Long currentMovieId) {
        // Lấy danh sách phim từ getRecommendations
        List<Movie> recommendedMovies = getRecommendations(currentMovieId);

        // Nếu số lượng phim đã đủ 8, trả về luôn
        if (recommendedMovies.size() >= 8) {
            return recommendedMovies.subList(0, 8);
        }

        // Lấy danh sách 8 phim có lượt xem nhiều nhất từ MovieService
        List<Movie> mostViewedMovies = movieServices.getTopViewedMovies(16);

        // Tập hợp ID của phim dự đoán để loại trừ
        Set<Long> existingMovieIds = recommendedMovies.stream()
                .map(Movie::getMovieId)
                .collect(Collectors.toSet());
        existingMovieIds.add(currentMovieId); // Loại trừ luôn currentMovieId

        // Bổ sung phim từ danh sách lượt xem nhiều nhất
        List<Movie> finalMovies = new ArrayList<>(recommendedMovies);
        for (Movie movie : mostViewedMovies) {
            if (!existingMovieIds.contains(movie.getMovieId())) {
                finalMovies.add(movie);
                existingMovieIds.add(movie.getMovieId()); // Đảm bảo không bị thêm lại
            }
            if (finalMovies.size() == 8) break; // Dừng khi đủ 8 phim
        }

        return finalMovies;
    }
}
