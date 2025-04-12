package cz.cvut.fel.cafoulu1.flashcards.backend.security.response;

import cz.cvut.fel.cafoulu1.flashcards.backend.security.jwtconfig.JwtUtils;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.oauth2.OAuth2UserImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Duration;

/**
 * This class is used for setting cookies in the response.
 */
public class CookieSetup {
    public static void setCookies(HttpServletResponse response, Authentication authentication, JwtUtils jwtUtils) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(false)
                .path("/")
//                .domain("localhost")
                .maxAge(Duration.ofDays(7))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        Object principal = authentication.getPrincipal();
        ResponseCookie adminCookie = ResponseCookie.from("isAdmin", String.valueOf(
                (principal instanceof UserDetailsImpl && ((UserDetailsImpl) principal).getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) ||
                        (principal instanceof OAuth2UserImpl && ((OAuth2UserImpl) principal).getAuthorities().stream()
                                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")))))
                .httpOnly(true)
                .secure(false) // Nastav na `true`, pokud jedeš přes HTTPS!
                .path("/")
                .maxAge(Duration.ofDays(7))
//                .domain("localhost")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, adminCookie.toString());
    }

    public static void unsetCookies(HttpServletResponse response) {
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)  // Nastav na true, pokud používáš HTTPS
                .path("/")
//                .domain("localhost")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        ResponseCookie adminCookie = ResponseCookie.from("isAdmin", "")
                .httpOnly(true)
                .secure(false)  // Nastav na true, pokud používáš HTTPS
                .path("/")
                .maxAge(0)
//                .domain("localhost")
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, adminCookie.toString());
    }
}
