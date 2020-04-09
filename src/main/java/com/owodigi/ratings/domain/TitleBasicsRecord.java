package com.owodigi.ratings.domain;

/**
 *
 */
public class TitleBasicsRecord {

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
     * (string) - original title; in the original language
     */
    private String originalTitle;
    
    /**
     * (boolean) - 0: non-adult title; 1: adult title
     */
    private String isAdult;
    
    /**
     * (YYYY) – represents the release year of a title. In the case of TV
     * Series; it is the series start year
     */
    private String startYear;
    
    /**
     * (YYYY) – TV Series end year. ‘\N’ for all other title types
     */
    private String endYear;
    
    /**
     * primary runtime of the title; in minutes
     */
    private String runtimeMinutes;
    
    /**
     * (string array) – includes up to three genres associated with the title
     */
    private String genres;

    
    public String tconst() {
        return tconst;
    }

    public String titleType() {
        return titleType;
    }

    public String primaryTitle() {
        return primaryTitle;
    }

    public String originalTitle() {
        return originalTitle;
    }

    public String isAdult() {
        return isAdult;
    }

    public String startYear() {
        return startYear;
    }

    public String endYear() {
        return endYear;
    }

    public String runtimeMinutes() {
        return runtimeMinutes;
    }

    public String genres() {
        return genres;
    }   
}
