package com.owodigi.movie.ratings.store.domain;

public class RatingRecord {
    /**
     * (string) - alphanumeric unique identifier of the title
     */
    private String tconst;
    
    /**
     * weighted average of all the individual user ratings
     */
    private String averageRating;

    /**
     * alphanumeric unique identifier of the title
     * 
     * @return 
     */
    public String getTconst() {
        return tconst;
    }

    /**
     * weighted average of all the individual user ratings
     * 
     * @return 
     */
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
