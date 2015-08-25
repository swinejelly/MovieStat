package edu.rit.moviestat.info;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.rit.moviestat.exception.MovieInformationUnavailableException;
import edu.rit.moviestat.model.MovieSelection;

/**
 * MovieSelection corresponding to those movies that are presently in theaters
 * (as reported by IMDB).
 * @author Scott Jordan
 */
public class InTheatersMovieSelectionDataSource implements MovieSelectionDataSource{
    private static final String IMDB_ROOT_URI = "http://www.imdb.com/";

    @Override
    public List<MovieSelection> getSelectedMovies() throws MovieInformationUnavailableException {
        String moviesInTheatersUri = IMDB_ROOT_URI + "movies-in-theaters/";
        
        Document imdbDoc;
        
        try {
            imdbDoc = Jsoup.connect(moviesInTheatersUri).get();
        } catch (IOException e) {
            throw new MovieInformationUnavailableException("Could not retrieve movies in theaters.", e);
        }
        
        Elements movieLinks = imdbDoc.select("h4 > a");
        
        List<MovieSelection> movieSelections = new ArrayList<>();
        for (Element movieLink: movieLinks) {
            String imdbId = extractIMDBIdFromTitleHref(movieLink.attr("href"));
            
            movieSelections.add(new MovieSelection(imdbId));
        }
        
        return movieSelections;
    }
    
    /**
     * Gets the IMDB id from a link of the form "/title/{imdb_id}/*"
     * @return The IMDB id or null if it could not be parsed.
     */
    private static String extractIMDBIdFromTitleHref(String href) {
        if (href != null && href.startsWith("/title/")) {
            String[] segments = href.split("/");
            
            if (segments.length >= 3) {
                return segments[2];
            }
        }
        
        return null;
    }

}
