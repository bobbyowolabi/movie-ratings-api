package com.owodigi.movie.ratings.api.domain;

public class MovieRecord {
    private String title;
    private String type;
    private String userRating;
    private String calculatedRating;
    private String castList;
    private Episode[] episodes;

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getCalculatedRating() {
        return calculatedRating;
    }

    public String getCastList() {
        return castList;
    }

    public Episode[] getEpisodes() {
        return episodes;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public void setCalculatedRating(String calculatedRating) {
        this.calculatedRating = calculatedRating;
    }

    public void setCastList(String castList) {
        this.castList = castList;
    }

    public void setEpisodes(Episode[] episodes) {
        this.episodes = episodes;
    }
}
