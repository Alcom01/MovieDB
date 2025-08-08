package com.movies.mdbs.entities;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name="movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String title;
    private String description;
    private LocalDate releaseDate;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "rating_id", nullable = false, unique = true)
    private Rating rating;

    public Movie(String title, String description, LocalDate releaseDate, Rating rating) {
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.rating = rating;

    }
}

