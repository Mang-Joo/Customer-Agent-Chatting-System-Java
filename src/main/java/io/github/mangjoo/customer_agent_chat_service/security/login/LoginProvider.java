package io.github.mangjoo.customer_agent_chat_service.security.login;

import io.github.mangjoo.customer_agent_chat_service.exception.MangJooException;
import io.github.mangjoo.customer_agent_chat_service.member.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginProvider implements AuthenticationProvider {
    private final LoginService loginService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            final String username = (String) authentication.getPrincipal();
            final String password = (String) authentication.getCredentials();

            MemberLogin login = new MemberLogin(username, password);
            Member member = loginService.login(login);

            var authority = List.of(new SimpleGrantedAuthority(member.getRole().name()));

            return new UsernamePasswordAuthenticationToken(member.getUserId(), null, authority);
        } catch (MangJooException e) {
            log.error("Login failed", e);
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
