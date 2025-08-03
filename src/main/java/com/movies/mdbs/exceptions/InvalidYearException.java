package com.movies.mdbs.exceptions;



public class InvalidYearException extends RuntimeException {

    public InvalidYearException(String message){
        super(message);
    }

}
