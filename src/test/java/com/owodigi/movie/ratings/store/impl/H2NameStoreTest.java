package com.owodigi.movie.ratings.store.impl;

import com.owodigi.movie.ratings.store.domain.NameRecord;
import com.owodigi.movie.ratings.store.NameStore;
import com.owodigi.util.AssertUtils;
import static com.owodigi.util.AssertUtils.newNameRecord;
import java.io.IOException;
import org.junit.Test;

public class H2NameStoreTest extends H2StoreTest {
       
    @Test
    public void addNconst() throws IOException {
        final NameStore store = new NameTable(connection());
        testAddNconst(newNameRecord("nm0000001"), store);
        testAddNconst(newNameRecord("nm0000002"), store);
    }    

    @Test
    public void updateName() throws IOException {
        final NameStore store = new NameTable(connection());
        final NameRecord originalRecord = newNameRecord("nm0000001"); 
        testAddNconst(originalRecord, store);
        originalRecord.setPrimaryName("Fred Astaire");
        store.updateName(originalRecord.nconst(), originalRecord.primaryName());
        final NameRecord updatedRecord = store.nconst(originalRecord.nconst());
        AssertUtils.assertEquals(originalRecord, updatedRecord);
    }

    private void testAddNconst(final NameRecord expected, final NameStore store) throws IOException {
        store.addNconst(expected.nconst());
        final NameRecord actual = store.nconst(expected.nconst());
        AssertUtils.assertEquals(expected, actual);
    }   
}
