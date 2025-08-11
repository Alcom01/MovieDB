package com.movies.mdbs.dto;

import com.movies.mdbs.entities.Director;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MovieRatingDTO {
    private Long movieId;
    private String title;
    private String description;
    private LocalDate releaseDate;
    private Double popularity;
    private Double weightedRating;
    private List<Director> directors;
}
