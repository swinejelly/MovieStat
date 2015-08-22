package edu.rit.moviestat.info;

import java.io.IOException;
import edu.rit.moviestat.model.Movie;

/**
 * Provides information on a movie identified by its id on IMDB.
 * @author Scott Jordan
 */
public interface MovieDataSource {
    public Movie getMovie(String imdbId) throws IOException;
}
