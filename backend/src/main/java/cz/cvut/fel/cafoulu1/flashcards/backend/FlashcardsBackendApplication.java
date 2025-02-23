package cz.cvut.fel.cafoulu1.flashcards.backend;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Objects;

/**
 * Main class of the flashcards backend application.
 */
@EnableCaching
@EnableScheduling
@RequiredArgsConstructor
@SpringBootApplication
public class FlashcardsBackendApplication implements CommandLineRunner {
    private final CacheManager cacheManager;

    public static void main(String[] args) {
        SpringApplication.run(FlashcardsBackendApplication.class, args);
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("Flashcards API")
                .description("Flashcards backend application API documentation")
                .version("1.0.0")
        );
    }

    @Scheduled(fixedRate = 1000 * 3600 * 2) // every 2 hours
    public void evictAllCachesAtIntervals() {
        cacheManager.getCacheNames().forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }

    @Override
    public void run(String... args) {
    }
}
