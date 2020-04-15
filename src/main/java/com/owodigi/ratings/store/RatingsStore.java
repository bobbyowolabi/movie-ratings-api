package com.owodigi.ratings.store;

import com.owodigi.ratings.domain.RatingRecord;

/**
 *
 */
public interface RatingsStore {

    /**
     * 
     * @param title
     * @return 
     */
    public RatingRecord rating(final String title);
}
