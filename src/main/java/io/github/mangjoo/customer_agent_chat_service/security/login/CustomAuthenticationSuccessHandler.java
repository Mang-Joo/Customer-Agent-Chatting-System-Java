package io.github.mangjoo.customer_agent_chat_service.security.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mangjoo.customer_agent_chat_service.member.model.DuplicateLoginCheck;
import io.github.mangjoo.customer_agent_chat_service.member.model.Member;
import io.github.mangjoo.customer_agent_chat_service.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final DuplicateLoginCheck duplicateLoginCheck;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("Login Success");



        HttpSession session = request.getSession();
        Long id = (Long) authentication.getPrincipal();
        session.setAttribute("userId", id);
        duplicateLoginCheck.login(id, session.getId());

        Member principal = memberRepository.findById(id);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        GrantedAuthority grantedAuthority = authorities.stream().findFirst().orElseThrow();

        objectMapper.writeValue(response.getWriter(), new LoginResponse(principal.getUserId(), principal.getName(), grantedAuthority.getAuthority()));
    }


}
