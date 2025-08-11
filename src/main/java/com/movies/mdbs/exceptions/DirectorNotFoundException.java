package com.movies.mdbs.exceptions;

public class DirectorNotFoundException extends RuntimeException{

    public DirectorNotFoundException(String message) {
        super(message);
    }
}
