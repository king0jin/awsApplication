package naver.et0709.awss3;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Component
@RestControllerAdvice
//파일을 업로드하는 도중에 예외가 발생하면 호출되는 메소드를 소유한 Class
public class FileUploadFailedException {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<ErrorResponse>
    handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException e) {
        ErrorResponse response = ErrorResponse.builder(e, HttpStatus.BAD_REQUEST,
                "용량 초과").build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
