package com.movies.mdbs.exceptions;

public class TitleNotFoundException extends RuntimeException{

    public TitleNotFoundException(String message){
          super(message);
    }
}
