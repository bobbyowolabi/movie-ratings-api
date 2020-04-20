package com.owodigi.movie.ratings.store.domain;

/**
 *
 */
public class RatingRecord {
    /**
     * (string) - alphanumeric unique identifier of the title
     */
    private String tconst;
    
    /**
     * weighted average of all the individual user ratings
     */
    private String averageRating;

    public String getTconst() {
        return tconst;
    }

    public String averageRating() {
        return averageRating;
    }

    public void setTconst(String tconst) {
        this.tconst = tconst;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }
}
