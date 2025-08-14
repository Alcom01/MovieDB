package com.movies.mdbs.bootstrap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.mdbs.entities.Director;
import com.movies.mdbs.repository.DirectorRepository;
import com.movies.mdbs.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@PropertySource("classpath:env.properties")
@Component
public class DirectorSeeder implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Value("${tmdb.api.key}")
    private  String apiKey;

    public DirectorSeeder(MovieRepository movieRepository,DirectorRepository directorRepository){
        this.movieRepository = movieRepository;
        this.restTemplate = new RestTemplate();
        this.mapper = new ObjectMapper();
        this.directorRepository = directorRepository;
    }
    @Override
    public void run(String... args) throws Exception {
      movieRepository.findAll().forEach(movie -> {
          if(movie.getTmdbId() !=null){
              try {
                  String url = "https://api.themoviedb.org/3/movie/" + movie.getTmdbId() + "/credits?api_key=" + apiKey;
                  String response = restTemplate.getForObject(url, String.class);
                  JsonNode root = mapper.readTree(response);
                  JsonNode crew = root.get("crew");

                  if(crew != null  && crew.isArray()){
                      List<Director> directors = new ArrayList<>();
                      for(JsonNode person : crew){
                          if("Director".equals(person.get("job").asText())){
                              String name = person.get("name").asText();
                              String originalName =person.has("original_name") ? person.get("original_name").asText() : name;
                              Director director = new Director(name,originalName);
                              directors.add(director);
                          }
                      }
                      // persisting directors( directors table was empty at the beginning.)
                      directors = directorRepository.saveAll(directors);
                      movie.setDirectors(directors);

                      movieRepository.save(movie);

                  }
              } catch(Exception e) {
                  System.out.println("Error fetching directors for movie " + movie.getTitle() + ": " + e.getMessage());
              }
          }
      });

    }
}
