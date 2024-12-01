package co.com.bancolombia.model.users.gateways;

import co.com.bancolombia.model.users.User;
import reactor.core.publisher.Mono;

public interface UsersGateways {

    Mono<User> getUser(String username);
    Mono<User> getUserById(String id);
}
