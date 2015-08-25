package edu.rit.moviestat.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Database model representing a selected movie.
 * @author Scott Jordan
 */
@Entity
public class MovieSelection {
    
    @Id
    private String imdbId;
    
    public MovieSelection(String imdbId) {
        this.imdbId = imdbId;
    }

    protected MovieSelection() {}

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }
}
