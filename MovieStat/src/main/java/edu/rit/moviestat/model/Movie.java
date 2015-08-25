package edu.rit.moviestat.model;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Model representing a movie and its cast.
 * @author Scott Jordan
 */
@Entity
public class Movie {
    
    @Id
    private String imdbId;
    
    private String title;
    
    @Temporal(TemporalType.DATE)
    private Calendar releaseDate;
    
    @ManyToMany
    private List<Actor> cast;

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Calendar getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Calendar releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Actor> getCast() {
        return cast;
    }

    public void setCast(List<Actor> cast) {
        this.cast = cast;
    }
    
    /**
     * Find the average age of the cast, only considering those who have an age present. 
     * @return The average age, or null if no actors have ages.
     */
    public Double getAverageAgeCast() {
        int numActorsWithAge = 0;
        double sum = 0;
        
        for (Actor actor: cast) {
            if (actor.getAge() != null) {
                numActorsWithAge++;
                
                sum += actor.getAge();
            }
        }
        
        if (numActorsWithAge >= 0 && sum > 0) {
            return sum / numActorsWithAge;
        }
        
        return null;
    }

    public Movie(String imdbId, String title, Calendar releaseDate, List<Actor> cast) {
        this.imdbId = imdbId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.cast = cast;
    }
    
    protected Movie() {}
}
