package com.movies.mdbs.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Double popularity;
    private Double voteAverage;
    private Double voteCount;
    private Double weightedRating;




    public Rating(Double popularity,Double voteAverage, Double voteCount,Double weightedRating) {

        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.weightedRating = weightedRating;

    }
    private double computeWeightedRating(double R,double v,double C,double m){
        return  (v / (v + m)) * R + (m / (v + m)) * C;
    }
}
