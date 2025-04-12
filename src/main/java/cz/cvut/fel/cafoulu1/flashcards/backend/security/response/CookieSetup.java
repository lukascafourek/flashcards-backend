package cz.cvut.fel.cafoulu1.flashcards.backend.security.response;

import cz.cvut.fel.cafoulu1.flashcards.backend.security.jwtconfig.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * This class is used for setting cookies in the response.
 */
public class CookieSetup {
    public static void setCookies(HttpServletResponse response, Authentication authentication, JwtUtils jwtUtils) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        System.out.println("Authorization header set with Bearer token: " + jwt);
    }

    public static void unsetCookies(HttpServletResponse response) {
        response.setHeader(HttpHeaders.AUTHORIZATION, null);
        System.out.println("Authorization and X-Is-Admin headers have been removed.");
    }
}
