package edu.rit.moviestat.info;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.rit.moviestat.exception.MovieInformationUnavailableException;
import edu.rit.moviestat.model.MovieSelection;
import edu.rit.moviestat.model.MovieSelectionRepository;

/**
 * MovieSelectionDataSource decorator that creates/uses a cache from the results of another
 * decorated MovieSelectionDataSource. 
 * @author Scott Jordan
 */
public class CachingMovieSelectionDataSourceDecorator implements MovieSelectionDataSource {
    
    private MovieSelectionDataSource decoratee;
    
    private LocalDateTime lastUpdate = null;
    
    @Autowired
    private MovieSelectionRepository repository;
    
    public CachingMovieSelectionDataSourceDecorator(MovieSelectionDataSource decoratee) {
        this.decoratee = decoratee;
    }

    @Override
    public List<MovieSelection> getSelectedMovies() throws MovieInformationUnavailableException {
        if (shouldTryCache()) {
            Iterable<MovieSelection> movieSelections = repository.findAll();
            
            List<MovieSelection> movieIds = new ArrayList<>();
            
            for (MovieSelection movieSelection: movieSelections) {
                movieIds.add(movieSelection);
            }
            
            return movieIds;
        }
        
        List<MovieSelection> movieSelections = decoratee.getSelectedMovies();
        
        repository.deleteAll();
        
        repository.save(movieSelections);
        
        lastUpdate = LocalDateTime.now();
        
        return decoratee.getSelectedMovies();
    }
    
    private boolean shouldTryCache() {
        return lastUpdate != null && lastUpdate.isAfter(LocalDateTime.now().minusHours(1));
    }

}
