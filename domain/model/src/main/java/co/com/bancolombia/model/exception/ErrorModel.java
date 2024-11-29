package co.com.bancolombia.model.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class ErrorModel {

    private final String message;
    private final String code;
    private final String cause;
    private final String details;
}
