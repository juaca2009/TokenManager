package co.com.bancolombia.api.tokenManagment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateTokenBody {

    private String username;
    private String password;
}
