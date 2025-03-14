package cz.cvut.fel.cafoulu1.flashcards.backend.model.builder;

import cz.cvut.fel.cafoulu1.flashcards.backend.model.AuthProvider;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.CardSet;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.SetStatistics;
import cz.cvut.fel.cafoulu1.flashcards.backend.model.User;

import java.util.List;

public class UserBuilder {
    private String email;
    private String password;
    private String username;
    private AuthProvider provider;
//    private List<CardSet> cardSets;
//    private List<SetStatistics> setStatistics;
//    private List<CardSet> favoriteSets;

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

//    public UserBuilder setCardSets(List<CardSet> cardSets) {
//        this.cardSets = cardSets;
//        return this;
//    }
//
//    public UserBuilder setSetStatistics(List<SetStatistics> setStatistics) {
//        this.setStatistics = setStatistics;
//        return this;
//    }
//
//    public UserBuilder setFavoriteSets(List<CardSet> favoriteSets) {
//        this.favoriteSets = favoriteSets;
//        return this;
//    }

    public User build() {
        User user = new User();
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setUsername(this.username);
        user.setProvider(this.provider);
//        user.setCardSets(this.cardSets);
//        user.setSetStatistics(this.setStatistics);
//        user.setFavoriteSets(this.favoriteSets);
        return user;
    }
}
