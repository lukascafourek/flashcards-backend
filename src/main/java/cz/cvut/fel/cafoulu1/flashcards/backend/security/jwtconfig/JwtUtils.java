package cz.cvut.fel.cafoulu1.flashcards.backend.security.jwtconfig;

import java.util.Date;

import javax.crypto.SecretKey;

import cz.cvut.fel.cafoulu1.flashcards.backend.service.oauth2.OAuth2UserImpl;
import cz.cvut.fel.cafoulu1.flashcards.backend.service.userdetails.UserDetailsImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

/**
 * This code was taken from
 * <a href="https://github.com/eugenp/tutorials/blob/master/spring-security-modules/spring-security-core/src/main/java/com/baeldung/jwtsignkey/jwtconfig/JwtUtils.java">eugenp</a>
 * on GitHub and modified for the purpose of this application.
 * <p>
 * This class is used to generate and validate JWT tokens.
 */
@Component
public class JwtUtils {
    private static final Logger logger = LogManager.getLogger(JwtUtils.class);

    public String generateJwtToken(Authentication authentication) {
        int jwtExpirationMs = 1000 * 60 * 60 * 24 * 7;
        String subject = null;
        if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            subject = userDetails.getUsername();
        } else if (authentication.getPrincipal() instanceof OAuth2UserImpl oAuth2User) {
            subject = oAuth2User.getEmail();
        }
        if (subject != null) {
            return Jwts.builder()
                    .subject(subject)
                    .issuedAt(new Date())
                    .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                    .signWith(getSigningKey())
                    .compact();
        }
        return null;
    }

    private SecretKey getSigningKey() {
        String jwtSecret = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
