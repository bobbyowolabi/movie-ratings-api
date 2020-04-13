package com.owodigi.ratings.domain;

import java.util.List;

/**
 *
 */
public class EpisodeRecord {
    /**
     * (string) - alphanumeric identifier of episode
     */
    private String tconst;    
    
    /**
     * (string) - alphanumeric identifier of the parent TV Series
     */
    private String parentTconst;
    
    /**
     * (string) – the more popular title / the title used by the filmmakers on promotional materials at the point of release
     */
    private String primaryTitle;  
    
    /**
     * weighted average of all the individual user ratings
     */
    private String averageRating;    
    
    /**
     * (integer) – season number the episode belongs to
     */
    private String seasonNumber;
    
    /**
     * (integer) – episode number of the tconst in the TV series
     */
    private String episodeNumber;
    
    /**
     * (string) - List of alphanumeric unique identifier of the name/person
     */
    private List<String> castList;
    
    public String parentConst() {
        return parentTconst;
    }

    public String tconst() {
        return tconst;
    }

    public String primaryTitle() {
        return primaryTitle;
    }

    public String averageRating() {
        return averageRating;
    }

    public List<String> castList() {
        return castList;
    }

    public String seasonNumber() {
        return seasonNumber;
    }

    public String episodeNumber() {
        return episodeNumber;
    }
    
    public void setParentConst(String parentConst) {
        this.parentTconst = parentConst;
    }

    public void setTconst(String tconst) {
        this.tconst = tconst;
    }

    public void setPrimaryTitle(String primaryTitle) {
        this.primaryTitle = primaryTitle;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public void setCastList(List<String> castList) {
        this.castList = castList;
    }

    public void setSeasonNumber(String seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public void setEpisodeNumber(String episodeNumber) {
        this.episodeNumber = episodeNumber;
    }
    
    
}
