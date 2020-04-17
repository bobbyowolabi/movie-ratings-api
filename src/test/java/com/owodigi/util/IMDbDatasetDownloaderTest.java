package com.owodigi.util;

import com.owodigi.util.IMDbTSVFormats.NameBasicFormat;
import com.owodigi.util.IMDbTSVFormats.TitleBasicsFormat;
import com.owodigi.util.IMDbTSVFormats.TitleEpisodeFormat;
import com.owodigi.util.IMDbTSVFormats.TitleRatingsFormat;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.csv.CSVRecord;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class IMDbDatasetDownloaderTest {
    private static ClientAndServer mockServer;
    private static int port;

    static {
        System.setProperty("mockserver.logLevel", "OFF");
    }    
    
    @BeforeClass
    public static void setupTests() throws IOException {
        mockServer = startClientAndServer(0);
        port = mockServer.getLocalPort();
    }
    
    @AfterClass
    public static void cleanupTests() {
        mockServer.stop();
    }    
    
    @Test
    public void downloadTitleBasics() throws IOException {
        final String fileName = "title.basics.tsv.gz";
        final List<List<String>> expected = AssertUtils.asList(
            Arrays.asList("tt0000001", "short", 	"Carmencita", "Carmencita", "0", "1894", "\\N", "1", "Documentary,Short"),
            Arrays.asList("tt0000002", "short", 	"Le clown et ses chiens", "Le clown et ses chiens", "0", "1892", "\\N", "5", "Animation,Short"),
            Arrays.asList("tt0000003", "short", "Pauvre Pierrot", "Pauvre Pierrot", "0", "1892", "\\N", "4", "Animation,Comedy,Romance"),
            Arrays.asList("tt0041038", "tvSeries", "The Lone Ranger", "The Lone Ranger", "0", "1949", "1957", "30", "Western"),
            Arrays.asList("tt0989125", "tvSeries", "BBC Sunday-Night Theatre", "BBC Sunday-Night Theatre", "0", "1950", "1959", "\\N", "Drama"),
            Arrays.asList("tt0041951", "tvEpisode", "The Tenderfeet", "The Tenderfeet", "0", "1949", "\\N", "30", "Western"),
            Arrays.asList("tt0042816", "tvEpisode", "Othello", "Othello", "0", "1950", "\\N", "135", "Drama")
        );
        testDownload(fileName, new TitleBasicsFormat(), expected);
    }

    @Test
    public void downloadTitleRatings() throws IOException {
        final String fileName = "title.ratings.tsv.gz";
        final List<List<String>> expected = AssertUtils.asList(
            Arrays.asList("tt0000001", "5.6", "1593"),
            Arrays.asList("tt0000002", "6.0", "195"),
            Arrays.asList("tt0000003", "6.5", "1266"),
            Arrays.asList("tt0041038", "7.8", "1918"),
            Arrays.asList("tt0989125", "8.0", "64"),
            Arrays.asList("tt0041951", "7.4", "47")
        );
        testDownload(fileName, new TitleRatingsFormat(), expected);
    }
    
    @Test
    public void downloadTitleEpisode() throws IOException {
        final String fileName = "title.episode.tsv.gz";
        final List<List<String>> expected = AssertUtils.asList(
            Arrays.asList("tt0041951", "tt0041038", "1", "9"),
            Arrays.asList("tt0042816", "tt0989125", "1", "17")
        );
        testDownload(fileName, new TitleEpisodeFormat(), expected);
    }

    @Test
    public void downloadNameBasics() throws IOException {
        final String fileName = "name.basics.tsv.gz";
        final List<List<String>> expected = AssertUtils.asList(
            Arrays.asList("nm0000001", "Fred Astaire", "1899", "1987", "soundtrack,actor,miscellaneous", "tt0053137,tt0050419,tt0072308,tt0043044"),
            Arrays.asList("nm0000002", "Lauren Bacall", "1924", "2014", "actress,soundtrack", "tt0071877,tt0038355,tt0037382,tt0117057"),
            Arrays.asList("nm0000003", "Brigitte Bardot", "1934", "\\N", "actress,soundtrack,producer", "tt0049189,tt0057345,tt0054452,tt0059956"),
            Arrays.asList("nm1588970", "Carmencita", "1868", "1910", "soundtrack", "tt0057728,tt0000001"),
            Arrays.asList("nm0005690", "William K.L. Dickson", "1860", "1935", "cinematographer,director,producer", "tt6687694,tt1428455,tt1496763,tt0219560"),
            Arrays.asList("nm0374658", "William Heise", "1847", "1910", "cinematographer,director,producer", "tt0241715,tt0285863,tt0241393,tt0229665"),
            Arrays.asList("nm0721526", "Émile Reynaud", "1844", "1918", "director", "tt2184201,tt0413219,tt2184231,tt0000003"),
            Arrays.asList("nm5442194", "Julien Pappé", "\\N", "2005", "producer", "tt0000003"),
            Arrays.asList("nm1335271", "Gaston Paulin", "1839", "1903", "composer", "tt2184231,tt0000004,tt0000003,tt0000002"),
            Arrays.asList("nm5442200", "Tamara Pappé", "\\N", "\\N", "editor", "tt0000003"),
            Arrays.asList("nm0156134", "Jack Chertok", "1906", "1995", "producer,miscellaneous", "tt0036218,tt0036719,tt0038990,tt0056775"),
            Arrays.asList("nm0138194", "Clayton Moore", "1914", "1999", "actor,soundtrack", "tt0040381,tt0048310,tt0051876,tt0041038"),
            Arrays.asList("nm0798855", "Jay Silverheels", "1912", "1980", "actor", "tt0041038,tt0048310,tt0066221,tt0051876"),
            Arrays.asList("nm0071986", "Ray Bennett", "1895", "1957", "actor", "tt0030382,tt0035786,tt0030126,tt0041918"),
            Arrays.asList("nm0112203", "Rand Brooks", "1918", "2003", "actor,miscellaneous,director", "tt0031381,tt0030079,tt0039213,tt0436502"),
            Arrays.asList("nm0782690", "George B. Seitz Jr.", "1915", "2002", "director,writer", "tt0790703,tt0242593,tt3631494,tt0041038"),
            Arrays.asList("nm0872077", "George W. Trendle", "1884", "1972", "writer,producer", "tt0827709,tt0182633,tt0990407,tt0041038"),
            Arrays.asList("nm0289014", "Gibson Fox", "\\N", "\\N", "writer", "tt0052472,tt0041038,tt0052441"),
            Arrays.asList("nm1080563", "Polly James", "\\N", "\\N", "writer", "tt0045077,tt0037096,tt0052103,tt0045067"),
            Arrays.asList("nm0834503", "Fran Striker", "1903", "1962", "writer,miscellaneous", "tt0032559,tt0041038,tt0031586,tt0031394")
        );
        testDownload(fileName, new NameBasicFormat(), expected);
    }    

    /**
     *
     * @param fileName
     * @param expected
     * @throws IOException
     */
    private <E extends TSVFormat> void testDownload(final String fileName, final E tsvFormat, final List<List<String>> expected) throws IOException {
        mockServer
            .when(
                request("/" + fileName)
            )
            .respond(
                response()
                    .withStatusCode(200)
                    .withBody(Files.readAllBytes(Paths.get("src/test/resources/" + fileName)))
            );
        final Iterator<List<String>> expectedIterator = expected.iterator();
        final URL url = new URL("http://localhost:" + port + "/" + fileName);
        IMDbDatasetDownloader.read(url, tsvFormat, new IMDbDownloaderCallback() {

            @Override
            public void read(final CSVRecord actual) {
                Assert.assertTrue("Unexpected record " + actual, expectedIterator.hasNext());
                AssertUtils.assertEquals(expectedIterator.next(), actual);
            }
        });
    }
}
