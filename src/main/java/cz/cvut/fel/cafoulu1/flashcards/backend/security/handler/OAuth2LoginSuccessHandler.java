package cz.cvut.fel.cafoulu1.flashcards.backend.security.handler;

import cz.cvut.fel.cafoulu1.flashcards.backend.security.jwtconfig.JwtUtils;
import cz.cvut.fel.cafoulu1.flashcards.backend.security.response.CookieSetup;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This class is used for handling successful OAuth2 login.
 */
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${frontend.url}")
    private String frontendUrl;

    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CookieSetup.setCookies(response, authentication, jwtUtils);
        String authorization = response.getHeader("Authorization");
        String jwt = authorization.substring(7);
        String isAdmin = response.getHeader("X-Is-Admin");
        String redirectUrl = frontendUrl + "/auth/redirect" +
                "?token=" + jwt +
                "&isAdmin=" + isAdmin;
        response.sendRedirect(redirectUrl);
    }
}
