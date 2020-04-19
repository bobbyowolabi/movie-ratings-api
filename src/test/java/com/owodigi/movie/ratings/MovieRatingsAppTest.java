package com.owodigi.movie.ratings;

import com.owodigi.movie.ratings.util.MovieRatingsAppConfiguration;
import com.owodigi.movie.ratings.store.domain.EpisodeRecord;
import com.owodigi.movie.ratings.store.domain.NameRecord;
import com.owodigi.movie.ratings.store.domain.TitleRecord;
import com.owodigi.movie.ratings.store.EpisodeStore;
import com.owodigi.movie.ratings.store.NameStore;
import com.owodigi.movie.ratings.store.TitleStore;
import com.owodigi.movie.ratings.store.impl.EpisodeTable;
import com.owodigi.movie.ratings.store.impl.NameTable;
import com.owodigi.movie.ratings.store.impl.TitleTable;
import static com.owodigi.movie.ratings.store.impl.util.ResultCallback.NO_OP_RESULT_CALLBACK;
import com.owodigi.util.AssertUtils;
import static com.owodigi.util.AssertUtils.newEpisodeRecord;
import static com.owodigi.util.AssertUtils.newNameRecord;
import static com.owodigi.util.AssertUtils.newTitleRecord;
import java.io.IOException;
import java.sql.Connection;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class MovieRatingsAppTest extends MovieRatingsAppConfiguration {
    private static final String APP_URL = "http://localhost:8080/movie-ratings?title=";
    

    @Test
    public void testMovieRatingsAPI() throws Exception {
        try {
            testDatabase();
            testAPI();
        } finally {
            MovieRatingsApp.stop();
        }
    }

    public void testDatabase() throws Exception {
        MovieRatingsApp.main(new String[0]);
        testGetEpisode();
        testGetName();
        testGetTitle();
        MovieRatingsApp.stop();
        MovieRatingsApp.main(new String[0]);
        verifyNoDuplicateRows();
    }

    public void testAPI() throws IOException {
        queryNonTVShow();
        queryTvShow();
    }
    
    public void queryNonTVShow() throws IOException {
        final String title = "Carmencita";
        final String expected = 
            "{"
                + "\"title\":\"Carmencita\","
                + "\"type\":\"short\","
                + "\"userRating\":\"5.6\","
                + "\"calculatedRating\":null,"
                + "\"castList\":\"Carmencita, William K.L. Dickson, William Heise\","
                + "\"episodes\":[]"
          + "}";
        AssertUtils.assertQuery("Querying a Non TV Show", appURL(title), expected);
    }

    private String appURL(final String title) {
        return (APP_URL + title).replaceAll("\\s+", "%20");
    }

    public void queryTvShow() throws IOException {
        final String title = "The Lone Ranger";
        final String expected = 
            "{"
                + "\"title\":\"The Lone Ranger\","
                + "\"type\":\"tvSeries\","
                + "\"userRating\":\"7.8\","
                + "\"calculatedRating\":\"7.4\","
                + "\"castList\":\"John Cason, Jay Silverheels, Clayton Moore, John Hart, Chuck Courtney, George W. Trendle, George W. George, Lane Bradford, Mickey Simpson, House Peters Jr.\","
                + "\"episodes\":[{"
                + "\"title\":\"The Tenderfeet\","
                + "\"userRating\":\"7.4\","
                + "\"seasonNumber\":\"1\","
                + "\"episodeNumber\":\"9\","
                + "\"castList\":\"Jack Chertok, Clayton Moore, Jay Silverheels, Ray Bennett, Rand Brooks, George B. Seitz Jr., George W. Trendle, Gibson Fox, Polly James, Fran Striker\""
                + "}]"
          + "}";
        AssertUtils.assertQuery("Querying a TV Show", appURL(title), expected);
    }

    public void testGetTitle() throws IOException {
        final TitleStore store = new TitleTable(connection());
        final TitleRecord expected = newTitleRecord (
            "6.5",
            Arrays.asList("nm0721526", "nm5442194", "nm1335271", "nm5442200"),
            "Pauvre Pierrot",
            "tt0000003",
            "short");
        final TitleRecord actual = store.title("Pauvre Pierrot");
        AssertUtils.assertEquals(expected, actual);
    }

    public void testGetEpisode() throws IOException {
        final EpisodeStore store = new EpisodeTable(connection());
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

    public void testGetName() throws IOException {
        final NameStore store = new NameTable(connection());
        final NameRecord expected = newNameRecord(
            "nm0005690",
            "William K.L. Dickson"
        );
        final NameRecord actual = store.nconst(expected.nconst());
        AssertUtils.assertEquals(expected, actual);
    }

    private void verifyNoDuplicateRows() throws IOException {
        verifyNoDuplicateTitleRows();
        verifyNoDuplicateEpisodeRows();
        verifyNoDuplicateNameRows();
    }

    private void verifyNoDuplicateTitleRows() throws IOException {
        final TestH2TitleStore store = new TestH2TitleStore(connection());
        final String title = "Pauvre Pierrot";
        final int expected = 1;
        final int actual = store.titles(title);
        Assert.assertEquals("Number of rows in TitleStore with Title " + title, expected, actual);
    }

    private class TestH2TitleStore extends TitleTable {

        public TestH2TitleStore(final Connection connection) throws IOException {
            super(connection);
        }

        public int titles(final String primaryTitle) throws IOException {
            final String sql = selectAllSql(TitleTable.columns.primaryTitle.name(), primaryTitle);
            return executeQuery(sql, NO_OP_RESULT_CALLBACK);
        }
    }

    private void verifyNoDuplicateEpisodeRows() throws IOException {
        final TestH2EpisodeStore store = new TestH2EpisodeStore(connection());
        final String tconst = "tt0041951";
        final int expected = 1;
        final int actual = store.getCount(tconst);
        Assert.assertEquals("Number of rows in EpisdoeStore with tconst " + tconst, expected, actual);
    }

    private class TestH2EpisodeStore extends EpisodeTable {

        public TestH2EpisodeStore(final Connection connection) throws IOException {
            super(connection);
        }

        public int getCount(final String tconst) throws IOException {
            final String sql = selectAllSql(EpisodeTable.columns.tconst.name(), tconst);
            return executeQuery(sql, NO_OP_RESULT_CALLBACK);
        }
    }

    private void verifyNoDuplicateNameRows() throws IOException {
        final TestH2NameStore store = new TestH2NameStore(connection());
        final String nconst = "nm0005690";
        final int expected = 1;
        final int actual = store.nconsts(nconst);
        Assert.assertEquals("Number of rows in NameStore with nconst " + nconst, expected, actual);
    }

    private class TestH2NameStore extends NameTable {

        public TestH2NameStore(final Connection connection) throws IOException {
            super(connection);
        }

        public int nconsts(final String nconst) throws IOException {
            final String sql = selectAllSql(NameTable.columns.nconst.name(), nconst);
            return executeQuery(sql, NO_OP_RESULT_CALLBACK);
        }
    }
}
