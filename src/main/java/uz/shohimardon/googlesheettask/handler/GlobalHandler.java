package uz.shohimardon.googlesheettask.handler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.shohimardon.googlesheettask.response.ApiErrorResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<ApiErrorResponse> handle400(IndexOutOfBoundsException e, HttpServletRequest request) {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .friendlyMessage(e.getMessage())
                .developerMessage(Arrays.toString(e.getStackTrace()))
                .requestPath(request.getRequestURL().toString())
                .build(),
                HttpStatus.BAD_REQUEST);
    }    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiErrorResponse> handle400(NullPointerException e, HttpServletRequest request) {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .friendlyMessage(e.getMessage())
                .developerMessage(Arrays.toString(e.getStackTrace()))
                .requestPath(request.getRequestURL().toString())
                .build(),
                HttpStatus.BAD_REQUEST);
    }
}
