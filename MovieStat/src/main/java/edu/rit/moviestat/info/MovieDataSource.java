package edu.rit.moviestat.info;

import java.util.List;

import edu.rit.moviestat.exception.MovieInformationUnavailableException;
import edu.rit.moviestat.model.Movie;

/**
 * Provides information on a movie identified by its id on IMDB.
 * @author Scott Jordan
 */
public interface MovieDataSource {
    /**
     * Gets a Movie model from a movie's IMDB id.
     * @param imdbId The IMDB id of the movie.
     * @return Movie model
     * @throws MovieInformationUnavailableException If an error occurred retrieving the movie.
     */
    public Movie getMovie(String imdbId) throws MovieInformationUnavailableException;
    
    /**
     * Gets Movie models from a list of movie IMDB ids.
     * @param imdbIds List of IMDB ids of the movies.
     * @return List of Movie models
     * @throws MovieInformationUnavailableException If an error occurred retrieving any movie.
     */
    public List<Movie> getMovies(List<String> imdbIds) throws MovieInformationUnavailableException;
}
