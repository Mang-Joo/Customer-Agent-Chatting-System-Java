package io.github.mangjoo.customer_agent_chat_service.security.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

@Slf4j
public class LoginFilter extends AbstractAuthenticationProcessingFilter {
    private static final String LOGIN_URL = "/api/v1/login";
    private final ObjectMapper objectMapper;

    public LoginFilter(
            ObjectMapper objectMapper,
            CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
            CustomAuthenticationFailureHandler customAuthenticationFailureHandler
    ) {
        super(LOGIN_URL);
        super.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        super.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        this.objectMapper = objectMapper;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
            log.info("Login request: {}", loginRequest.email);

            UsernamePasswordAuthenticationToken token = loginRequest.toAuthenticationToken();

            return this.getAuthenticationManager().authenticate(token);
        } catch (Exception e) {
            log.error("Error while parsing login request", e);
            throw new AuthenticationServiceException("Error while parsing login request", e);
        }
    }

    record LoginRequest(String email, String password) {
            UsernamePasswordAuthenticationToken toAuthenticationToken() {
                if (email == null || password == null || email.isBlank() || password.isBlank()) {
                    throw new AuthenticationServiceException("Email or password is null");
                }
                return new UsernamePasswordAuthenticationToken(email, password);
            }
        }
}
