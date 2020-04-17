package com.owodigi.ratings;

import com.owodigi.ratings.store.impl.H2StoreTest;
import com.owodigi.ratings.util.RatingsAppProperties;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockserver.integration.ClientAndServer;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public abstract class MovieRatingAPIConfiguration extends H2StoreTest {
    private static final Path APPLICATION_PROPERTIES_PATH = Paths.get("./target/test-data/ratings-app.properties");
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRatingAPIConfiguration.class);
    private static ClientAndServer mockServer;
    private static int port;

    static {
        System.setProperty("mockserver.logLevel", "OFF");
    }
    
    @BeforeClass
    public static void setupTestSuite() throws IOException {
        setupIMDbDatasetServer();
        setApplicationProperties();
    }
    
    private static void setupIMDbDatasetServer() throws IOException {
        mockServer = startClientAndServer(0);
        port = mockServer.getLocalPort();
        LOGGER.info("Starting up IMDb Dataset Server on port " + port);
        whenRequest("/title.basics.tsv.gz", "src/test/resources/title.basics.tsv.gz");
        whenRequest("/title.ratings.tsv.gz", "src/test/resources/title.ratings.tsv.gz");
        whenRequest("/title.principals.tsv.gz", "src/test/resources/title.principals.tsv.gz");
        whenRequest("/title.episode.tsv.gz", "src/test/resources/title.episode.tsv.gz");
        whenRequest("/name.basics.tsv.gz", "src/test/resources/name.basics.tsv.gz");
        LOGGER.info("Set IMDb Dataset Server expectations for request of the .tsv.gz files");
    }

    private static void setApplicationProperties() throws IOException {
        LOGGER.info("setApplicationProperties");
        Files.deleteIfExists(APPLICATION_PROPERTIES_PATH);
        LOGGER.info("Deleting " + APPLICATION_PROPERTIES_PATH);
        final Properties applicationProperties = new Properties();
        applicationProperties.load(Files.newInputStream(Paths.get("src/test/resources/ratings-app.properties")));
        applicationProperties.setProperty(RatingsAppProperties.DATABASE_PATH, uniqueDbPath().toString());
        applicationProperties.setProperty(RatingsAppProperties.IMDB_TITLE_BASICS_URL, "http://localhost:" + port + "/title.basics.tsv.gz");
        applicationProperties.setProperty(RatingsAppProperties.IMBD_NAME_BASICS_URL, "http://localhost:" + port + "/name.basics.tsv.gz");
        applicationProperties.setProperty(RatingsAppProperties.IMBD_TITLE_EPISODE_URL, "http://localhost:" + port + "/title.episode.tsv.gz");
        applicationProperties.setProperty(RatingsAppProperties.IMBD_TITLE_PRINCIPALS_URL, "http://localhost:" + port + "/title.principals.tsv.gz");
        applicationProperties.setProperty(RatingsAppProperties.IMBD_TITLE_RATINGS_URL, "http://localhost:" + port + "/title.ratings.tsv.gz");
        createDirectoryIfNotExists(APPLICATION_PROPERTIES_PATH.getParent());
        applicationProperties.store(Files.newOutputStream(APPLICATION_PROPERTIES_PATH), "");
        LOGGER.info("Set application properties " + applicationProperties + " to be stored " + APPLICATION_PROPERTIES_PATH);
        System.setProperty(RatingsAppProperties.SYSTEM_PROPERTIES_FILE, APPLICATION_PROPERTIES_PATH.toString());      
        LOGGER.info("Set System property " + RatingsAppProperties.SYSTEM_PROPERTIES_FILE + " to " + APPLICATION_PROPERTIES_PATH);
    }    
    
    @After
    public void cleanup() throws IOException {
        Files.deleteIfExists(APPLICATION_PROPERTIES_PATH);
        LOGGER.info("Deleted " + APPLICATION_PROPERTIES_PATH);
    }  

    @AfterClass
    public static void cleanupIMDbDataServer() {
        LOGGER.info("Stopping IMDb Dataset Server on port " + port);
        mockServer.stop();
    }
    
    private static void createDirectoryIfNotExists(final Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createDirectory(path);
        }
    }
    
    /**
     *
     * @param requestPath
     * @param responsePath
     * @throws IOException
     */
    private static void whenRequest(final String requestPath, final String responsePath) throws IOException {
        LOGGER.info("Adding Expection of request path: " + requestPath + "; response: " + requestPath + "; to http://localhost:" + port);
        mockServer
            .when(
                request()
                    .withMethod("GET")
                    .withPath(requestPath)
            )
            .respond(
                response()
                    .withStatusCode(200)
                    .withBody(Files.readAllBytes(Paths.get(responsePath)))
            );
    }    
}
