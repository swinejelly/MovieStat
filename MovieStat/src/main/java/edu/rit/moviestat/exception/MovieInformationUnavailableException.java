package edu.rit.moviestat.exception;

/**
 * Exception thrown when critical information for a movie is not available for any reason.
 * @author Scott Jordan
 */
public class MovieInformationUnavailableException extends Exception {
    public MovieInformationUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public MovieInformationUnavailableException(String message) {
        super(message);
    }
    
    public MovieInformationUnavailableException(Throwable cause) {
        super(cause);
    }
}
