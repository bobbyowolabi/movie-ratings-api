package com.owodigi.movie.ratings.store.impl;

import java.io.IOException;

public interface DatasetStore extends AutoCloseable {

    /**
     * Clears all the contents of this Store.
     * @throws java.io.IOException
     */
    public void clear() throws IOException;
}