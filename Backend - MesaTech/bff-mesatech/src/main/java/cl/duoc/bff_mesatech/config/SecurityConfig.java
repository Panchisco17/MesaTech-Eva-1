package cl.duoc.bff_mesatech.config; 

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configure(http))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.POST, "/api/v1/productos/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/api/v1/productos/**", "/api/v2/productos/**").authenticated()
                
                .requestMatchers(HttpMethod.PUT, "/api/v1/solicitudes/*/estado", "/api/v2/solicitudes/*/estado")
                    .hasAnyRole("OPERADOR", "ADMINISTRADOR")
                
                .requestMatchers(HttpMethod.GET, "/api/v1/solicitudes/usuario/**").hasAnyRole("CLIENTE", "OPERADOR", "ADMINISTRADOR")

                .requestMatchers(HttpMethod.GET, "/api/v1/solicitudes").hasAnyRole("OPERADOR", "ADMINISTRADOR")
                
                .requestMatchers(HttpMethod.POST, "/api/v1/solicitudes").authenticated()

                .requestMatchers(HttpMethod.DELETE, "/api/v1/solicitudes/**").hasRole("ADMINISTRADOR")
                
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix(""); 

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}