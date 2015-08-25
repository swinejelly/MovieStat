package edu.rit.moviestat.info;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import edu.rit.moviestat.exception.MovieInformationUnavailableException;
import edu.rit.moviestat.model.ActorRepository;
import edu.rit.moviestat.model.Movie;
import edu.rit.moviestat.model.MovieRepository;
import edu.rit.moviestat.model.MovieSelection;

/**
 * MovieDataSource decorator that creates/uses a cache from the results of another 
 * MovieDataSource.
 * @author Scott Jordan
 */
public class CachingMovieDataSource implements MovieDataSource {
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private ActorRepository actorRepository;
    
    private MovieDataSource decoratee;
    
    public CachingMovieDataSource(MovieDataSource decoratee) {
        this.decoratee = decoratee;
    }

    @Override
    public Movie getMovie(MovieSelection movieSelection) throws MovieInformationUnavailableException {
        Movie movie = movieRepository.findOne(movieSelection.getImdbId());
        
        if (movie == null) {
            movie = decoratee.getMovie(movieSelection);
        }
        
        return movie;
    }

    @Override
    public List<Movie> getMovies(List<MovieSelection> movieSelections) throws MovieInformationUnavailableException {
        List<String> imdbIds = movieSelections.stream().map(movieSelection -> movieSelection.getImdbId()).collect(Collectors.toList());
        
        List<Movie> foundMovies = getAllMoviesFromRepository(imdbIds);
        List<String> foundImdbIds = foundMovies.stream().map(movie -> movie.getImdbId()).collect(Collectors.toList());
        
        Collection<String> missingImdbIds = new ArrayList<String>(imdbIds);
        missingImdbIds.removeAll(foundImdbIds);
        
        List<MovieSelection> missingMovieSelection = missingImdbIds.stream().map(MovieSelection::new).collect(Collectors.toList());
        
        List<Movie> remainingMovies = decoratee.getMovies(missingMovieSelection);
        
        saveNewMovies(remainingMovies);
        
        foundMovies.addAll(remainingMovies);
        
        return foundMovies;
    }
    
    private List<Movie> getAllMoviesFromRepository(List<String> imdbIds) {
        Iterable<Movie> allFoundMovies = movieRepository.findAll(imdbIds);
        
        List<Movie> foundMovies = new ArrayList<>();
        allFoundMovies.forEach(foundMovies::add);
        
        return foundMovies;
    }
    
    private void saveNewMovies(List<Movie> movies) {
        for (Movie movie: movies) {
            actorRepository.save(movie.getCast());
        }
        
        movieRepository.save(movies);
    }
}
