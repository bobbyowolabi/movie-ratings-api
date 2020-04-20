package com.owodigi.movie.ratings.store.domain;

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
    
    /**
     * alphanumeric identifier of the parent TV Series
     * 
     * @return 
     */
    public String parentConst() {
        return parentTconst;
    }

    /**
     * alphanumeric identifier of episode
     * 
     * @return 
     */
    public String tconst() {
        return tconst;
    }

    /**
     * season number the episode belongs to
     * 
     * @return 
     */
    public String seasonNumber() {
        return seasonNumber;
    }

    /**
     * episode number of the tconst in the TV series
     * 
     * @return 
     */
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
