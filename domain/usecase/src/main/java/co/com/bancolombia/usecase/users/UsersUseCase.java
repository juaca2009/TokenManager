package co.com.bancolombia.usecase.users;

import co.com.bancolombia.model.exception.ExceptionMessage;
import co.com.bancolombia.model.exception.TokenException;
import co.com.bancolombia.model.users.User;
import co.com.bancolombia.model.users.gateways.UsersGateways;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class UsersUseCase {

    private final UsersGateways usersGateways;

    public Mono<User> authenticateUser(String username, String password){
        return usersGateways.getUser(username)
                .flatMap(user -> {
                    if (user.getPassword().equals(password)) {
                        return Mono.just(user);
                    } else {
                        return Mono.error(new TokenException(ExceptionMessage.BAD_PASSWORD.getCode(),
                                ExceptionMessage.BAD_PASSWORD.getMessage()));
                    }
                });

    }
}
