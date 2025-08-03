package com.movies.mdbs.exceptions;

public class MovieAlreadyExistsException extends RuntimeException {

    public MovieAlreadyExistsException(String message){
        super(message);
    }
}
