package com.owodigi.movie.ratings.api.domain;

public class Episode {
    private String title;
    private String userRating;
    private String seasonNumber;
    private String episodeNumber;
    private String castList;

    public String getTitle() {
        return title;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getSeasonNumber() {
        return seasonNumber;
    }

    public String getEpisodeNumber() {
        return episodeNumber;
    }

    public String getCastList() {
        return castList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public void setSeasonNumber(String seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public void setEpisodeNumber(String episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public void setCastList(String castList) {
        this.castList = castList;
    }
}
