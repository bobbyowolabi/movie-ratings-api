package com.owodigi.ratings;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 */
//@SpringBootApplication
//@RestController
public class MovieRatingsAPI {
    private static ConfigurableApplicationContext context;

    public static void main(final String[] args) throws IOException {
//        context = SpringApplication.run(MovieRatingsAPI.class, args);
        final RatingStore store = new RatingStore();
        final IMDbDatasetProcessor processor = new IMDbDatasetProcessor(store);
        store.clear();
        processor.process();
    }

    public static void stop() {
        if (context != null) {
            context.close();
        }
    }
    
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }
}
