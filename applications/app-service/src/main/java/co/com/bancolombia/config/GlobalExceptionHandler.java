package co.com.bancolombia.config;

import co.com.bancolombia.model.exception.ErrorModel;
import co.com.bancolombia.model.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorModel> handleUserException(UserException ex, ServerWebExchange exchange) {
        return ResponseEntity.status(HttpStatus.valueOf(ex.getCode()))
                .body(ErrorModel.builder()
                        .message(ex.getMessage())
                        .code(String.valueOf(ex.getCode()))
                        .cause(ex.getCause().toString())
                        .details("Se presento un error inesperado")
                        .build());
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorModel> handleValidationException(WebExchangeBindException ex, ServerWebExchange exchange) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorModel.builder()
                        .message(ex.getMessage())
                        .code("400")
                        .cause(ex.getCause().toString())
                        .details("Se presento un error inesperado")
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorModel> handleGenericException(Exception ex, ServerWebExchange exchange) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorModel.builder()
                        .message(ex.getMessage())
                        .code("500")
                        .cause(ex.getCause().toString())
                        .details("Se presento un error inesperado")
                        .build());
    }
}
