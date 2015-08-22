package edu.rit.moviestat.model;

import java.util.Date;
import java.util.List;

public class Movie {
    private String title;
    
    private Date releaseDate;
    
    private List<Actor> cast;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Actor> getCast() {
        return cast;
    }

    public void setCast(List<Actor> cast) {
        this.cast = cast;
    }
    

    public Movie(String title, Date releaseDate, List<Actor> cast) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.cast = cast;
    }
}
