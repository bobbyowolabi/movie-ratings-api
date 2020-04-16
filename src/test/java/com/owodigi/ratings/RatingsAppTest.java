package com.owodigi.ratings;

import com.owodigi.ratings.domain.EpisodeRecord;
import com.owodigi.ratings.domain.NameRecord;
import com.owodigi.ratings.domain.TitleRecord;
import com.owodigi.ratings.store.EpisodeStore;
import com.owodigi.ratings.store.NameStore;
import com.owodigi.ratings.store.TitleStore;
import com.owodigi.ratings.store.impl.H2EpisodeStore;
import com.owodigi.ratings.store.impl.H2NameStore;
import com.owodigi.ratings.store.impl.H2TitleStore;
import static com.owodigi.ratings.store.impl.util.ResultCallback.NO_OP_RESULT_CALLBACK;
import com.owodigi.ratintgs.util.RatingsAppProperties;
import com.owodigi.util.AssertUtils;
import static com.owodigi.util.AssertUtils.newEpisodeRecord;
import static com.owodigi.util.AssertUtils.newNameRecord;
import static com.owodigi.util.AssertUtils.newTitleRecord;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class RatingsAppTest extends RatingsAppConfiguration {

    /**
     *
     */
    private void runAndTestRatingsApp() throws IOException {
        RatingsApp.main(new String[0]);
        testGetTitle();
        testGetEpisode();
        testGetName();
    }


    @Test
    public void testRatingsApp() throws IOException {
        runAndTestRatingsApp();
        runAndTestRatingsApp();
        verifyNoDuplicateRows();
    }

    private void testGetTitle() throws IOException {
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

    public void testGetEpisode() throws IOException {
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
    
    private void testGetName() throws IOException {
        final NameStore store = new H2NameStore(
            RatingsAppProperties.databaseUserName(),
            RatingsAppProperties.databaseUserPassword(),
            RatingsAppProperties.databasePath());
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
    
    private void verifyNoDuplicateEpisodeRows() throws IOException {
        final TestH2EpisodeStore store = new TestH2EpisodeStore(
            RatingsAppProperties.databaseUserName(),
            RatingsAppProperties.databaseUserPassword(),
            RatingsAppProperties.databasePath());
        final String tconst = "tt0041951";
        final int expected = 1;
        final int actual = store.tconsts(tconst);
        Assert.assertEquals("Number of rows in EpisdoeStore with tconst " + tconst, expected, actual);        
    }    

    private class TestH2EpisodeStore extends H2EpisodeStore {

        public TestH2EpisodeStore(final String username, final String password, final Path databasePath) throws IOException {
            super(username, password, databasePath);
        }

        public int tconsts(final String tconst) throws IOException {
            final String sql = selectAllSql(H2EpisodeStore.columns.tconst.name(), tconst);
            return executeQuery(sql, NO_OP_RESULT_CALLBACK);
        }
    }
    
    private void verifyNoDuplicateNameRows() throws IOException {
        final TestH2NameStore store = new TestH2NameStore(
            RatingsAppProperties.databaseUserName(),
            RatingsAppProperties.databaseUserPassword(),
            RatingsAppProperties.databasePath());
        final String nconst = "nm0005690";
        final int expected = 1;
        final int actual = store.nconsts(nconst);
        Assert.assertEquals("Number of rows in NameStore with nconst " + nconst, expected, actual);        
    }     

    private class TestH2NameStore extends H2NameStore {

        public TestH2NameStore(final String username, final String password, final Path databasePath) throws IOException {
            super(username, password, databasePath);
        }

        public int nconsts(final String nconst) throws IOException {
            final String sql = selectAllSql(H2NameStore.columns.nconst.name(), nconst);
            return executeQuery(sql, NO_OP_RESULT_CALLBACK);
        }
    }    
}
