package edu.rit.moviestat.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Model representing an actor who stars in movies.
 * @author Scott Jordan
 */
@Entity
public class Actor {
    @Id
    private String name;
    
    @Temporal(TemporalType.DATE)
    private Calendar birthdate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Calendar birthdate) {
        this.birthdate = birthdate;
    }
    
    /**
     * Age in years of the actor
     * @return Age in years or null if birthdate is not present.
     */
    public Integer getAge() {
        if (birthdate != null) {
            Calendar now = Calendar.getInstance();
            
            int years = now.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);
            
            boolean sameMonth = birthdate.get(Calendar.MONTH) == now.get(Calendar.MONTH);
            boolean greaterMonth = birthdate.get(Calendar.MONTH) < now.get(Calendar.MONTH);
            
            if (greaterMonth) {
                years++;
            } else if (sameMonth) {
                boolean greaterEqualDay = birthdate.get(Calendar.DAY_OF_MONTH) <= now.get(Calendar.DAY_OF_MONTH);
                
                if (greaterEqualDay) {
                    years++;
                }
            }
            
            return years;
        }
        
        return null;
    }

    public Actor(String name, Calendar birthdate) {
        this.name = name;
        this.birthdate = birthdate;
    }
    
    protected Actor() {}
}
