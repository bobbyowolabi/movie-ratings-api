package com.owodigi.ratings;

import com.owodigi.ratintgs.RatingsApp;
import com.owodigi.ratintgs.util.RatingsAppProperties;
import java.io.IOException;
import org.junit.Test;

/**
 *
 */
public class RatingsAppTest {

    @Test
    public void testRun() throws IOException {
        System.setProperty(RatingsAppProperties.SYSTEM_PROPERTIES_FILE, "src/test/resources/ratings-app.properties");
        RatingsApp.main(new String[0]);
    }
}
