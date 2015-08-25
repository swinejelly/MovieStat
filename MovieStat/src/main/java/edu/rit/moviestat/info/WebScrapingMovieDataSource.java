package edu.rit.moviestat.info;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import edu.rit.moviestat.exception.MovieInformationUnavailableException;
import edu.rit.moviestat.model.Actor;
import edu.rit.moviestat.model.Movie;
import edu.rit.moviestat.model.MovieSelection;

/**
 * MovieDataSource that sources information for the Movie model by scraping
 * publicly available data sources, namely IMDB and Wikipedia.
 * @author Scott Jordan
 */
public class WebScrapingMovieDataSource implements MovieDataSource {
    
    private static final String IMDB_ROOT_URI = "http://www.imdb.com/";
    
    private static final String WIKIPEDIA_ROOT_URI = "https://en.wikipedia.org/";
    
    @Autowired
    private Executor movieExecutor;
    
    @Autowired
    private Executor actorExecutor;

    @Override
    public Movie getMovie(MovieSelection movieSelection) throws MovieInformationUnavailableException {
        String url = IMDB_ROOT_URI + "title/" + movieSelection.getImdbId();
        
        Document doc;
        
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new MovieInformationUnavailableException("Could not retrieve movie information.", e);
        }
        
        String title = getTitle(doc);
        
        Calendar releaseDate = getReleaseDate(doc);
        
        List<Actor> actors = getActors(doc);
        
        return new Movie(movieSelection.getImdbId(), title, releaseDate, actors);
    }
    

    @Override
    public List<Movie> getMovies(List<MovieSelection> movieSelections) throws MovieInformationUnavailableException {
        List<RunnableFuture<Movie>> movieFutures = new ArrayList<>();
        for (MovieSelection movieSelection: movieSelections) {
            movieFutures.add(getMovieRunnableFuture(movieSelection));
        }
        
        List<Movie> movies = new ArrayList<Movie>();
        
        for (RunnableFuture<Movie> movieFuture: movieFutures) {
            movieExecutor.execute(movieFuture);
        }
        
        for (RunnableFuture<Movie> movieFuture: movieFutures) {
            try {
                movies.add(movieFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                // Neither exception should happen in normal or recoverable operation of the program.
                throw new RuntimeException(e);
            }
        }
        
        return movies;
    }
    
    /**
     * Creates a RunnableFuture for a Movie
     * @param movieSelection MovieSelection to get Movie for
     * @return RunnableFuture that resolves to a Movie
     */
    public RunnableFuture<Movie> getMovieRunnableFuture(final MovieSelection movieSelection) {
        return new FutureTask<Movie>( new Callable<Movie>() {
            @Override
            public Movie call() throws Exception {
                return getMovie(movieSelection);
            }
        });
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
     * @return Calendar of a movie's release date if possible otherwise null.
     */
    private static Calendar getReleaseDate(Document doc) {
        Element releaseDateElement = doc.select("meta[itemprop=datePublished]").first();

        if (releaseDateElement != null) {
            String releaseDateText = releaseDateElement.attr("content");
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            Calendar releaseDate = Calendar.getInstance();
            
            try {
                releaseDate.setTime(sdf.parse(releaseDateText));
                
                return releaseDate;
            } catch (ParseException exception) {}
        }
        
        return null;
    }
    
    /**
     * Get the birthdate of an actor from his/her wikipedia article.
     * @param doc Document of an actor's wikipedia article.
     * @return LocalDate of actor's birthdate if possible otherwise null.
     */
    private static Calendar getBirthdate(Document doc) {
        Element birthdayElement = doc.select("span.bday").first();
        
        if (birthdayElement != null) {
            String birthdayText = birthdayElement.text();
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            Calendar birthDate = Calendar.getInstance();
            
            try {
                birthDate.setTime(sdf.parse(birthdayText));
                
                return birthDate;
            } catch (ParseException e) {}
        }
        
        return null;
    }
    
    /**
     * Get the actors of a movie from its IMDB page.
     * @param doc Document of a movie's IMDB page.
     * @return List of Actors
     */
    private List<Actor> getActors(Document doc) throws MovieInformationUnavailableException {
        Elements personElements = doc.select("td[itemtype$=Person]");
        
        List<RunnableFuture<Actor>> actorFutures = new ArrayList<>();
        for (Element personElement: personElements) {
            String name = personElement.select("span").html();
            
            actorFutures.add(getActorRunnableFuture(name));
        }
        
        List<Actor> actors = new ArrayList<Actor>();
        
        for (RunnableFuture<Actor> actorFuture: actorFutures) {
            actorExecutor.execute(actorFuture);
        }
        
        for (Future<Actor> actorFuture: actorFutures) {
            try {
                actors.add(actorFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                // Neither exception should happen in normal or recoverable operation of the program.
                throw new RuntimeException(e);
            }
        }
        
        return actors;
    }
    
    /**
     * Creates a RunnableFuture for an Actor.
     * @param actorName Name of the actor to get information for.
     * @return RunnableFuture that resolves to an Actor.
     */
    private static RunnableFuture<Actor> getActorRunnableFuture(final String actorName) {
        return new FutureTask<Actor>( new Callable<Actor>() {
            @Override
            public Actor call() throws Exception {
                return getActor(actorName);
            }
        });
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
                
                Calendar birthdate = getBirthdate(doc);
                
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
