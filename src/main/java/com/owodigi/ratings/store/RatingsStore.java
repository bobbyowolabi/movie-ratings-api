package com.owodigi.ratings.store;

import com.owodigi.ratings.domain.Rating;

/**
 *
 */
public interface RatingsStore {

    /**
     * 
     * @param title
     * @return 
     */
    public Rating rating(final String title);
}
