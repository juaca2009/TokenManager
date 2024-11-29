package co.com.bancolombia.usersdrivenadapter;

import co.com.bancolombia.model.exception.ExceptionMessage;
import co.com.bancolombia.model.exception.UserException;
import co.com.bancolombia.model.users.User;
import co.com.bancolombia.model.users.gateways.UsersGateways;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class UsersRepository implements UsersGateways {

    private final List<User> userList;

    @Autowired
    public UsersRepository(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public Mono<User> getUser(String username) {
        return Mono.justOrEmpty(userList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst())
                .switchIfEmpty(Mono.error(new UserException(ExceptionMessage.USER_NOT_FOUND.getCode(),
                        ExceptionMessage.USER_NOT_FOUND.getMessage())));
    }
}
