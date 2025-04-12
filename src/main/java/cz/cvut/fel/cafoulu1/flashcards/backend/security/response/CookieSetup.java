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
                .secure(true)
                .path("/")
                .sameSite("None")
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
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(Duration.ofDays(7))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, adminCookie.toString());
        System.out.println("JWT: " + jwt);
        System.out.println("isAdmin: " + adminCookie.getValue());
        System.out.println("JWT cookie: " + jwtCookie);
        System.out.println("isAdmin cookie: " + adminCookie);
        System.out.println(response.getHeader(HttpHeaders.SET_COOKIE));
    }

    public static void unsetCookies(HttpServletResponse response) {
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        ResponseCookie adminCookie = ResponseCookie.from("isAdmin", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, adminCookie.toString());
        System.out.println("JWT cookie: " + jwtCookie);
        System.out.println("isAdmin cookie: " + adminCookie);
        System.out.println(response.getHeader(HttpHeaders.SET_COOKIE));
    }
}
