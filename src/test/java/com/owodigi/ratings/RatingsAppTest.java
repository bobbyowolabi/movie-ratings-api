package com.owodigi.ratings;

import com.owodigi.ratings.domain.EpisodeRecord;
import com.owodigi.ratings.domain.TitleRecord;
import com.owodigi.ratings.store.EpisodeStore;
import com.owodigi.ratings.store.TitleStore;
import com.owodigi.ratings.store.impl.H2EpisodeStore;
import com.owodigi.ratings.store.impl.H2StoreTest;
import com.owodigi.ratings.store.impl.H2TitleStore;
import static com.owodigi.ratings.store.impl.util.ResultCallback.NO_OP_RESULT_CALLBACK;
import com.owodigi.ratintgs.util.RatingsAppProperties;
import com.owodigi.util.AssertUtils;
import static com.owodigi.util.AssertUtils.newEpisodeRecord;
import static com.owodigi.util.AssertUtils.newTitleRecord;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 *
 */
public class RatingsAppTest extends H2StoreTest {
    private MockServerClient mockServerClient;
    private final Path applicationPropertiesPath = Paths.get("./target/test-data/ratings-app.properties"); 

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this);

    @After
    public void cleanup() throws IOException {
        Files.deleteIfExists(applicationPropertiesPath);
    }
    
    /**
     * 
     */
    private void runAndTestRatingsApp() throws IOException {
        RatingsApp.main(new String[0]);
        testGetRating();
        testGetRatingOfEpisode();        
    }
    
    
    private void setApplicationProperties() throws IOException {
        final Properties applicationProperties = new Properties();
        applicationProperties.load(Files.newInputStream(Paths.get("src/test/resources/ratings-app.properties")));
        applicationProperties.setProperty(RatingsAppProperties.DATABASE_PATH, databasePath().toString());
        Files.createDirectory(applicationPropertiesPath.getParent());
        applicationProperties.store(Files.newOutputStream(applicationPropertiesPath), "");
        System.setProperty(RatingsAppProperties.SYSTEM_PROPERTIES_FILE, applicationPropertiesPath.toString());
    }
    
    @Test
    public void testRatingsApp() throws IOException {
        mockServerClient.bind(8080);
        whenRequest("/title.basics.tsv.gz", "src/test/resources/title.basics.tsv.gz");
        whenRequest("/title.ratings.tsv.gz", "src/test/resources/title.ratings.tsv.gz");
        whenRequest("/title.principals.tsv.gz", "src/test/resources/title.principals.tsv.gz");
        whenRequest("/title.episode.tsv.gz", "src/test/resources/title.episode.tsv.gz");
        setApplicationProperties();
        runAndTestRatingsApp();
        runAndTestRatingsApp();
        verifyNoDuplicateRows();
    }

    private void testGetRating() throws IOException {
        final TitleStore store = new H2TitleStore(
            RatingsAppProperties.databaseUserName(),
            RatingsAppProperties.databaseUserPassword(),
            RatingsAppProperties.databasePath());
        final TitleRecord expected = newTitleRecord (
            "6.5",
            Arrays.asList("nm0721526", "nm5442194", "nm1335271", "nm5442200"),
            "Pauvre Pierrot",
            "tt0000003",
            "short");
        final TitleRecord actual = store.title("Pauvre Pierrot");
        AssertUtils.assertEquals(expected, actual);
    }

    public void testGetRatingOfEpisode() throws IOException {
        final EpisodeStore store = new H2EpisodeStore(
            RatingsAppProperties.databaseUserName(),
            RatingsAppProperties.databaseUserPassword(),
            RatingsAppProperties.databasePath());
        final EpisodeRecord expected = newEpisodeRecord(
            "tt0041951",
            "tt0041038",
            "The Tenderfeet",
            "7.4",
            "1",
            "9",
            Arrays.asList("nm0156134","nm0138194","nm0798855","nm0071986","nm0112203","nm0782690","nm0872077","nm0289014","nm1080563","nm0834503")
        );
        final EpisodeRecord actual = store.title("The Tenderfeet");
        AssertUtils.assertEquals(expected, actual);
    }

    private void verifyNoDuplicateRows() throws IOException {
        final TestH2TitleStore store = new TestH2TitleStore(
            RatingsAppProperties.databaseUserName(),
            RatingsAppProperties.databaseUserPassword(),
            RatingsAppProperties.databasePath());
        final String title = "Pauvre Pierrot";
        final int expected = 1;
        final int actual = store.titles(title);
        Assert.assertEquals("Number of rows in TitleStore with Title " + title, expected, actual);
    }

    private class TestH2TitleStore extends H2TitleStore {

        public TestH2TitleStore(final String username, final String password, final Path databasePath) throws IOException {
            super(username, password, databasePath);
        }

        public int titles(final String primaryTitle) throws IOException {
            final String sql = selectAllSql(H2TitleStore.columns.primaryTitle.name(), primaryTitle);
            return executeQuery(sql, NO_OP_RESULT_CALLBACK);
        }
    }    
    
    /**
     *
     * @param requestPath
     * @param responsePath
     * @throws IOException
     */
    private void whenRequest(final String requestPath, final String responsePath) throws IOException {
        mockServerClient
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
