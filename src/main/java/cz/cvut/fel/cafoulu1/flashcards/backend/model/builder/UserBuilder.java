package cz.cvut.fel.cafoulu1.flashcards.backend.model.builder;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.AuthProvider;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;

/**
 * Builder class for creating User objects.
 */
public class UserBuilder {
    private String email;
    private String password;
    private String username;
    private AuthProvider provider;

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder setProvider(AuthProvider provider) {
        this.provider = provider;
        return this;
    }

    public User build() {
        User user = new User();
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setUsername(this.username);
        user.setProvider(this.provider);
        return user;
    }
}
