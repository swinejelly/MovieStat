package edu.rit.moviestat.info;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.rit.moviestat.model.Actor;
import edu.rit.moviestat.model.Movie;

public class WebScrapingMovieDataSource implements MovieDataSource {
    
    private static final String IMDB_ROOT_URI = "http://www.imdb.com/";

    @Override
    public Movie getMovie(String imdbId) throws IOException {
        String url = IMDB_ROOT_URI + "title/" + imdbId;
        
        Document doc = Jsoup.connect(url).get();
        
        String title = getTitle(doc);
        
        Date releaseDate = getReleaseDate(doc);
        
        List<Actor> actors = getActors(doc);
        
        return new Movie(title, releaseDate, actors);
    }
    
    /**
     * Get the title of a movie from its IMDB page.
     * @param doc Document of a movie's IMDB page.
     */
    private static String getTitle(Document doc) {
        return doc.select("h1").first().child(0).html();
    }
    
    /**
     * Get the release date of a movie from its IMDB page.
     * @param doc Document of a movie's IMDB page.
     */
    private static Date getReleaseDate(Document doc) {
        String releaseDateText = doc.select("meta[itemprop=datePublished]").first().attr("content");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        try {
            return dateFormat.parse(releaseDateText);
        } catch (ParseException exception) {
            return null;
        }
    }
    
    /**
     * Get the actors of a movie from its IMDB page.
     * @param doc Document of a movie's IMDB page.
     */
    private static List<Actor> getActors(Document doc) {
        List<Actor> actors = doc.select("td[itemtype$=Person]").stream()
                                     .map(element -> element.select("span").html())
                                     .map(name -> new Actor(name)).collect(Collectors.toList());
        
        return actors;
    }

}
