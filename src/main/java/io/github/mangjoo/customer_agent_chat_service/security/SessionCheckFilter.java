package io.github.mangjoo.customer_agent_chat_service.security;

import io.github.mangjoo.customer_agent_chat_service.exception.MangJooException;
import io.github.mangjoo.customer_agent_chat_service.member.model.DuplicateLoginCheck;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("NullableProblems")
public class SessionCheckFilter extends OncePerRequestFilter {
    private final DuplicateLoginCheck duplicateLoginCheck;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        String sessionId = session.getId();

        if (userId != null) {
            try {
                duplicateLoginCheck.checkLogin(userId, sessionId);
                filterChain.doFilter(request, response);
            } catch (MangJooException e) {
                log.error("Duplicate login detected {}", userId, e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                session.invalidate();
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

}
