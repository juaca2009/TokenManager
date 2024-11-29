package co.com.bancolombia.api.tokenManagment;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;

@Configuration
@RequiredArgsConstructor
public class TokenRoutes {

    @Value("${api.base-path}")
    private final String basePath;

    @Value("${api.auth-path}")
    private final String authPath;

    @Bean
    public RouterFunction<ServerResponse> route(TokenHandler tokenHandler) {
        return RouterFunctions.route()
                .nest(path(basePath), builder -> builder
                        .POST(authPath, tokenHandler::generateTokenHandler))
                .build();
    }
}
