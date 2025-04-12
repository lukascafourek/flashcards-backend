package cz.cvut.fel.cafoulu1.flashcards.backend.controller.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Enumeration;

/**
 * Detailed logging strategy for logging detailed information about request.
 */
@Component
@Scope("singleton")
public class DetailedLoggingStrategy implements LoggingStrategy {
    @Override
    public void logPreHandle(HttpServletRequest request) {
        log.info("[preHandle][{}][{}]{}{}", request, request.getMethod(), request.getRequestURI(), getParameters(request));
    }

    @Override
    public void logPostHandle(HttpServletRequest request) {
        log.info("[postHandle][{}]", request);
    }

    private String getParameters(HttpServletRequest request) {
        StringBuilder posted = new StringBuilder();
        Enumeration<?> e = request.getParameterNames();
        if (e != null) {
            posted.append("?");
        }
        while (true) {
            assert e != null;
            if (!e.hasMoreElements()) break;
            if (posted.length() > 1) {
                posted.append("&");
            }
            String curr = (String) e.nextElement();
            posted.append(curr).append("=");
            if (curr.contains("password")
                    || curr.contains("pass")
                    || curr.contains("pwd")) {
                posted.append("*****");
            } else {
                posted.append(request.getParameter(curr));
            }
        }
        String ip = request.getHeader("X-FORWARDED-FOR");
        String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
        if (ipAddr!=null && !ipAddr.isEmpty()) {
            posted.append("&_psip=").append(ipAddr);
        }
        return posted.toString();
    }

    private String getRemoteAddr(HttpServletRequest request) {
        String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && !ipFromHeader.isEmpty()) {
            log.debug("ip from proxy - X-FORWARDED-FOR : {}", ipFromHeader);
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }
}
