package com.owodigi.movie.ratings;

import com.owodigi.movie.ratings.store.impl.H2MovieStore;
import com.owodigi.imdb.IMDbDatasetProcessor;
import com.owodigi.movie.ratings.api.APIServer;
import com.owodigi.movie.ratings.util.MovieRatingsAppProperties;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import com.owodigi.movie.ratings.store.MovieStore;
import java.util.Timer;
import java.util.TimerTask;
import org.springframework.boot.SpringApplication;

public class MovieRatingsApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRatingsApp.class);
    private static ConfigurableApplicationContext context;
    private static MovieStore store;
    private static IMDbDatasetProcessor processor;
    private static final long ONE_DAY = 86_400_000; 

    public static void main(final String[] args) throws IOException {
        LOGGER.info("Create RatingStore");
        try {
            store = new H2MovieStore(MovieRatingsAppProperties.databaseUserName(), MovieRatingsAppProperties.databaseUserPassword(), MovieRatingsAppProperties.databasePath());
            processor = new IMDbDatasetProcessor(store);
            DATASET_PROCESSOR_TASK.run();            
            context = SpringApplication.run(APIServer.class, args);
            final Timer timer = new Timer();
            timer.scheduleAtFixedRate(DATASET_PROCESSOR_TASK, ONE_DAY, ONE_DAY);
        } catch (final Exception ex) {
            LOGGER.warn("Unable to continue due to " + ex.getMessage(), ex);
        }
    }
    
    public static MovieStore store() {
        return store;
    }
    
    /**
     * Closes this applications and releases all of its associated resources.
     * 
     * @throws Exception 
     */
    public static void stop() throws Exception {
        if (store != null) {
            store.close();
        }
        if (context != null) {
            context.close();
        }
    }  
    
    private static final TimerTask DATASET_PROCESSOR_TASK = new TimerTask() {
        
        @Override
        public void run() {
            try {
                LOGGER.info("Starting processing of Datasets");
                LOGGER.info("Clearing out all Datastores");
                store.clear();            
                processor.process();
            } catch (IOException ex) {
                LOGGER.error("Unable to complete update task", ex);
            }
        }
    };
}
