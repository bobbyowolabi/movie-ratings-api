package com.owodigi.ratings;

import com.owodigi.ratings.domain.TitleRecord;
import com.owodigi.ratings.store.TitleStore;
import com.owodigi.ratings.store.impl.H2TitleStore;
import com.owodigi.ratintgs.RatingsApp;
import com.owodigi.ratintgs.util.RatingsAppProperties;
import com.owodigi.util.AssertUtils;
import static com.owodigi.util.AssertUtils.newTitleRecord;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 *
 */
public class RatingsAppTest {
    private MockServerClient mockServerClient;
    private static final Path TEST_DB_PATH = Paths.get("./target/test-data/test-h2.mv.db");
    private static final Path TEST_DB_TRACE_PATH = Paths.get("./target/test-data/test-h2.trace.db");

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this);

    @Before
    public void setupTest() throws IOException {
        Files.deleteIfExists(TEST_DB_PATH);
        Files.deleteIfExists(TEST_DB_TRACE_PATH);
    }
    
    @Test
    public void testGetRating() throws IOException {
        mockServerClient.bind(8080);
        mockServerClient
            .when(
                request()
                    .withPath("/title.basics.tsv.gz")
            )
            .respond(
                response()
                    .withStatusCode(200)
                    .withBody(Files.readAllBytes(Paths.get("src/test/resources/title.basics.tsv.gz")))
            );
        System.setProperty(RatingsAppProperties.SYSTEM_PROPERTIES_FILE, "src/test/resources/ratings-app.properties");
        RatingsApp.main(new String[0]); 
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
    
    @Test
    public void testGetRatingOfEpisode() {
        Assert.fail();
    }
    
    @Test
    public void testGetRatingOfTVShow() {
        Assert.fail();
    }
}
