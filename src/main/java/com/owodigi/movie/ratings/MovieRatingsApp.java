package com.owodigi.movie.ratings;

import com.owodigi.movie.ratings.api.APIServer;
import com.owodigi.movie.ratings.store.RatingStore;
import com.owodigi.imdb.IMDbDatasetProcessor;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 */
public class MovieRatingsApp {
    private static ConfigurableApplicationContext context;
    private static RatingStore store;

    public static void main(final String[] args) throws IOException {
        store = new RatingStore();
        final IMDbDatasetProcessor processor = new IMDbDatasetProcessor(store);
        store.clear();
        processor.process();
        context = SpringApplication.run(APIServer.class, args);
    }

    public static RatingStore store() {
        return store;
    }
    
    public static void stop() {
        if (context != null) {
            context.close();
        }
    }  
}
