package com.owodigi.ratings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class APITest extends MovieRatingAPIConfiguration {
    private static final String RATINGS_APP_URL = "http://localhost:7272/ratings?title=foo";
    
    private void assertQuery(final String message, final String expected) throws IOException {
        HttpURLConnection connection = null;
        try {
            final URL url = new URL(RATINGS_APP_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            final int statusCode = connection.getResponseCode();
            if (statusCode <= 299) {
                final String actual = toString(connection.getInputStream());
                Assert.assertEquals(expected, actual);
            } else {
                Assert.fail(message + ": Unexpected response code " + statusCode + toString(connection.getErrorStream()));
            }
        } catch (final MalformedURLException ex) {
            Assert.fail(message + ": URL to movie API is invalid due to " + ex);
        } catch (final ProtocolException ex) {
            Assert.fail(message + ": The request method is invalud due to " + ex);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    @Test
    public void testAPI() throws IOException {
        MovieRatingsAPI.main(new String[0]);
        try {
            queryNonTVShow();
            queryTvShow();
            queryTvShowEpisode();
            queryNonExistentTitle();
            
        } finally {
            MovieRatingsAPI.stop();
        }
    }
    
    public void queryNonTVShow() throws IOException {
        final String expected = "{\n"
                + "   \"title\": \"Foo\",\n"
                + "   \"type\": \"tvSeries\",\n"
                + "   \"userRating: \"5.4\",\n"
                + "   \"castList\": \"Person 1, Person 2\",\n"
                + "   \"calculatedRating\": 5.9,\n"
                + "   \"episodes\": [{\n"
                + "      \"title\": \"foo\",\n"
                + "      \"userRating\": 6.5,\n"
                + "      \"seasonNumber\": 1,\n"
                + "      \"episodeNumber\": 10,\n"
                + "      \"castList\": \"Person 1, Person 2\"      \n"
                + "   }]\n"
                + "}";
        assertQuery("Querying a Non TV Show", expected);
    }

    public void queryTvShow() throws IOException {
    }
    
    public void queryTvShowEpisode() throws IOException {
    }
    
    public void queryNonExistentTitle() throws IOException {
    }
    
    /**
     * Does not maintain original line ending characters
     * 
     * @param inputStream
     * @return
     * @throws IOException 
     */
    private String toString(final InputStream inputStream) throws IOException {
        final StringBuilder value = new StringBuilder();
        try(final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            value.append(reader.readLine()).append("\n");
        }
        return value.toString();
    }
}
