package com.owodigi.movie.ratings.store.impl;

import com.owodigi.movie.ratings.store.domain.EpisodeRecord;
import com.owodigi.movie.ratings.store.EpisodeStore;
import com.owodigi.util.AssertUtils;
import static com.owodigi.util.AssertUtils.newEpisodeRecord;
import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class EpisodeTableTest extends H2StoreTest {
       
    @Test
    public void addTitle() throws IOException {
        final EpisodeStore store = new EpisodeTable(connection());
        testAddTitle(newEpisodeRecord("tt0000009", "tt0000001", "1", "10"), store);
        testAddTitle(newEpisodeRecord("tt0000008", "tt0000002", "2", "7"), store);
    }
    
    @Test
    public void parentTconst() throws IOException {
        final EpisodeStore store = new EpisodeTable(connection());
        final EpisodeRecord expected = new EpisodeRecord();
        expected.setEpisodeNumber("1");
        expected.setParentConst("tt002");
        expected.setSeasonNumber("5");
        expected.setTconst("TT0010");
        store.add(expected.tconst(), expected.parentConst(), expected.seasonNumber(), expected.episodeNumber());
        final List<EpisodeRecord> actualRecords = store.parentTconst(expected.parentConst());
        Assert.assertEquals(1, actualRecords.size());
        AssertUtils.assertEquals(expected, actualRecords.iterator().next());
    }
    
    private void testAddTitle(final EpisodeRecord expected, final EpisodeStore store) throws IOException {
        store.add(expected.tconst(), expected.parentConst(), expected.seasonNumber(), expected.episodeNumber());
        final EpisodeRecord actual = store.tconst(expected.tconst());
        AssertUtils.assertEquals(expected, actual);
    }
}
