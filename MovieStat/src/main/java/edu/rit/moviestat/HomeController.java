package edu.rit.moviestat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import edu.rit.moviestat.exception.MovieInformationUnavailableException;
import edu.rit.moviestat.info.MovieDataSource;
import edu.rit.moviestat.info.MovieSelection;
import edu.rit.moviestat.model.Movie;

/**
 * Main controller of the MovieStat application
 * @author Scott Jordan
 */
@Controller
public class HomeController {
    
    @Autowired
    private MovieSelection movieSelection;
    
    @Autowired
    private MovieDataSource movieDataSource;
    
    @RequestMapping("/")
    public ModelAndView index() throws MovieInformationUnavailableException {
        ModelAndView modelView = new ModelAndView("index");
        
        List<String> movieIds = movieSelection.getSelectedMovies();
        
        List<Movie> movies = movieDataSource.getMovies(movieIds);
        
        modelView.addObject("movies", movies);
        
        return modelView;
    }
}
