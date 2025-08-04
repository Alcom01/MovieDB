package com.movies.mdbs.repository;

import com.movies.mdbs.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MovieRepository  extends JpaRepository<Movie,Long> {
    boolean existsByTitleAndReleaseDate(String title, LocalDate releaseDate);
    Optional<Movie>findByTitleAndReleaseDate(String title,LocalDate releaseDate);
    Optional<Movie>findByTitle(String title);




}
