package com.movies.mdbs.entities;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name="movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;


    @Column(unique = true)
    private Long tmdbId;

    private String title;
    private String description;
    private LocalDate releaseDate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "movies_directors",
            joinColumns = @JoinColumn(name = "movie_id" ,referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "director_id", referencedColumnName = "id")
    )
    private List<Director> directors = new ArrayList<>();




    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "rating_id", nullable = false, unique = true)
    private Rating rating;

    public Movie(String title, String description, LocalDate releaseDate, Rating rating,Long tmdbId) {
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.tmdbId = tmdbId;
    }
}

