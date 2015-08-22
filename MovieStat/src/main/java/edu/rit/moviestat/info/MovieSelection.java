package edu.rit.moviestat.info;

import java.io.IOException;
import java.util.List;

/**
 * An arbitrary selection of movies identified by their IMDB ids.
 * @author Scott Jordan
 */
public interface MovieSelection {
    /**
     * @return IMDB ids of an arbitrary selection of movies.
     */
    public List<String> getSelectedMovies() throws IOException;
}
