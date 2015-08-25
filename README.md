# MovieStat

A Spring application that scrapes and stores publically available movie information and displays statistics through a web application.

This is a learning project intended to cover web scraping and using Spring with Dependency Injection.

# Design

I set out to create a simple web application that would display a single templated homepage. This way, once deployed
it is available over the network to any client and additional pages or APIs for JSON models could be added as desired.

The application caches information such as what movies are currently showing or the actors in the movies instead of
scraping the internet everytime a request is made. The information is stored and accessed using an in-memory database,
which could be switched to a persistent database in order to maintain information across invocations of the program.

Access to the database is managed through Spring and Spring Data. By using Spring Data's automatically generated implementation
of CrudRepository I can perform many common operations on all tables without writing any code or SQL.

The web scraping first accesses IMDB (Internet Movie Database) to determine what movies are currently in theaters
as well as what actors star in the movie. IMDB also maintains a unique "IMDB Id" for every movie which I use to identify
movies in the application. After retrieving the actor names, the application searches for an article on Wikipedia about
that actor and tries to extract their birthdate.

Because of how many web pages need to be accessed and parsed at a time, two thread pools are used so that movies and actors
can be looked up in parallel.

Spring's Dependency Injection is used through the entire application to simplify configuration and set up, allowing data sources,
thread pools, and CrudRepositories to be instantiated anywhere that needs them.

# Set up

This application depends on having JDK 1.7 and the most recent version of Maven. 

If you are on Windows, the most recent version of the [Spring Tool Suite](https://spring.io/tools/sts/all) will include Maven and the project 
can be imported. JDK 1.7 can be installed from [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html).

If you are on Linux, installing JDK 1.7 and Maven may vary depending on your distribution and where you want them installed.
See [here](http://openjdk.java.net/install/) for instructions on the JDK. See [here](https://maven.apache.org/download.cgi) for how to install Maven.

# Running

If you are on Windows, running edu.rit.moviestat.MovieStatApplication from inside Spring Tool Suite will start the server on localhost:8080.

If you are on Linux, after installing JDK 1.7 and the most recent version of Maven, `cd` into the `MovieStat` directory in your cloned repository.
Running

    mvn spring-boot:run

will start the application on localhost:8080.

Once the server is running, visit localhost:8080 in your web browser to see your very own movie statistics!