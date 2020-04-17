package com.owodigi.ratings;

import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 */
public class MovieRatingsAPI {
    private static ConfigurableApplicationContext context;

    public static void main(final String[] args) throws IOException {
        final RatingStore store = new RatingStore();
        final IMDbDatasetProcessor processor = new IMDbDatasetProcessor(store);
        store.clear();
        processor.process();
        context = SpringApplication.run(APIServer.class, args);
    }

    public static void stop() {
        if (context != null) {
            context.close();
        }
    }
}
