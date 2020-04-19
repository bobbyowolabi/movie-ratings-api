package com.owodigi.movie.ratings.util;

import com.owodigi.movie.ratings.store.impl.H2StoreTest;
import com.owodigi.movie.ratings.store.impl.util.H2Connection;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.mockserver.integration.ClientAndServer;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 *
 */
public abstract class MovieRatingsAppConfiguration extends H2StoreTest {
    private static final Path UNIQUE_DB_PATH = uniqueDbPath();
    private static final Path APPLICATION_PROPERTIES_PATH = Paths.get("./target/test-data/ratings-app.properties");
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
        whenRequest("/title.basics.tsv.gz", "src/test/resources/title.basics.tsv.gz");
        whenRequest("/title.ratings.tsv.gz", "src/test/resources/title.ratings.tsv.gz");
        whenRequest("/title.principals.tsv.gz", "src/test/resources/title.principals.tsv.gz");
        whenRequest("/title.episode.tsv.gz", "src/test/resources/title.episode.tsv.gz");
        whenRequest("/name.basics.tsv.gz", "src/test/resources/name.basics.tsv.gz");
    }

    private static void setApplicationProperties() throws IOException {
        Files.deleteIfExists(APPLICATION_PROPERTIES_PATH);
        final Properties applicationProperties = new Properties();
        applicationProperties.load(Files.newInputStream(Paths.get("src/test/resources/ratings-app.properties")));
        applicationProperties.setProperty(MovieRatingsAppProperties.DATABASE_PATH, UNIQUE_DB_PATH.toString());
        applicationProperties.setProperty(MovieRatingsAppProperties.IMDB_TITLE_BASICS_URL, "http://localhost:" + port + "/title.basics.tsv.gz");
        applicationProperties.setProperty(MovieRatingsAppProperties.IMBD_NAME_BASICS_URL, "http://localhost:" + port + "/name.basics.tsv.gz");
        applicationProperties.setProperty(MovieRatingsAppProperties.IMBD_TITLE_EPISODE_URL, "http://localhost:" + port + "/title.episode.tsv.gz");
        applicationProperties.setProperty(MovieRatingsAppProperties.IMBD_TITLE_PRINCIPALS_URL, "http://localhost:" + port + "/title.principals.tsv.gz");
        applicationProperties.setProperty(MovieRatingsAppProperties.IMBD_TITLE_RATINGS_URL, "http://localhost:" + port + "/title.ratings.tsv.gz");
        createDirectoryIfNotExists(APPLICATION_PROPERTIES_PATH.getParent());
        applicationProperties.store(Files.newOutputStream(APPLICATION_PROPERTIES_PATH), "");
        System.setProperty(MovieRatingsAppProperties.SYSTEM_PROPERTIES_FILE, APPLICATION_PROPERTIES_PATH.toString());      
    }    
    
    @After
    public void cleanup() throws IOException {
        Files.deleteIfExists(APPLICATION_PROPERTIES_PATH);
    }  

    @AfterClass
    public static void cleanupIMDbDataServer() {
        mockServer.stop();
    }

    @Override
    protected Connection connection(final String path) throws IOException {
        Assert.fail("Please use connection(); this test only supports 1 database connection");
        return null;
    }

    @Override
    protected Connection connection() throws IOException {
        return H2Connection.instance(MovieRatingsAppProperties.databaseUserName(), MovieRatingsAppProperties.databaseUserPassword(), MovieRatingsAppProperties.databasePath().toString());
    }
    
    private static void createDirectoryIfNotExists(final Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createDirectory(path);
        }
    }

    @Override
    protected Path databasePath() {
        return UNIQUE_DB_PATH;
    }
    
    /**
     *
     * @param requestPath
     * @param responsePath
     * @throws IOException
     */
    private static void whenRequest(final String requestPath, final String responsePath) throws IOException {
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
