package io.github.mangjoo.customer_agent_chat_service.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mangjoo.customer_agent_chat_service.member.model.DuplicateLoginCheck;
import io.github.mangjoo.customer_agent_chat_service.security.login.CustomAuthenticationFailureHandler;
import io.github.mangjoo.customer_agent_chat_service.security.login.CustomAuthenticationSuccessHandler;
import io.github.mangjoo.customer_agent_chat_service.security.login.LoginFilter;
import io.github.mangjoo.customer_agent_chat_service.security.login.LoginProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final ObjectMapper objectMapper;
    private final LoginProvider loginProvider;
    private final DuplicateLoginCheck duplicateLoginCheck;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:5173", "https://chat-backend.mangjoo.com"));
                    corsConfiguration.setAllowedMethods(List.of("*"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                                .maximumSessions(1)
                                .maxSessionsPreventsLogin(true)
                )
                .securityContext(c -> c.requireExplicitSave(false))
                .authorizeHttpRequests(requests ->
                        requests
                                .requestMatchers("/api/v1/login", "/api/v1/register", "/login.html").permitAll()
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs")
                                .hasAuthority("ADMIN")
                                .anyRequest()
                                .authenticated()
                )
                .addFilterBefore(loginFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new SessionCheckFilter(duplicateLoginCheck), AnonymousAuthenticationFilter.class)
                .authenticationProvider(loginProvider);
        return http.build();
    }

    private LoginFilter loginFilter(AuthenticationManager authenticationManager) {
        LoginFilter loginFilter = new LoginFilter(objectMapper, customAuthenticationSuccessHandler, customAuthenticationFailureHandler);
        loginFilter.setAuthenticationManager(authenticationManager);
        return loginFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(loginProvider);
    }
}
