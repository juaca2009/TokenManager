package co.com.bancolombia.model.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {

    UNAUTHORIZED("401","Unauthorized"),
    USER_NOT_FOUND("404","User not found"),
    BAD_PASSWORD("401","Bad password"),
    INVALID_REQUEST("400","Bad request"),
    NO_IP("400","We could´nt ge the IP");


    private final String code;
    private final String message;
}
