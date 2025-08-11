package com.movies.mdbs.controller;
import com.movies.mdbs.dto.MovieRatingDTO;
import com.movies.mdbs.entities.Movie;
import com.movies.mdbs.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/v1/tmdbs-movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    public MovieController(MovieService movieService){
        this.movieService = movieService;
    }


    @GetMapping("/all-movies")
    public List<MovieRatingDTO> getAllMoviesWRating(){
        return movieService.getAllMovies();

    }
    @GetMapping("/by-title/{title}")
    public List<MovieRatingDTO> getByTitle(@PathVariable  String title){
        return movieService.getMoviesByTitle(title);
    }

    @GetMapping("/by-year/{year}")
    public List<MovieRatingDTO> getByYear(@PathVariable String year){
        return movieService.getMoviesByYear(year);
    }
    @PostMapping("/add-movie")
    public void addMovie(@RequestBody Movie movie) throws Exception {
         Movie movie1 = new Movie();
         movie1.setTitle(movie.getTitle());
         movie1.setDescription(movie.getDescription());
         movie1.setReleaseDate(movie.getReleaseDate());
         movie1.setRating(movie.getRating());
         movieService.addMovie(movie1);

    }

    @DeleteMapping("/delete-movie/{title}")
    public void deleteMovie (@PathVariable  String title) throws Exception {
        movieService.deleteMovie(title);
    }

    @GetMapping("/by-rating/{weightedRating}")
        public List<MovieRatingDTO> getMovieByRating(@PathVariable String weightedRating){
          return movieService.getByRating(weightedRating);

        }
        @GetMapping("/by-director/{name}")
     public List<MovieRatingDTO> getMoviesByDirectorName(@PathVariable String name){
        return movieService.getMoviesByDirector(name);
        }
    }



