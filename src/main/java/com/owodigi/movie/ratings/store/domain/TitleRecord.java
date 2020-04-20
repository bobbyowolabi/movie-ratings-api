package com.owodigi.movie.ratings.store.domain;

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
     * alphanumeric unique identifier of the title.
     * 
     * @return 
     */
    public String tconst() {
        return tconst;
    }

    /**
     * the type/format of the title
     * 
     * @return 
     */
    public String titleType() {
        return titleType;
    }

    /**
     * The more popular title / the title used by the filmmakers on
     * promotional materials at the point of release.
     * 
     * @return 
     */
    public String primaryTitle() {
        return primaryTitle;
    }

    public void setTconst(String tconst) {
        this.tconst = tconst;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }

    public void setPrimaryTitle(String primaryTitle) {
        this.primaryTitle = primaryTitle;
    }
}
