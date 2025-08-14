package com.movies.mdbs.serviceTests;


import com.movies.mdbs.entities.Director;
import com.movies.mdbs.entities.Movie;
import com.movies.mdbs.entities.Rating;
import com.movies.mdbs.exceptions.*;
import com.movies.mdbs.repository.MovieRepository;
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
    private Director director;

    @BeforeEach
    void setUp(){
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("SAW");
        movie.setDescription("John Kramer a Engineering prodigy starts playing games with folks who do not value their lives.");
        movie.setReleaseDate(LocalDate.of(2004, Month.OCTOBER,29));
        movie.setTmdbId(1L);

        rating = new Rating();
        rating.setId(1L);
        rating.setPopularity(60000.50);
        rating.setVoteAverage(87.5);
        rating.setVoteCount(1000.0);
        rating.setWeightedRating(((rating.getVoteCount() / (rating.getVoteCount() + 1000)) * rating.getVoteAverage() + (1000 / (rating.getVoteCount() + 1000)) * 7.0));

         director = new Director();
         director.setId(1L);
         director.setName("James Wan");
         director.setOriginalName("James Wan");

         List<Director> directors = new ArrayList<>();
         directors.add(director);


        movie.setRating(rating);
        movie.setDirectors(directors);

    }
    @Test
   void getAllMovies_shouldReturnMovies_whenListNotEmpty(){
        // Arrange
        when(movieRepository.findAll()).thenReturn(List.of(movie));

         //Act
        var result = movieService.getAllMovies();

        //Assert
        assertAll(() -> assertEquals(1,result.size()),
                  () -> assertEquals("SAW",result.get(0).getTitle()));
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

        double expectedWeightedRating =
                ((1000.0 / (1000.0 + 1000.0)) * 87.5 + (1000.0 / (1000.0 + 1000.0)) * 7.0);
        //Assert
        assertAll(() -> assertEquals("SAW",dto.getTitle()),
                  () ->  assertEquals("John Kramer a Engineering prodigy starts playing games with folks who do not value their lives.",
                        dto.getDescription()),
                  () -> assertEquals(LocalDate.of(2004,Month.OCTOBER,29),dto.getReleaseDate()),
                  () ->  assertEquals(60000.50,dto.getPopularity()),
                  () ->  assertEquals(expectedWeightedRating,dto.getWeightedRating()),
                  () -> assertEquals("James Wan",dto.getDirectors().get(0).getName())
        );
    }

    @Test
    void getAllMovies_shouldHandleZeroVoteCount(){
        rating.setVoteCount(0.0);
        movie.setRating(rating);

        when(movieRepository.findAll()).thenReturn(List.of(movie));

        var result = movieService.getAllMovies();
        assertNotNull(result.get(0).getWeightedRating());
    }


    @Test
    void getMoviesByTitle_shouldReturnFilteredMovieIfTitleMatches(){
        // Arrange
       when(movieRepository.findAll()).thenReturn(List.of(movie));

        //Act
        var result = movieService.getMoviesByTitle("SAW");

        // Assert
        assertAll(() -> assertEquals(1,result.size()),
                  ()  -> assertEquals("SAW",result.get(0).getTitle())
        );
    }
    @Test

    void getMoviesByTitle_shouldMapMovieToDtoCorrectly(){
        // Arrange
        when(movieRepository.findAll()).thenReturn(List.of(movie));
        // Act
        var result = movieService.getMoviesByTitle("SAW");
        var dto = result.get(0);
        double expectedWeightedRating =
                ((1000.0 / (1000.0 + 1000.0)) * 87.5 + (1000.0 / (1000.0 + 1000.0)) * 7.0);
        // Assert
        assertAll(() -> assertEquals("SAW",dto.getTitle()),
                () ->  assertEquals("John Kramer a Engineering prodigy starts playing games with folks who do not value their lives.",
                        dto.getDescription()),
                () -> assertEquals(LocalDate.of(2004,Month.OCTOBER,29),dto.getReleaseDate()),
                () ->  assertEquals(60000.50,dto.getPopularity()),
                () ->  assertEquals(expectedWeightedRating,dto.getWeightedRating()),
                ()  -> assertEquals("James Wan",dto.getDirectors().get(0).getName())
                );


    }

    @Test
    void getMoviesByTitle_shouldReturnMultipleMoviesIfTitlesMatch(){
        Movie movie2 = new Movie();
        movie2.setTitle("SAW");
        movie2.setReleaseDate(LocalDate.of(2001,Month.FEBRUARY,1));
        movie2.setRating(rating);
        movie2.setDirectors(List.of(director));

        when(movieRepository.findAll()).thenReturn(List.of(movie,movie2));

        var result = movieService.getMoviesByTitle("SAW");

        assertAll(() -> assertEquals(2,result.size()),
                  () -> assertTrue(result.stream().allMatch(m->m.getTitle().equals("SAW")))
                );
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
        assertAll("Invalid input",
                () -> assertThrows(TitleNotFoundException.class,()->movieService.getMoviesByTitle("")),
                () -> assertThrows(TitleNotFoundException.class,()->movieService.getMoviesByTitle(" ")),
                () -> assertThrows(TitleNotFoundException.class,()->movieService.getMoviesByTitle(null))
                );


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
    void getMoviesByYear_shouldMapDtoCorrectly() throws Exception {
        //Arrange
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        //Act
        var result = movieService.getMoviesByYear("2004");
        var dto = result.get(0);

        double expectedWeightedRating =
                ((1000.0 / (1000.0 + 1000.0)) * 87.5 + (1000.0 / (1000.0 + 1000.0)) * 7.0);

        assertAll(
                () -> assertEquals("SAW", dto.getTitle()),
                () -> assertEquals("John Kramer a Engineering prodigy starts playing games with folks who do not value their lives.",
                        dto.getDescription()),
                () -> assertEquals(LocalDate.of(2004, Month.OCTOBER, 29), dto.getReleaseDate()),
                () -> assertEquals(60000.50, dto.getPopularity()),
                () -> assertEquals(expectedWeightedRating, dto.getWeightedRating()),
                () -> assertEquals("James Wan", dto.getDirectors().get(0).getName())
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
    void addMovie_ShouldAddMovieIfIsNotPresent()throws Exception {
        // Arrange
        when(movieRepository.findByTitle("SAW")).thenReturn(Optional.empty());
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        // Act
        movieService.addMovie(movie);
        var result = movieService.getAllMovies();

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals("SAW", result.get(0).getTitle()),
                () -> verify(movieRepository).save(any(Movie.class))
        );


    }
    @Test
    void addMovie_ShouldThrowExceptionIfMovieIsPresent(){
        when(movieRepository.findByTitle("SAW")).thenReturn(Optional.of(movie));

        assertThrows(MovieAlreadyExistsException.class, () -> movieService.addMovie(movie));
    }

    @Test
    void addMovie_ShouldThrowExceptionIfDirectorIsEmptyOrNull(){
        assertAll(() -> assertThrows(DirectorNotFoundException.class,() -> movieService.getMoviesByDirector("")),
                  () -> assertThrows(DirectorNotFoundException.class,() -> movieService.getMoviesByDirector(null))
        );
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
    void deleteMovie_shouldThrowExceptionIfTitleIsNull(){
        assertThrows(TitleNotFoundException.class,() -> movieService.deleteMovie(null));

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
    @Test
    void getMoviesByDirector_shouldReturnMoviesIfExists(){
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        var result = movieService.getAllMovies();
        var directors= result.get(0).getDirectors();
        var moviesByDirectors = movieService.getMoviesByDirector(directors.toString());

        assertEquals(1, directors.size());
        assertEquals("SAW",moviesByDirectors.get(0).getTitle());
        assertTrue(directors.toString().contains("James Wan"));
    }

    @Test
    void getMoviesByDirector_shouldHandleMultipleDirectors(){
        Director director2 =  new Director();
        director2.setName("Zeki Demirkubuz");
        director2.setOriginalName("Zeki Demirkubuz");
        movie.setDirectors(List.of(director,director2));

        when(movieRepository.findAll()).thenReturn(List.of(movie));

        var result = movieService.getMoviesByDirector("James Wan");

        assertAll(() -> assertEquals(1,result.size()),
                  () -> assertTrue(result.get(0).getDirectors().stream().anyMatch(d -> d.getName().equals("James Wan")))
        );
    }
    @Test
    void getMoviesByDirector_shouldThrowExceptionIfNotExists(){
        when(movieRepository.findAll()).thenReturn(List.of(movie));
        assertThrows(DirectorNotFoundException.class, () -> movieService.getMoviesByDirector("Christopher Nolan"));
    }
}
