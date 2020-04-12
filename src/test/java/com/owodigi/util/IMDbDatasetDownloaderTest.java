package com.owodigi.util;

import com.owodigi.util.IMDbTSVFormats.TitleBasicsFormat;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.csv.CSVRecord;
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
public class IMDbDatasetDownloaderTest {
    private MockServerClient mockServerClient;

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this);

    @Test
    public void downloadGzipTSV() throws IOException {
        mockServerClient
            .when(
                request()
            )
            .respond(
                response()
                    .withStatusCode(200)
                    .withBody(Files.readAllBytes(Paths.get("src/test/resources/title.basics.tsv.gz")))
            );
        final List<List<String>> expected = AssertUtils.asList(
            Arrays.asList("tt0000001", "short", 	"Carmencita", "Carmencita", "0", "1894", "\\N", "1", "Documentary,Short"),
            Arrays.asList("tt0000002", "short", 	"Le clown et ses chiens", "Le clown et ses chiens", "0", "1892", "\\N", "5", "Animation,Short"),
            Arrays.asList("tt0000003", "short", "Pauvre Pierrot", "Pauvre Pierrot", "0", "1892", "\\N", "4", "Animation,Comedy,Romance")
        );
        final Iterator<List<String>> expectedIterator = expected.iterator();
        final URL url = new URL("http://localhost:" + mockServerRule.getPort());
        IMDbDatasetDownloader.read(url, new TitleBasicsFormat(), new IMDbDownloaderCallback() {
            
            @Override
            public void read(final CSVRecord actual) {
                Assert.assertTrue("Unexpected record " + actual, expectedIterator.hasNext());
                AssertUtils.assertEquals(expectedIterator.next(), actual);
            }
        });
    }
}
