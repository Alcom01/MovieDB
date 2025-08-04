package com.movies.mdbs.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(value = {InvalidRatingException.class})
    public ResponseEntity<Object> handleInvalidRatingException(InvalidRatingException ex){
        // Creating a custom Payload
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        InvalidRatingBody invalidRatingBody =  new InvalidRatingBody(
                ex.getMessage(),
                badRequest,
                ZonedDateTime.now()
        );// return the response
        return new ResponseEntity<>(invalidRatingBody,badRequest);
    }


    @ExceptionHandler(value = {InvalidYearException.class})
    public ResponseEntity<Object> handleInvalidYearException(InvalidYearException ex){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        InvalidYearBody invalidYearBody = new InvalidYearBody(
                ex.getMessage(),
                badRequest,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(invalidYearBody,badRequest);
    }

    @ExceptionHandler(value = {TitleNotFoundException.class})
    public ResponseEntity<Object> handleTitleNotFoundException(TitleNotFoundException ex){
        HttpStatus  notFound = HttpStatus.NOT_FOUND;
        TitleNotFoundBody titleNotFoundBody = new TitleNotFoundBody(
                ex.getMessage(),
                notFound,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(titleNotFoundBody,notFound);
    }

    @ExceptionHandler(value = {MovieAlreadyExistsException.class})
    public ResponseEntity<Object> handleMovieAlreadyExistsException(MovieAlreadyExistsException ex){
        HttpStatus alreadyExists = HttpStatus.BAD_REQUEST;
        MovieAlreadyExistsBody movieAlreadyExistsBody= new MovieAlreadyExistsBody(
                ex.getMessage(),
                alreadyExists,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(movieAlreadyExistsBody,alreadyExists);
    }

}
