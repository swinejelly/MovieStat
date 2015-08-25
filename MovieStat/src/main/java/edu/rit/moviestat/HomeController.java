package edu.rit.moviestat;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import edu.rit.moviestat.exception.MovieInformationUnavailableException;
import edu.rit.moviestat.info.MovieDataSource;
import edu.rit.moviestat.info.MovieSelectionDataSource;
import edu.rit.moviestat.model.Movie;
import edu.rit.moviestat.model.MovieSelection;

/**
 * Main controller of the MovieStat application
 * @author Scott Jordan
 */
@Controller
public class HomeController {
    
    @Autowired
    private MovieSelectionDataSource movieSelection;
    
    @Autowired
    private MovieDataSource movieDataSource;
    
    @RequestMapping("/")
    public ModelAndView index() throws MovieInformationUnavailableException {
        ModelAndView modelView = new ModelAndView("index");
        
        List<MovieSelection> movieIds = movieSelection.getSelectedMovies();
        
        List<Movie> movies = movieDataSource.getMovies(movieIds);
        
        modelView.addObject("movies", movies);
        
        return modelView;
    }
    
    /**
     * Runs after initialization to "warm up" the caches.
     * @throws MovieInformationUnavailableException if MovieInformation is unavailable
     */
    @PostConstruct
    private void warmUp() throws MovieInformationUnavailableException {
        index();
    }
}
