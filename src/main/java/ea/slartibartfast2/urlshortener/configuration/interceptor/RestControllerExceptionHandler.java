package ea.slartibartfast2.urlshortener.configuration.interceptor;

import ea.slartibartfast2.urlshortener.exception.UrlNotFoundException;
import ea.slartibartfast2.urlshortener.model.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(value = {UrlNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleUrlNotFoundException(UrlNotFoundException ex) {
        log.warn("Caught exception", ex);
        ErrorResponse errorResponse = ErrorResponse.builder().errorMessage(ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }
}
