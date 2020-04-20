package com.owodigi.movie.ratings.store.domain;

/**
 *
 */
public class NameRecord {
    /**
     * (string) - alphanumeric unique identifier of the name/person
     */
    private String nconst;
    
    /**
     *  (string)– name by which the person is most often credited
     */
    private String primaryName;

    /**
     * alphanumeric unique identifier of the name/person
     * 
     * @return 
     */
    public String nconst() {
        return nconst;
    }

    /**
     * name by which the person is most often credited
     * 
     * @return 
     */
    public String primaryName() {
        return primaryName;
    }

    public void setNconst(final String nconst) {
        this.nconst = nconst;
    }

    public void setPrimaryName(final String primaryName) {
        this.primaryName = primaryName;
    }
}
