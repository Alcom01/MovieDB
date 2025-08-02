package com.movies.mdbs.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MovieRatingDTO {
    private Long movieId;
    private String title;
    private String description;
    private LocalDate releaseDate;
    private Double popularity;
    private Double weightedRating;
}
