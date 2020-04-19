package com.owodigi.movie.ratings;

import com.owodigi.movie.ratings.api.APIServer;
import com.owodigi.movie.ratings.store.impl.H2RatingStore;
import com.owodigi.imdb.IMDbDatasetProcessor;
import com.owodigi.movie.ratings.store.domain.RatingStore;
import com.owodigi.movie.ratings.util.MovieRatingsAppProperties;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 */
public class MovieRatingsApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRatingsApp.class);
    private static ConfigurableApplicationContext context;
    private static RatingStore store;

    public static void main(final String[] args) throws IOException {
        LOGGER.info("Create RatingStore");
        try {
            store = new H2RatingStore(MovieRatingsAppProperties.databaseUserName(), MovieRatingsAppProperties.databaseUserPassword(), MovieRatingsAppProperties.databasePath());
            final IMDbDatasetProcessor processor = new IMDbDatasetProcessor(store);
            LOGGER.info("Clearing out all Datastores");
            store.clear();
            processor.process();
            context = SpringApplication.run(APIServer.class, args);
        } catch (final Exception ex) {
            LOGGER.warn("Unable to continue due to " + ex.getMessage(), ex);
        }
    }

    public static RatingStore store() {
        return store;
    }
    
    public static void stop() throws Exception {
        if (store != null) {
            store.close();
        }
        if (context != null) {
            context.close();
        }
    }  
}
