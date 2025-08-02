package com.movies.mdbs.service;

import com.movies.mdbs.dto.MovieRatingDTO;
import com.movies.mdbs.entities.Movie;
import com.movies.mdbs.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class MovieService {
   private final MovieRepository movieRepository;



    @Autowired
    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;

    }
    private MovieRatingDTO convertEntityToDTO(Movie movie){
        MovieRatingDTO movieRatingDTO = new MovieRatingDTO();
        movieRatingDTO.setMovieId(movie.getId());
        movieRatingDTO.setTitle(movie.getTitle());
        movieRatingDTO.setDescription(movie.getDescription());
        movieRatingDTO.setReleaseDate(movie.getReleaseDate());
        movieRatingDTO.setPopularity(movie.getRating().getPopularity());
        movieRatingDTO.setWeightedRating(movie.getRating().getWeightedRating());

         return movieRatingDTO;

    }


    public List<MovieRatingDTO> getAllMovies(){
        return movieRepository.findAll()
                .stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());

    }




    public List<MovieRatingDTO> getMoviesByTitle(String title){
        return movieRepository.findAll()
                .stream()
                .map(this::convertEntityToDTO)
                .filter(movie -> movie.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }


    public  List<MovieRatingDTO> getMoviesByYear(String year) {
        return movieRepository.findAll()
                .stream()
                .map(this::convertEntityToDTO)
                .filter(movie -> movie.getReleaseDate().toString().toLowerCase().contains(year.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void  addMovie(Movie movie) throws Exception {
        Optional<Movie>  film = movieRepository.findById(movie.getId());
        if(film.isPresent()){
            throw new Exception("Movie already exists!!");
        }
        movieRepository.save(movie);
    }
    public void  deleteMovie(String title) throws Exception {
        Optional<Movie> film = movieRepository.findByTitle(title);

        if(film.isEmpty()){
            throw new Exception("Movie does not exists.");
        }else{
            movieRepository.delete(film.get());

        }
    }

     public List<MovieRatingDTO> getByRating(String weightedRating){
          List<MovieRatingDTO> allMovies = movieRepository.findAll()
                  .stream()
                  .map(this::convertEntityToDTO)
                  .toList();
            Double rating = Double.valueOf(weightedRating);

          List<MovieRatingDTO> moviesRated = new ArrayList<>();
          for( MovieRatingDTO movie : allMovies){
                if(movie.getWeightedRating() >= rating){
                      moviesRated.add(movie);
                }
          }
          return moviesRated;
         }

    }





