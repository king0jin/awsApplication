package naver.et0709.awss3;

import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Service {
    public String uploadFile(String category, MultipartFile multipartFile);
}
