package com.movies.mdbs.service;
import com.movies.mdbs.dto.MovieRatingDTO;
import com.movies.mdbs.entities.Movie;
import com.movies.mdbs.exceptions.*;
import com.movies.mdbs.repository.MovieRepository;
import com.movies.mdbs.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;




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
        movieRatingDTO.setDirectors(movie.getDirectors());

         return movieRatingDTO;

    }



    public List<MovieRatingDTO> getAllMovies(){
        List<MovieRatingDTO> allMovies = movieRepository.findAll()
                .stream()
                .map(this::convertEntityToDTO)
                .toList();
        if(allMovies.isEmpty()){
            throw new MoviesNotFoundException("There is no movie in database");
        }
        return allMovies;

    }




    public List<MovieRatingDTO> getMoviesByTitle(String title){
           if(isEmptyOrNull(title)){
               throw new TitleNotFoundException("Title Cannot Be empty or null");
           }
            List<MovieRatingDTO> movieDto= movieRepository.findAll()
                    .stream()
                    .map(this::convertEntityToDTO)
                    .filter(movie -> movie.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .toList();

            if(movieDto.isEmpty()){
                 throw new TitleNotFoundException("Title not found!!!");
            }

            return movieDto;


    }


    public  List<MovieRatingDTO> getMoviesByYear(String year) {
        // checking if entered year is empty or contains only whitespaces
        if(isEmptyOrNull(year)){
            throw new InvalidYearException("Year cannot be empty or blank.");
        }
        // preventing user to enter alphabetic characters
            if(containsChar(year) ){
                throw new InvalidYearException("Year cannot contain letters.");
            }

        List<MovieRatingDTO> movieRatingDTOList = movieRepository.findAll()
                .stream()
                .map(this::convertEntityToDTO)
                .filter(movie -> movie.getReleaseDate().toString().toLowerCase().contains(year.toLowerCase()))
                .toList();
           if(movieRatingDTOList.isEmpty()){
               throw new InvalidYearException("There is no movie exists by given year.");
           }
           return  movieRatingDTOList;
    }

    public void  addMovie(Movie movie) throws Exception {

      Optional<Movie> film = movieRepository.findByTitle(movie.getTitle());
      if(film.isPresent()){
          throw new MovieAlreadyExistsException("Entered movie already exists.");
      }
      if(movie.getTitle().isEmpty()){
          throw new MovieAlreadyExistsException("Title cannot be empty.");
      }
      if(movie.getReleaseDate() == null ){
          throw new MovieAlreadyExistsException("Release date cannot be empty");
      }
      if(movie.getDirectors() == null || movie.getDirectors().toString().trim().isEmpty()){
          throw new DirectorNotFoundException("Director cannot be empty or null");
      }

      movieRepository.save(movie);
    }

    public void  deleteMovie(String title) throws Exception {
        Optional<Movie> movie = movieRepository.findByTitle(title);

        if(movie.isPresent()){
            movieRepository.delete(movie.get());
        }else{
            throw new TitleNotFoundException("There is no movie with given title");
        }
    }

     public List<MovieRatingDTO> getByRating(String weightedRating){
        if(isEmptyOrNull(weightedRating)){
            throw new InvalidRatingException("Rating cannot be empty.");
        }

         if(containsChar(weightedRating)){
             throw new InvalidRatingException("Rating cannot contain letters.");
         }

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
          if (moviesRated.isEmpty() ){
              throw new InvalidRatingException("There is no movies within specified rate range.");
          }

          return moviesRated;
         }

         public List<MovieRatingDTO> getMoviesByDirector(String name){
        List<MovieRatingDTO> allMovies = movieRepository.findAll().stream()
                .map(this::convertEntityToDTO)
                .toList();

          List<MovieRatingDTO> allMoviesOfDirector = new ArrayList<>();

          for(MovieRatingDTO movie : allMovies){
              if(movie.getDirectors().toString().toLowerCase().contains(name.toLowerCase())){
                  allMoviesOfDirector.add(movie);
              }
          }
          if(allMoviesOfDirector.isEmpty()){
              throw new DirectorNotFoundException("There no movie title related to given Director");
          }
          return allMoviesOfDirector;
         }

         //helper method that checks number field contains character if it does return true.
         public boolean containsChar(String text){
           return !text.matches("^\\d+(\\.\\d+)?$");

         }
       //helper method that checks  field contains space or is null,if it does return true.
         public boolean isEmptyOrNull(String text){
             return text == null || text.trim().isEmpty();
         }

    }





