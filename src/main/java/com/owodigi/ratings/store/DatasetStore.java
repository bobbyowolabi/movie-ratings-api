package com.owodigi.ratings.store;

import java.io.IOException;

/**
 *
 */
public interface DatasetStore {

    /**
     * Clears all the contents of this Store.
     * @throws java.io.IOException
     */
    public void clear() throws IOException;
}