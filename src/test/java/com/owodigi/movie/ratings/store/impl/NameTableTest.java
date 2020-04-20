package com.owodigi.movie.ratings.store.impl;

import com.owodigi.movie.ratings.store.domain.NameRecord;
import com.owodigi.movie.ratings.store.NameStore;
import com.owodigi.util.AssertUtils;
import static com.owodigi.util.AssertUtils.newNameRecord;
import java.io.IOException;
import org.junit.Test;

public class NameTableTest extends H2StoreTest {
       
    @Test
    public void addNconst() throws IOException {
        final NameStore store = new NameTable(connection());
        testAddNconst(newNameRecord("nm0000001", "Name1"), store);
        testAddNconst(newNameRecord("nm0000002", "Name2"), store);
    }    

    private void testAddNconst(final NameRecord expected, final NameStore store) throws IOException {
        store.add(expected.nconst(), expected.primaryName());
        final NameRecord actual = store.nconst(expected.nconst());
        AssertUtils.assertEquals(expected, actual);
    }   
}
