package edu.rit.moviestat.info;

import java.util.List;

import edu.rit.moviestat.exception.MovieInformationUnavailableException;

/**
 * An arbitrary selection of movies identified by their IMDB ids.
 * @author Scott Jordan
 */
public interface MovieSelection {
    /**
     * @return IMDB ids of an arbitrary selection of movies.
     */
    public List<String> getSelectedMovies() throws MovieInformationUnavailableException;
}
