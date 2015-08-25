package edu.rit.moviestat;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

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
    
    @Bean
    @Scope("prototype")
    public Executor executor() {
        int corePoolSize = 32;
        int maximumPoolSize = 32;
        
        long keepAliveTime = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
        
        return new ThreadPoolExecutor(corePoolSize,
                                      maximumPoolSize,
                                      keepAliveTime,
                                      unit,
                                      workQueue);
    }

    public static void main(String[] args) {
        SpringApplication.run(MovieStatApplication.class, args);
    }
}
