package edu.rit.moviestat;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import edu.rit.moviestat.info.CachingMovieDataSource;
import edu.rit.moviestat.info.CachingMovieSelectionDataSourceDecorator;
import edu.rit.moviestat.info.InTheatersMovieSelectionDataSource;
import edu.rit.moviestat.info.MovieDataSource;
import edu.rit.moviestat.info.MovieSelectionDataSource;
import edu.rit.moviestat.info.WebScrapingMovieDataSource;

/**
 * Spring configuration of the MovieStat application.
 * @author Scott Jordan
 */
@SpringBootApplication
public class MovieStatApplication extends SpringBootServletInitializer {
    
    @Autowired
    private AutowireCapableBeanFactory beanFactory;
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MovieStatApplication.class);
    }
    
    @Bean
    public MovieSelectionDataSource movieSelection() {
        MovieSelectionDataSource baseDataSource = new InTheatersMovieSelectionDataSource();
        
        beanFactory.autowireBean(baseDataSource);
        
        return new CachingMovieSelectionDataSourceDecorator(baseDataSource);
    }
    
    @Bean
    public MovieDataSource movieDataSource() {
        MovieDataSource baseDataSource = new WebScrapingMovieDataSource();
        
        beanFactory.autowireBean(baseDataSource);
        
        return new CachingMovieDataSource(baseDataSource);
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
    
    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        
        EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.DERBY)
                                     .setName("db/moviestatdb")
                                     .build();
        
        return db;
    }

    public static void main(String[] args) {
        SpringApplication.run(MovieStatApplication.class, args);
    }
}
