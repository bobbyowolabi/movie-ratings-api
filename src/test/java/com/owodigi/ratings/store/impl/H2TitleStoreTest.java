package com.owodigi.ratings.store.impl;

import com.owodigi.ratings.domain.TitleRecord;
import com.owodigi.ratings.store.TitleStore;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class H2TitleStoreTest {

    @Test
    public void add() {
        final TitleStore store = null; //new H2TitleStore();
        final String expectedTconst = "tt0000001";
        final String expectedTitleType = "short";
        final String expectedPrimaryTitle = "Animation Film Title";
        store.add(expectedTconst, expectedTitleType, expectedPrimaryTitle);
        final TitleRecord record = store.title("Animation Film Title");
        Assert.assertEquals("averageRating", null, record.averageRating());
        Assert.assertEquals("nConstList", null, record.nConstList());
        Assert.assertEquals("primaryTitle", expectedPrimaryTitle, record.primaryTitle());
        Assert.assertEquals("tconst", expectedTconst, record.tconst());
        Assert.assertEquals("titleType", expectedTitleType, record.titleType());
    }
}
