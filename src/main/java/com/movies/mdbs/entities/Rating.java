package com.movies.mdbs.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;


    private Double popularity;
    private Double voteAverage;
    private Double voteCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double weightedRating;


    @PrePersist
    protected  void onCreate(){
        this.weightedRating = computeWeightedRating(this.voteAverage,this.voteCount);
    }




    public Rating(Double popularity,Double voteAverage, Double voteCount) {
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.weightedRating = computeWeightedRating(voteAverage,voteCount);

    }
    private double computeWeightedRating(double R, double v){
        return  (v / (v + 1000)) * R + (1000 / (v +1000)) * 7;
    }



}
