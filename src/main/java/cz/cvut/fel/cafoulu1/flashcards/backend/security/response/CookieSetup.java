package cz.cvut.fel.cafoulu1.flashcards.backend.security.response;

import cz.cvut.fel.cafoulu1.flashcards.backend.security.jwtconfig.JwtUtils;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.oauth2.OAuth2UserImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
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
        Object principal = authentication.getPrincipal();
        boolean isAdmin = (principal instanceof UserDetailsImpl && ((UserDetailsImpl) principal).getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) ||
                (principal instanceof OAuth2UserImpl && ((OAuth2UserImpl) principal).getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")));
        response.setHeader("X-Is-Admin", String.valueOf(isAdmin));
    }

    public static void unsetCookies(HttpServletResponse response) {
        response.setHeader(HttpHeaders.AUTHORIZATION, null);
        response.setHeader("X-Is-Admin", null);
    }
}
