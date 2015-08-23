package edu.rit.moviestat.info;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import edu.rit.moviestat.model.Actor;
import edu.rit.moviestat.model.Movie;

/**
 * MovieDataSource that sources information for the Movie model by scraping
 * publicly available data sources, namely IMDB and Wikipedia.
 * @author Scott Jordan
 */
public class WebScrapingMovieDataSource implements MovieDataSource {
    
    private static final String IMDB_ROOT_URI = "http://www.imdb.com/";
    
    private static final String WIKIPEDIA_ROOT_URI = "https://en.wikipedia.org/";

    @Override
    public Movie getMovie(String imdbId) throws IOException {
        String url = IMDB_ROOT_URI + "title/" + imdbId;
        
        Document doc = Jsoup.connect(url).get();
        
        String title = getTitle(doc);
        
        LocalDate releaseDate = getReleaseDate(doc);
        
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
     * @return LocalDate of a movie's release date if possible otherwise null.
     */
    private static LocalDate getReleaseDate(Document doc) {
        Element releaseDateElement = doc.select("meta[itemprop=datePublished]").first();

        if (releaseDateElement != null) {
            String releaseDateText = releaseDateElement.attr("content");
            
            try {
                return LocalDate.parse(releaseDateText, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException exception) {}
        }
        
        return null;
    }
    
    /**
     * Get the birthdate of an actor from his/her wikipedia article.
     * @param doc Document of an actor's wikipedia article.
     * @return LocalDate of actor's birthdate if possible otherwise null.
     */
    private static LocalDate getBirthdate(Document doc) {
        Element birthdayElement = doc.select("span.bday").first();
        
        if (birthdayElement != null) {
            String birthdayText = birthdayElement.text();
            
            try {
                return LocalDate.parse(birthdayText, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {}
        }
        
        return null;
    }
    
    /**
     * Get the actors of a movie from its IMDB page.
     * @param doc Document of a movie's IMDB page.
     */
    private static List<Actor> getActors(Document doc) {
        List<String> actorNames = doc.select("td[itemtype$=Person]").stream()
                                     .map(element -> element.select("span").html())
                                     .collect(Collectors.toList());
        
        List<Actor> actors = new ArrayList<Actor>();
        
        for (String actorName: actorNames) {
            Actor actor = getActor(actorName);
            
            actors.add(actor);
        }
        
        return actors;
    }
    
    /**
     * Get an Actor from his/her name with as much available information as possible.
     * @param actorName The full name of the actor.
     * @return Actor with as much information populated as possible.
     */
    private static Actor getActor(String actorName) {
        try {
            String actorWikipediaURL = getActorWikipediaURL(actorName);
            
            if (actorWikipediaURL != null) {
                Document doc = Jsoup.connect(actorWikipediaURL).get();
                
                LocalDate birthdate = getBirthdate(doc);
                
                return new Actor(actorName, birthdate);
            }
        } catch (IOException exception) {}
        
        return new Actor(actorName, null);
    }
    
    /**
     * Gets the URL of an actor's wikipedia article.
     * @param actorName The name of the actor.
     * @return Wikipedia article URL or null if one could not be found.
     */
    private static String getActorWikipediaURL(String actorName) throws IOException {
        String searchUrl = getWikipediaActorSearchUrlQuery(actorName);
        
        Document doc = Jsoup.connect(searchUrl).get();
        
        Element urlElement = doc.select("Url").first();
        
        if (urlElement != null) {
            return urlElement.text();
        }
        
        return null;
    }
    
    /**
     * Creates the URL of a query to Wikipedia requesting search results for 
     * actorName. The results will be in JSON OpenSearch format.
     * @param actorName The name of the actor to search for.
     * @return String URL
     */
    private static String getWikipediaActorSearchUrlQuery(String actorName) {
        String urlEncodedActorName;
        try {
            urlEncodedActorName = URLEncoder.encode(actorName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            /*
             * This exception should never occur on a valid JVM for UTF-8
             * and this method's client cannot reasonably recover from this error.
             */
            throw new RuntimeException(e);
        }
        
        String wikiApiUri = WIKIPEDIA_ROOT_URI + "w/api.php";
        
        String queryExtension = 
                String.format("?action=opensearch&format=xml&search=%s&limit=1&redirects=resolve", urlEncodedActorName);
        
        return wikiApiUri + queryExtension;
    }

}
