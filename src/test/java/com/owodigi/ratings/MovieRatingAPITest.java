package com.owodigi.ratings;

import java.io.IOException;
import org.junit.AfterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public abstract class MovieRatingAPITest extends MovieRatingAPIConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRatingAPITest.class);
    private static int runCount = 0;
    private static boolean started = false;
    
    protected static int runCount() {
        return runCount;
    }
    
    protected static void start() throws IOException {
        if (started == false) {
            LOGGER.info("Starting MovieRatingAPI");
            ++runCount;
            started = true;
            MovieRatingsAPI.main(new String[0]);
        }
    }
    
    @AfterClass
    public static void stop() {
        if (started) {
            LOGGER.info("Stopping MovieRatingAPI");
            started = false;
            MovieRatingsAPI.stop();
        }
    }
}
