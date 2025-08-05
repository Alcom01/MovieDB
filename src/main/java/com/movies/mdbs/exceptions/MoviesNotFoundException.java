package com.movies.mdbs.exceptions;

public class MoviesNotFoundException extends RuntimeException{
    public MoviesNotFoundException(String message){
        super(message);
    }
}
