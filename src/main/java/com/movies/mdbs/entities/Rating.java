package com.movies.mdbs.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;


    private Double popularity;
    private Double voteAverage;
    private Double voteCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull
    private Double weightedRating;


    private  static final  double C = 7.0;


    private  static final double m = 1000;

    private double computeWeightedRating(double R, double v){
        return  (v / (v + Rating.m)) * R + (Rating.m / (v + Rating.m)) * Rating.C;
    }

    public Rating(Double popularity,Double voteAverage, Double voteCount) {

        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.weightedRating = computeWeightedRating(voteAverage,voteCount);

    }

}
