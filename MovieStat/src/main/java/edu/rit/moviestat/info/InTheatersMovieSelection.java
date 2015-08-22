package edu.rit.moviestat.info;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class InTheatersMovieSelection implements MovieSelection{
    private static final String IMDB_ROOT_URI = "http://www.imdb.com/";

    @Override
    public List<String> getSelectedMovies() throws IOException {
        String moviesInTheatersUri = IMDB_ROOT_URI + "movies-in-theaters/";
        
        Document imdbDoc = Jsoup.connect(moviesInTheatersUri).get();
        
        Elements movieLinks = imdbDoc.select("h4 > a");
        
        return movieLinks.stream().map(element -> extractIMDBIdFromTitleHref(element.attr("href"))).collect(Collectors.toList());
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