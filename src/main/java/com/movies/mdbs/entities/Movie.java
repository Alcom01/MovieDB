package com.movies.mdbs.entities;
import jakarta.persistence.*;
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
    private Long id;

    private String title;
    private String description;
    private LocalDate releaseDate;

    @OneToOne
    @JoinColumn(name = "rating_id", nullable = false, unique = true)
    private Rating rating;

    public Movie(String title, String description, LocalDate releaseDate, Rating rating) {
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.rating = rating;

    }
}

