package com.owodigi.ratings.domain;

import java.util.Set;

/**
 *
 */
public class Rating {
    private String title;
    private double rating;
    private Set<String> castList;

    public String title() {
        return title;
    }

    public double rating() {
        return rating;
    }

    public Set<String> castList() {
        return castList;
    }
}
