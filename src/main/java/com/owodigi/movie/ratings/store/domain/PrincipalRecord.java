package com.owodigi.movie.ratings.store.domain;

public class PrincipalRecord {

    /**
     * (string) - alphanumeric unique identifier of the title
     */
    private String tconst;

    /**
     * (string) - alphanumeric unique identifier of the name/person

     */
    private String nconst;
    
    /**
     * (integer) – a number to uniquely identify rows for a given titleId
     */
    private String ordering;

    /**
     * alphanumeric unique identifier of the title
     * 
     * @return 
     */
    public String getTconst() {
        return tconst;
    }

    /**
     * alphanumeric unique identifier of the name/person.
     * 
     * @return 
     */
    public String getNconst() {
        return nconst;
    }

    /**
     * a number to uniquely identify rows for a given titleId.
     * 
     * @return 
     */
    public String getOrdering() {
        return ordering;
    }

    public void setTconst(String tconst) {
        this.tconst = tconst;
    }

    public void setNconst(String nconst) {
        this.nconst = nconst;
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }
}
