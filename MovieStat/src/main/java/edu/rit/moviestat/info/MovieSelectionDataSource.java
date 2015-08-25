package edu.rit.moviestat.info;

import java.util.List;

import edu.rit.moviestat.exception.MovieInformationUnavailableException;
import edu.rit.moviestat.model.MovieSelection;

/**
 * An arbitrary selection of movies identified by their IMDB ids.
 * @author Scott Jordan
 */
public interface MovieSelectionDataSource {
    /**
     * @return MovieSelections of an arbitrary selection of movies.
     */
    public List<MovieSelection> getSelectedMovies() throws MovieInformationUnavailableException;
}
