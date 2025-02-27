package cz.cvut.fel.cafoulu1.flashcards.backend.security.response;

import lombok.Getter;
import lombok.Setter;

/**
 * This code was taken from <a href="https://github.com/eugenp/tutorials/blob/master/spring-security-modules/spring-security-core/src/main/java/com/baeldung/jwtsignkey/response/JwtResponse.java">baeldung GitHub</a>
 * and modified for the purpose of this application.
 */
@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";

    private String email;

    public JwtResponse(String accessToken, String email) {
        this.token = accessToken;
        this.email = email;
    }
}
