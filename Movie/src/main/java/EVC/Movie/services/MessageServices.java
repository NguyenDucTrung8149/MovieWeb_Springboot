package EVC.Movie.services;


import EVC.Movie.entity.Episode;
import EVC.Movie.entity.Message;
import EVC.Movie.entity.Movie;
import EVC.Movie.entity.User;
import EVC.Movie.repository.IMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MessageServices {
    @Autowired
    private IMessageRepository messageRepository;
    @Autowired
    private UserServices userServices;
    @Autowired
    private EmailService emailService;
    public List<Message> getAll () {
        return messageRepository.findAll();
    }
    public List<Message> getAllWithEmgerTrueAndTypeFalse () {
        return messageRepository.findAllByEmergencyAndTypeOrderByNgaydangDesc(true,false);
    }
    public List<Message> getAllWithEmgerFalseAndType () {
        return messageRepository.findAllByEmergencyAndTypeOrderByNgaydangDesc(false,false);
    }

    public Message getById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public void delete(Long id) {
        messageRepository.deleteById(id);
    }

    public Page<Message> getAllWithPagination(Pageable pageable) {
        return messageRepository.findAllByOrderByNgaydangDesc(pageable);
    }
    public Page<Message> getAllWithMovieId(Long movieId,Pageable pageable){
        return messageRepository.findAllByMovie_MovieIdOrderByNgaydangDesc(movieId,pageable);
    }
    public Page<Message> getAllWithType(Boolean type,Pageable pageable){
        return messageRepository.findAllByTypeOrderByNgaydangDesc(type,pageable);
    }
    public Page<Message> getAllWithEmergency(Boolean type,Pageable pageable){
        return messageRepository.findAllByEmergencyOrderByNgaydangDesc(type,pageable);
    }
    public Page<Message> getAllWithMovieIdAndTypeAndEmg(Long movieId,Boolean type,Boolean emg,Pageable pageable){
        return messageRepository.findAllByMovie_MovieIdAndTypeAndEmergencyOrderByNgaydangDesc(movieId,type,emg,pageable);
    }
    public Page<Message> getAllWithMovieIdAndType(Long movieId,Boolean type,Pageable pageable){
        return messageRepository.findAllByMovie_MovieIdAndTypeOrderByNgaydangDesc(movieId,type,pageable);
    }
    public Page<Message> getAllWithMovieIdAndEmg(Long movieId,Boolean emg,Pageable pageable){
        return messageRepository.findAllByMovie_MovieIdAndEmergencyOrderByNgaydangDesc(movieId,emg,pageable);
    }
    public Page<Message> getAllWithTypeAndEmg(Boolean type ,Boolean emg,Pageable pageable){
        return messageRepository.findAllByTypeAndEmergencyOrderByNgaydangDesc(type,emg,pageable);
    }
    public Page<Message> getAllWithPagination7Day(Pageable pageable) {
        LocalDate now = LocalDate.now();
        LocalDate sevenDaysAgo = now.minusDays(7);
        Pageable sortedByCreatedAtDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("ngaydang").descending());
        return messageRepository.findAllWithCreationDateBetween(sevenDaysAgo, now, sortedByCreatedAtDesc);
    }

    @Async
    public void notifyUsersAboutNewEpisode(Movie movie, Message message) {
        List<User> users = userServices.findUsersByFavoriteMovie(movie);
        String subject = "Cập nhật tập mới cho phim: " + message.getMovie().getName();
        String text = message.getInfo();
        for (User user : users) {
            String email = user.getEmail();
            if (email == null || email.isEmpty()) {
                continue;
            }
            emailService.sendSimpleMessage(email, subject, text);
        }
    }
}
