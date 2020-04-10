package com.owodigi.ratings.domain;

import java.util.List;

/**
 *
 */
public class TitleRecord {

    /**
     * (string) - alphanumeric unique identifier of the title
     */
    private String tconst;
    
    /**
     * (string) – the type/format of the title (e.g. movie; short; tvseries;
     * tvepisode; video; etc)
     */
    private String titleType;
    
    /**
     * (string) – the more popular title / the title used by the filmmakers on
     * promotional materials at the point of release
     */
    private String primaryTitle;
    
    /**
     * weighted average of all the individual user ratings
     */
    private String averageRating;
    
    /**
     * 
     */
    private List<String> nconstList;

    
    public String averageRating() {
        return averageRating;
    }
    
    public String tconst() {
        return tconst;
    }

    public String titleType() {
        return titleType;
    }

    public String primaryTitle() {
        return primaryTitle;
    }

    public List<String> nConstList() {
        return nconstList;
    }
}
