package org.harvanir.demo.datasource.advice;

/**
 * @author Harvan Irsyadi
 */
public class InvalidRouteKeyException extends RuntimeException {

    public InvalidRouteKeyException(String message) {
        super(message);
    }
}
