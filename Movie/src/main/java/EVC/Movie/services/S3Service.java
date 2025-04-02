package EVC.Movie.services;

import com.amazonaws.services.mediaconvert.AWSMediaConvert;
import com.amazonaws.services.mediaconvert.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private AWSMediaConvert mediaConvertClient;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.s3.outputvideos}")
    private String bucketOutput;
    @Value("${cloud.aws.s3.imagesmovie}")
    private String bucketImagesmovie;
    @Value("${cloud.aws.mediaconvert.role}")
    private String mediaConvertRole;
    @Async
    public CompletableFuture<String> uploadFile(MultipartFile file, Long movieId, Long episodeId) throws IOException {

        // Tải tệp lên S3
        String originalFileName = movieId + "_" + episodeId + "_" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(new PutObjectRequest(bucketName, originalFileName, file.getInputStream(), metadata));

        String s3InputPath = "s3://" + bucketName + "/" + originalFileName;
        String s3OutputPath = "s3://" + bucketOutput + "/" + movieId + "/" + episodeId + "/";

        // Cấu hình cho công việc MediaConvert
        JobSettings jobSettings = new JobSettings()
                .withInputs(new Input()
                        .withFileInput(s3InputPath)
                        .addAudioSelectorsEntry("Audio Selector 1", new AudioSelector()
                                .withDefaultSelection("DEFAULT") // Lựa chọn audio mặc định
                        )
                )
                .withOutputGroups(new OutputGroup()
                        .withName("HLS Group")
                        .withOutputGroupSettings(new OutputGroupSettings()
                                .withType("HLS_GROUP_SETTINGS")
                                .withHlsGroupSettings(new HlsGroupSettings()
                                        .withDestination(s3OutputPath) // Thư mục đầu ra trong S3
                                        .withSegmentLength(6)
                                        .withMinSegmentLength(1)
                                        .withManifestDurationFormat("INTEGER")
                                        .withOutputSelection("MANIFESTS_AND_SEGMENTS")
                                        .withSegmentControl("SEGMENTED_FILES")
                                )
                        )
                        .withOutputs(
                                // Output 1: 1080p
                                new Output()
                                        .withNameModifier("_1080p")
                                        .withContainerSettings(new ContainerSettings()
                                                .withContainer("M3U8"))
                                        .withVideoDescription(new VideoDescription()
                                                .withCodecSettings(new VideoCodecSettings()
                                                        .withCodec("H_264")
                                                        .withH264Settings(new H264Settings()
                                                                .withBitrate(5000000) // Bitrate
                                                                .withGopSize(30.0)
                                                                .withRateControlMode("CBR")
                                                        )
                                                )
                                                .withWidth(1920)
                                                .withHeight(1080)
                                        )
                                        .withAudioDescriptions(new AudioDescription()
                                                .withCodecSettings(new AudioCodecSettings()
                                                        .withCodec("AAC")
                                                        .withAacSettings(new AacSettings()
                                                                .withBitrate(96000)
                                                                .withSampleRate(48000)
                                                                .withCodingMode("CODING_MODE_2_0")
                                                        )
                                                )
                                        ),
                                // Output 2: 720p
                                new Output()
                                        .withNameModifier("_720p")
                                        .withContainerSettings(new ContainerSettings()
                                                .withContainer("M3U8"))
                                        .withVideoDescription(new VideoDescription()
                                                .withCodecSettings(new VideoCodecSettings()
                                                        .withCodec("H_264")
                                                        .withH264Settings(new H264Settings()
                                                                .withBitrate(3000000)
                                                                .withGopSize(30.0)
                                                                .withRateControlMode("CBR")
                                                        )
                                                )
                                                .withWidth(1280)
                                                .withHeight(720)
                                        )
                                        .withAudioDescriptions(new AudioDescription()
                                                .withCodecSettings(new AudioCodecSettings()
                                                        .withCodec("AAC")
                                                        .withAacSettings(new AacSettings()
                                                                .withBitrate(96000)
                                                                .withSampleRate(48000)
                                                                .withCodingMode("CODING_MODE_2_0")
                                                        )
                                                )
                                        ),
                                // Output 3: 480p
                                new Output()
                                        .withNameModifier("_480p")
                                        .withContainerSettings(new ContainerSettings()
                                                .withContainer("M3U8"))
                                        .withVideoDescription(new VideoDescription()
                                                .withCodecSettings(new VideoCodecSettings()
                                                        .withCodec("H_264")
                                                        .withH264Settings(new H264Settings()
                                                                .withBitrate(1500000)
                                                                .withGopSize(30.0)
                                                                .withRateControlMode("CBR")
                                                        )
                                                )
                                                .withWidth(854)
                                                .withHeight(480)
                                        )
                                        .withAudioDescriptions(new AudioDescription()
                                                .withCodecSettings(new AudioCodecSettings()
                                                        .withCodec("AAC")
                                                        .withAacSettings(new AacSettings()
                                                                .withBitrate(96000)
                                                                .withSampleRate(48000)
                                                                .withCodingMode("CODING_MODE_2_0")
                                                        )
                                                )
                                        ),
                                // Output 4: 360p
                                new Output()
                                        .withNameModifier("_360p")
                                        .withContainerSettings(new ContainerSettings()
                                                .withContainer("M3U8"))
                                        .withVideoDescription(new VideoDescription()
                                                .withCodecSettings(new VideoCodecSettings()
                                                        .withCodec("H_264")
                                                        .withH264Settings(new H264Settings()
                                                                .withBitrate(1000000)
                                                                .withGopSize(30.0)
                                                                .withRateControlMode("CBR")
                                                        )
                                                )
                                                .withWidth(640)
                                                .withHeight(360)
                                        )
                                        .withAudioDescriptions(new AudioDescription()
                                                .withCodecSettings(new AudioCodecSettings()
                                                        .withCodec("AAC")
                                                        .withAacSettings(new AacSettings()
                                                                .withBitrate(96000)
                                                                .withSampleRate(48000)
                                                                .withCodingMode("CODING_MODE_2_0")
                                                        )
                                                )
                                        ),
                                // Output 5: 240p
                                new Output()
                                        .withNameModifier("_240p")
                                        .withContainerSettings(new ContainerSettings()
                                                .withContainer("M3U8"))
                                        .withVideoDescription(new VideoDescription()
                                                .withCodecSettings(new VideoCodecSettings()
                                                        .withCodec("H_264")
                                                        .withH264Settings(new H264Settings()
                                                                .withBitrate(600000)
                                                                .withGopSize(30.0)
                                                                .withRateControlMode("CBR")
                                                        )
                                                )
                                                .withWidth(426)
                                                .withHeight(240)
                                        )
                                        .withAudioDescriptions(new AudioDescription()
                                                .withCodecSettings(new AudioCodecSettings()
                                                        .withCodec("AAC")
                                                        .withAacSettings(new AacSettings()
                                                                .withBitrate(96000)
                                                                .withSampleRate(48000)
                                                                .withCodingMode("CODING_MODE_2_0")
                                                        )
                                                )
                                        )
                        )
                );

        // Tạo công việc MediaConvert
        CreateJobRequest createJobRequest = new CreateJobRequest()
                .withRole(mediaConvertRole)
                .withSettings(jobSettings);

        CreateJobResult createJobResult = mediaConvertClient.createJob(createJobRequest);
        String jobId = createJobResult.getJob().getId();

        // Trả về URL của file master.m3u8
        String fileNameWithoutExtension = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        String masterPlaylistUrl = amazonS3.getUrl(bucketOutput,
                movieId + "/" + episodeId + "/" + fileNameWithoutExtension + ".m3u8").toString();
        return CompletableFuture.completedFuture(masterPlaylistUrl);
    }


    public void deleteFile(String dbFileUrl) {
        try {
            String fileNameWithoutExtension = dbFileUrl.substring(dbFileUrl.lastIndexOf('/') + 1, dbFileUrl.lastIndexOf('_'));
            fileNameWithoutExtension = URLDecoder.decode(fileNameWithoutExtension, StandardCharsets.UTF_8);
            String fileNameInBucketName = fileNameWithoutExtension + ".mp4";
            amazonS3.deleteObject(bucketName, fileNameInBucketName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteFilesBucketOutput(Long movieId, Long episodeId) {
        // Tiền tố để tìm các file trong S3
        String prefix = movieId + "/" + episodeId + "/"; // Ví dụ: "1/27/"

        try {
            // Tạo yêu cầu để liệt kê các đối tượng trong bucket với tiền tố
            ListObjectsV2Request request = new ListObjectsV2Request()
                    .withBucketName(bucketOutput)
                    .withPrefix(prefix);

            ListObjectsV2Result result;

            do {
                result = amazonS3.listObjectsV2(request);
                List<S3ObjectSummary> objects = result.getObjectSummaries();

                // Xóa từng file
                for (S3ObjectSummary objectSummary : objects) {
                    String key = objectSummary.getKey();
                    amazonS3.deleteObject(bucketOutput, key); // Xóa đối tượng trong S3
                    System.out.println("Deleted file: " + key);
                }

                // Kiểm tra nếu có trang tiếp theo
                request.setContinuationToken(result.getNextContinuationToken());
            } while (result.isTruncated()); // Nếu còn đối tượng cần xử lý

            System.out.println("Successfully deleted all files for prefix: " + prefix);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting files in bucket " + bucketOutput + " with prefix " + prefix, e);
        }
    }



    //For Bucket ImagesMovie
    public String uploadFileToBucketImageMovie(MultipartFile file,Long movieId) throws IOException {
        String fileName = movieId + "_" + file.getOriginalFilename();
        amazonS3.putObject(new PutObjectRequest(bucketImagesmovie, fileName, file.getInputStream(), null));
        return amazonS3.getUrl(bucketImagesmovie, fileName).toString();
    }
    public void deleteFileFromBucketImageMovie(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        amazonS3.deleteObject(new DeleteObjectRequest(bucketImagesmovie, fileName));
    }
}

