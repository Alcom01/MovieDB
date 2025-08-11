package com.movies.mdbs.bootstrap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.mdbs.entities.Movie;
import com.movies.mdbs.entities.Rating;
import com.movies.mdbs.repository.DirectorRepository;
import com.movies.mdbs.repository.MovieRepository;
import com.movies.mdbs.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;


@PropertySource("classpath:env.properties")
@Component
public class MovieSeeder implements CommandLineRunner {

    private final MovieRepository movieRepository;

    @Value("${tmdb.api.key}")
            private String apiKey;

    double m = 1000;
    double C = 7.0;

    public MovieSeeder(MovieRepository movieRepository){
          this.movieRepository = movieRepository;

    }
    @Override
    public void run(String... args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();


        for (int page = 1; page <= 10; page++) {
            String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey + "&page=" + page;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JsonNode root = mapper.readTree(response.getBody()).get("results");

            for (JsonNode node : root) {
                //  getting fields for movie entity
                String title = node.get("title").asText();
                String description = node.get("overview").asText();
                String release = node.get("release_date").asText();
                Long tmdbId = node.get("id").asLong();
                LocalDate releaseDate;

                // getting  fields for rating entity
                double popularity = node.get("popularity").asDouble();
                double voteAverage = node.get("vote_average").asDouble();
                double voteCount = node.get("vote_count").asDouble();

                try {
                    releaseDate = LocalDate.parse(release);

                } catch (Exception e) {
                    releaseDate = LocalDate.now();
                }
                 if(!movieRepository.existsByTitleAndReleaseDate(title,releaseDate)){
                     Rating rating = new Rating(popularity,voteAverage,voteCount);
                    // ratingRepository.save(rating);
                     Movie movie = new Movie(title,description,releaseDate,rating,tmdbId);
                     movieRepository.save(movie);

                 }
            }
        }

    }

    }


