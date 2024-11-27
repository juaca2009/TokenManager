package co.com.bancolombia.model.users.gateways;

import co.com.bancolombia.model.users.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UsersGateways {

    Mono<User> getUser(String username);
}
