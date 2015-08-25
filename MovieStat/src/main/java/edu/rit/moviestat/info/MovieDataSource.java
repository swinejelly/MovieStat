package edu.rit.moviestat.info;

import java.util.List;

import edu.rit.moviestat.exception.MovieInformationUnavailableException;
import edu.rit.moviestat.model.Movie;
import edu.rit.moviestat.model.MovieSelection;

/**
 * Provides information on a movie identified by its id on IMDB.
 * @author Scott Jordan
 */
public interface MovieDataSource {
    /**
     * Gets a Movie model from a MovieSelection
     * @param movieSelection The MovieSelection
     * @return Movie model
     * @throws MovieInformationUnavailableException If an error occurred retrieving the movie.
     */
    public Movie getMovie(MovieSelection movieSelection) throws MovieInformationUnavailableException;
    
    /**
     * Gets Movie models from a list of MovieSelections
     * @param movieSelections List of MovieSelections
     * @return List of Movie models
     * @throws MovieInformationUnavailableException If an error occurred retrieving any movie.
     */
    public List<Movie> getMovies(List<MovieSelection> movieSelections) throws MovieInformationUnavailableException;
}
