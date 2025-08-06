package com.movies.mdbs.serviceTests;


import com.movies.mdbs.entities.Movie;
import com.movies.mdbs.entities.Rating;
import com.movies.mdbs.exceptions.*;
import com.movies.mdbs.repository.MovieRepository;
import com.movies.mdbs.repository.RatingRepository;
import com.movies.mdbs.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieServiceUnitTest {
    @Mock
   private MovieRepository movieRepository;



    @InjectMocks
    private MovieService movieService;

    private Movie movie;
    private Rating rating;

    @BeforeEach
    void setUp(){
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("SAW");
        movie.setDescription("John Kramer a Engineering prodigy starts playing games with folks who do not value their lives.");
        movie.setReleaseDate(LocalDate.of(2004, Month.OCTOBER,29));

        rating = new Rating();
        rating.setId(1L);
        rating.setPopularity(60000.50);
        rating.setVoteAverage(87.5);
        rating.setVoteCount(1000.0);
        rating.setWeightedRating(((rating.getVoteCount() / (rating.getVoteCount() + 1000)) * rating.getVoteAverage() + (1000 / (rating.getVoteCount() + 1000)) * 7.0));


        movie.setRating(rating);

    }
    @Test
   void getAllMovies_shouldReturnMovies_whenListNotEmpty(){
        // Arrange
        when(movieRepository.findAll()).thenReturn(List.of(movie));

         //Act
        var result = movieService.getAllMovies();

        //Assert
        assertEquals(1,result.size());
        assertEquals("SAW",result.get(0).getTitle());
    }

    @Test
    void getAllMovies_shouldThrowException_whenListIsEmpty(){
        // Arrange
        when(movieRepository.findAll()).thenReturn(List.of());

        // Act& Assert
        assertThrows(MoviesNotFoundException.class, () -> movieService.getAllMovies());

    }
    @Test
    void getAllMovies_shouldMapDTOCorrectly(){
        //Arrange
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        //Act
        var result = movieService.getAllMovies();

        var dto = result.get(0);

        assertEquals("SAW",dto.getTitle());
        assertEquals("John Kramer a Engineering prodigy starts playing games with folks who do not value their lives.",
                dto.getDescription());
        assertEquals(LocalDate.of(2004,Month.OCTOBER,29),dto.getReleaseDate());
        assertEquals(60000.50,dto.getPopularity());

        double expectedWeightedRating =  ((1000.0 / (1000.0 + 1000.0)) * 87.5 + (1000.0 / (1000.0 + 1000.0)) * 7.0);
        assertEquals(expectedWeightedRating,dto.getWeightedRating());
    }
    @Test
    void getMoviesByTitle_shouldReturnFilteredMovieIfTitleMatches(){
        // Arrange
       when(movieRepository.findAll()).thenReturn(List.of(movie));

        //Act
        var result = movieService.getMoviesByTitle("SAW");

        // Assert
        assertEquals(1,result.size());
        assertEquals("SAW",result.get(0).getTitle());

    }

    @Test
    void getMoviesByTitle_shouldThrowExceptionIfTitleNotMatches(){
        //Arrange
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        //Assert & Throw
        assertThrows(TitleNotFoundException.class ,()-> movieService.getMoviesByTitle("Singham"));
    }


    @Test
    void getMoviesByTitle_shouldThrowExceptionIfTitleIsEmptyOrNull(){

        //Assert & Throw
         assertThrows(TitleNotFoundException.class ,()-> movieService.getMoviesByTitle(""));
         assertThrows(TitleNotFoundException.class,() -> movieService.getMoviesByTitle(null));


    }


    @Test
    void getMoviesByYear_shouldReturnMoviesIfYearMatches(){
        // Arrange
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        //Act
        var result = movieService.getMoviesByYear("2004");

        //Assert
        assertAll(
                ()-> assertEquals(1,result.size()),
                () -> assertEquals (2004,(int)result.get(0).getReleaseDate().getYear()),
                () -> assertEquals("SAW",result.get(0).getTitle())
        );
    }

    @Test
    void getMoviesByYear_shouldThrowExceptionIfYearNotMatches(){
        // Arrange
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        // Assert & throw
        assertThrows(InvalidYearException.class,() ->  movieService.getMoviesByYear("2008"));
    }

    @Test
    void getMoviesByYear_shouldThrowExceptionIfYearIsEmptyOrBlank(){
        assertAll(
                ()->assertThrows(InvalidYearException.class,()->movieService.getMoviesByYear("")),
                ()->assertThrows(InvalidYearException.class,()->movieService.getMoviesByYear(" "))

        );
    }

    @Test
    void getMoviesByYear_shouldThrowExceptionIfYearContainLetters(){
          assertAll(() -> assertThrows(InvalidYearException.class,()->movieService.getMoviesByYear("20a4")),
                    () -> assertThrows(InvalidYearException.class,()->movieService.getMoviesByYear("abcd")),
                    () -> assertThrows(InvalidYearException.class,()->movieService.getMoviesByYear("20@4")),
                    () -> assertThrows(InvalidYearException.class,()->movieService.getMoviesByYear("2024."))
          );


    }
    @Test
    void addMovie_ShouldAddMovieIfIsNotPresent() throws Exception {
        // mocks dont store changes automatically so i have create fakeDb which will serve as database.
       List<Movie> fakeDb = new ArrayList<>();
       fakeDb.add(movie);

       when(movieRepository.findAll()).thenAnswer(invocation -> new ArrayList<>(fakeDb));
       when(movieRepository.save(any(Movie.class))).thenAnswer(invocation ->{
              Movie m = invocation.getArgument(0);
              fakeDb.add(m);
              return m;
               }
               );

        double weightedRating =  (100.0/ (100.0 + 1000.0)) * 1500.0 + (1000.0 / (1000.0 + 1000.0)) * 7.0;

        Rating rating1 = new Rating();
        rating1.setId(2L);
        rating1.setPopularity(5000.0);
        rating1.setVoteAverage(1500.0);
        rating1.setVoteCount(100.0);
        rating1.setWeightedRating(weightedRating);

        Movie movie1 = new Movie();
        movie1.setId(2L);
        movie1.setTitle("Shame");
        movie1.setDescription("A guy who is chronically depressed");
        movie1.setReleaseDate( LocalDate.of(2013,Month.FEBRUARY,3));
        movie1.setRating(rating1);

        movieService.addMovie(movie1);

        var result = movieService.getAllMovies();

        assertAll(() -> assertEquals(2,result.size()),
                  () -> assertEquals("Shame",result.get(1).getTitle()));

        verify(movieRepository).save(movie1);

    }
    @Test
    void addMovie_ShouldThrowExceptionIfMovieIsPresent(){
        when(movieRepository.findByTitle("SAW")).thenReturn(Optional.of(movie));

        assertThrows(MovieAlreadyExistsException.class, () -> movieService.addMovie(movie));
    }

    @Test
    void deleteMovie_shouldDeleteIfMovieIsPresent() throws Exception {
        when(movieRepository.findByTitle("SAW")).thenReturn(Optional.of(movie));

        movieService.deleteMovie("SAW");

        verify(movieRepository).delete(movie);
    }
    @Test
    void deleteMovie_shouldThrowExceptionIfMovieIsNotPresent(){
        when(movieRepository.findByTitle("Shame")).thenReturn(Optional.empty());

        assertThrows(TitleNotFoundException.class,() -> movieService.deleteMovie("Shame"));

    }

    @Test
    void getByRating_shouldReturnMoviesIfExists(){
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        var result = movieService.getAllMovies();

         double rating = result.get(0).getWeightedRating();
        var  ratedMovies = movieService.getByRating(Double.toString(rating));

        assertAll(() -> assertEquals("SAW",ratedMovies.get(0).getTitle()),
                  () -> assertEquals(1,result.size())

        );
    }

    @Test
    void getByRating_shouldThrowExceptionIfRatingContainsLetter(){
        assertAll(() -> assertThrows(InvalidRatingException.class,() -> movieService.getByRating("7a")),
                  () -> assertThrows(InvalidRatingException.class,() -> movieService.getByRating("75c")),
                  () -> assertThrows(InvalidRatingException.class,() -> movieService.getByRating("7@")),
                  () -> assertThrows(InvalidRatingException.class,() -> movieService.getByRating("7-5"))
        );

    }

    @Test
    void getByRating_shouldThrowExceptionIfRatingEmptyOrNull(){
        assertAll(() -> assertThrows(InvalidRatingException.class,() -> movieService.getByRating("")),
                  () -> assertThrows(InvalidRatingException.class,() -> movieService.getByRating(" ")),
                  () -> assertThrows(InvalidRatingException.class,() -> movieService.getByRating(null))
        );
    }







}
