package co.com.bancolombia.model.logs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogsActions {

    CREATED("Authenticated and token created"),
    VALIDATED("Token Validated"),
    NO_VALIDATED("Token not validated");

    private final String message;
}
