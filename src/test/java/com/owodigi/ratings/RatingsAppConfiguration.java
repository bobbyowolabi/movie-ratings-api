package com.owodigi.ratings;

import com.owodigi.ratings.store.impl.H2StoreTest;
import com.owodigi.ratintgs.util.RatingsAppProperties;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockserver.integration.ClientAndServer;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 *
 */
public abstract class RatingsAppConfiguration extends H2StoreTest {
    private static ClientAndServer mockServer;
    private final Path applicationPropertiesPath = Paths.get("./target/test-data/ratings-app.properties");
    
    @BeforeClass
    public static void setupIMDbDatasetServer() throws IOException {
        mockServer = startClientAndServer(8080);
        whenRequest("/title.basics.tsv.gz", "src/test/resources/title.basics.tsv.gz");
        whenRequest("/title.ratings.tsv.gz", "src/test/resources/title.ratings.tsv.gz");
        whenRequest("/title.principals.tsv.gz", "src/test/resources/title.principals.tsv.gz");
        whenRequest("/title.episode.tsv.gz", "src/test/resources/title.episode.tsv.gz");
        whenRequest("/name.basics.tsv.gz", "src/test/resources/name.basics.tsv.gz");
    }

    @Before
    public void setApplicationProperties() throws IOException {
        Files.deleteIfExists(applicationPropertiesPath);
        final Properties applicationProperties = new Properties();
        applicationProperties.load(Files.newInputStream(Paths.get("src/test/resources/ratings-app.properties")));
        applicationProperties.setProperty(RatingsAppProperties.DATABASE_PATH, databasePath().toString());
        createDirectoryIfNotExists(applicationPropertiesPath.getParent());
        applicationProperties.store(Files.newOutputStream(applicationPropertiesPath), "");
        System.setProperty(RatingsAppProperties.SYSTEM_PROPERTIES_FILE, applicationPropertiesPath.toString());
    }    
    
    @After
    public void cleanup() throws IOException {
        Files.deleteIfExists(applicationPropertiesPath);
    }

    @AfterClass
    public static void cleanupIMDbDataServer() {
        mockServer.stop();
    }
    
    private void createDirectoryIfNotExists(final Path path) throws IOException {
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
        mockServer
            .when(
                request()
                    .withPath(requestPath)
            )
            .respond(
                response()
                    .withStatusCode(200)
                    .withBody(Files.readAllBytes(Paths.get(responsePath)))
            );
    }    
}
