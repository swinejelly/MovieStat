package edu.rit.moviestat.model;

import java.time.LocalDate;
import java.time.Period;

/**
 * Model representing an actor who stars in movies.
 * @author Scott Jordan
 */
public class Actor {
    private String name;
    
    private LocalDate birthdate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
    
    /**
     * Age in years of the actor
     * @return Age in years or null if birthdate is not present.
     */
    public Integer getAge() {
        if (birthdate != null) {
            LocalDate now = LocalDate.now();
            
            Period age = Period.between(birthdate, now);
            
            return age.getYears();
        }
        
        return null;
    }

    public Actor(String name, LocalDate birthdate) {
        this.name = name;
        this.birthdate = birthdate;
    }
}
