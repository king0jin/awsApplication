package naver.et0709.awss3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3ServiceImpl implements AwsS3Service {
    private AmazonS3 amazonS3Client;

    //Properties에서 값을 가져와서 설정
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.region.static}")
    private String region;

    //생성자가 호출된 후에 수행할 메서드
    @PostConstruct
    public void setS3Client() {
        //AWS 로그인하여 amazonS3Client생성
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey,
                this.secretKey);
        amazonS3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    //파일 존재 여부를 확인하여 리턴하는 메소드
    private  boolean validateFileExists(MultipartFile multipartFile) {
        boolean result = true;
        if (multipartFile.isEmpty()) {
            result = false;
        }
        return result;
    }

    //실질적으로 수행될 메소드
    @Override
    public String uploadFile(String category, MultipartFile multipartFile) {
        //파일 여부를 리턴받는다
        boolean result = validateFileExists(multipartFile);
        //파일 존재여부가 false이면 아무것도 리턴하지 않을 것
        if(result == false){
            return null;
        }
        //파일 존재여부가 true이면 파일 경로 생성
        String fileName = CommonUtils.buildFileName(category, multipartFile.getOriginalFilename());
        //파일 업로드 준비
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        try (InputStream inputStream = multipartFile.getInputStream()) {
            //파일 업로드
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            return null;
        }

        return amazonS3Client.getUrl(bucketName, fileName).toString();

    }
}
