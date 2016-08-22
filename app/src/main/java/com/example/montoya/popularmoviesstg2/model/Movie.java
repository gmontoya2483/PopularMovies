package com.example.montoya.popularmoviesstg2.model;

/**
 * Created by montoya on 05.05.2016.
 */
public class Movie {
    private long id;
    private String title;
    private String imageThumbnail;
    private String sysnopsis;
    private String userRating;
    private String releaseDate;



    public Movie(long id, String title, String imageThumbnail, String sysnopsis, String userRating, String releaseDate) {
        this.id=id;
        this.title = title;
        this.imageThumbnail = imageThumbnail;
        this.sysnopsis = sysnopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public String getSysnopsis() {
        return sysnopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
