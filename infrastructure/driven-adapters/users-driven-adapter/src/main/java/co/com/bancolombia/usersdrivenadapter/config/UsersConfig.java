package co.com.bancolombia.usersdrivenadapter.config;

import co.com.bancolombia.model.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class UsersConfig {

    private final List<User> userList;

    @Bean
    public List<User> setUserList() {
        User user1 = createUser(
                UUID.fromString("3f2b9c64-8b54-4d26-8c12-7dbfa23c9edc"),"andres97","123",
                "andres97@gmail.com","Andres Salazar","user", LocalDateTime.now(), LocalDateTime.now()
        );
        User user2 = createUser(
                UUID.fromString("9ac72532-e531-4c4f-9244-2988c9e1b9ff"), "juli99", "123",
                "juli@gmail.com", "Julian Paredes", "admin", LocalDateTime.now(), LocalDateTime.now()
        );
        userList.add(user1);
        userList.add(user2);
        return userList;
    }

    public User createUser(UUID id, String username, String password, String email, String fullName, String role,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        return User.builder()
                .id(id)
                .username(username)
                .password(password)
                .email(email)
                .fullName(fullName)
                .role(role)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
