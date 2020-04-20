package com.owodigi.movie.ratings.store.domain;

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
     * (integer) – season number the episode belongs to
     */
    private String seasonNumber;
    
    /**
     * (integer) – episode number of the tconst in the TV series
     */
    private String episodeNumber;
    

    public String parentConst() {
        return parentTconst;
    }

    public String tconst() {
        return tconst;
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

    public void setSeasonNumber(String seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public void setEpisodeNumber(String episodeNumber) {
        this.episodeNumber = episodeNumber;
    }
}
