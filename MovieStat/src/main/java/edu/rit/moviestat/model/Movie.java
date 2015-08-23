package edu.rit.moviestat.model;

import java.time.LocalDate;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Model representing a movie and its cast.
 * @author Scott Jordan
 */
public class Movie {
    private String title;
    
    private LocalDate releaseDate;
    
    private List<Actor> cast;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Actor> getCast() {
        return cast;
    }

    public void setCast(List<Actor> cast) {
        this.cast = cast;
    }
    
    public Double getAverageAgeCast() {
        OptionalDouble averageAge = cast.stream().filter(actor -> actor.getAge() != null).mapToInt(actor -> actor.getAge()).average();
        
        if (averageAge.isPresent()) {
            return averageAge.getAsDouble();
        }
        
        return null;
    }

    public Movie(String title, LocalDate releaseDate, List<Actor> cast) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.cast = cast;
    }
}
