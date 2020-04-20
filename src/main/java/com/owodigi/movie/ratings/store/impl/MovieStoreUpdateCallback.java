package com.owodigi.movie.ratings.store.impl;

import com.owodigi.movie.ratings.store.MovieStoreUpdater;
import java.io.IOException;

/**
 *
 */
public interface MovieStoreUpdateCallback {

    public void update(final MovieStoreUpdater updater) throws IOException;
}
