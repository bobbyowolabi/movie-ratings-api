package com.owodigi.movie.ratings.store.util;

import com.owodigi.movie.ratings.store.util.MovieStoreUpdater;
import java.io.IOException;

/**
 *
 */
public interface MovieStoreUpdateCallback {

    public void update(final MovieStoreUpdater updater) throws IOException;
}
