package de.whiteo.mylfa.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthInterceptor implements HandlerInterceptor {

    private final ThreadLocal<String> users = new ThreadLocal<>();

    private final TokenInteract tokenInteract;


    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        if (request.getServletPath().equals("/version")
                || request.getServletPath().equals("/api/v1/authenticate")
                || request.getServletPath().equals("/api/v1/user/create")
                || request.getRequestURI().equals("/api/v1/authenticate")
                || request.getRequestURI().equals("/api/v1/user/create")) {
            return true;
        }

        String token = tokenInteract.getToken(request);
        if (tokenInteract.validateToken(token)) {
            String user = tokenInteract.getUser(token);
            if (StringUtils.isNotBlank(user)) {
                users.set(user);
                return true;
            }
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        return false;
    }

    public String getUserName() {
        return users.get();
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                           @NonNull Object handler, ModelAndView modelAndView) {
        users.remove();
    }
}