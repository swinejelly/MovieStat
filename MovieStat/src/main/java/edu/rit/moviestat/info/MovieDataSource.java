package edu.rit.moviestat.info;

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
     * @throws IOException If an error occurred retrieving the movie.
     */
    public Movie getMovie(String imdbId) throws MovieInformationUnavailableException;
}
