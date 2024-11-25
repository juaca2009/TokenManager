package co.com.bancolombia.api;

import co.com.bancolombia.api.tokenManagment.TokenRoutes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import co.com.bancolombia.usecase.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody TokenRoutes.LoginRequest loginRequest) {
        return authService.authenticate(LoginRequest)
                .map(token -> ResponseEntity.ok(token))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(401).body("Invalid credentials")));
    }

}
