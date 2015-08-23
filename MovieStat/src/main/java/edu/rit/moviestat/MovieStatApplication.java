package edu.rit.moviestat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import edu.rit.moviestat.info.InTheatersMovieSelection;
import edu.rit.moviestat.info.MovieDataSource;
import edu.rit.moviestat.info.MovieSelection;
import edu.rit.moviestat.info.WebScrapingMovieDataSource;

/**
 * Spring configuration of the MovieStat application.
 * @author Scott Jordan
 */
@SpringBootApplication
public class MovieStatApplication extends SpringBootServletInitializer {
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MovieStatApplication.class);
    }
    
    @Bean
    public MovieSelection movieSelection() {
        return new InTheatersMovieSelection();
    }
    
    @Bean
    public MovieDataSource movieDataSource() {
        return new WebScrapingMovieDataSource();
    }

    public static void main(String[] args) {
        SpringApplication.run(MovieStatApplication.class, args);
    }
}
