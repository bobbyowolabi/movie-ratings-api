package com.owodigi.movie.ratings.store.impl;

import com.owodigi.movie.ratings.store.domain.TitleRecord;
import com.owodigi.movie.ratings.store.TitleStore;
import com.owodigi.util.AssertUtils;
import static com.owodigi.util.AssertUtils.newTitleRecord;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class TitleTableTest extends H2StoreTest {
       
    @Test
    public void addTitle() throws IOException {
        final TitleStore store = new TitleTable(connection());
        testAdd(newTitleRecord("tt0000001", "short", "Animation Film Title"), store);
        testAdd(newTitleRecord("tt0000002", "short2", "Animation Film Title2"), store);
    }

    private void testAdd(final TitleRecord expected, final TitleStore store) throws IOException {
        store.addTitle(expected.tconst(), expected.titleType(), expected.primaryTitle());
        TitleRecord actual = store.title(expected.primaryTitle());
        AssertUtils.assertEquals(expected, actual);
        actual = store.tconst(expected.tconst());
        AssertUtils.assertEquals(expected, actual);
    }
}
