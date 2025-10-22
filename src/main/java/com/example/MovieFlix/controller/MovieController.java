package com.example.MovieFlix.controller;

import com.example.MovieFlix.model.dto.omdb.MovieDetailsResponse;
import com.example.MovieFlix.model.dto.omdb.MovieSearchResponse;
import com.example.MovieFlix.service.OmdbService;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for movie search and details
 */
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    private final OmdbService omdbService;

    public MovieController(OmdbService omdbService) {
        this.omdbService = omdbService;
    }

    /**
     * Search for movies by title
     *
     * @param query Search query
     * @param page  Page number (optional, defaults to 1)
     * @return Search results
     */
    @GetMapping("/search")
    public ResponseEntity<MovieSearchResponse> searchMovies(
            @RequestParam("q") @NotBlank String query,
            @RequestParam(value = "page", required = false) Integer page) {
        logger.info("Search request received: query={}, page={}", query, page);
        MovieSearchResponse response = omdbService.searchMovies(query, page);
        return ResponseEntity.ok(response);
    }

    /**
     * Get movie details by title
     *
     * @param title Movie title
     * @param year  Release year (optional)
     * @return Movie details
     */
    @GetMapping("/{title}")
    public ResponseEntity<MovieDetailsResponse> getMovieByTitle(
            @PathVariable String title,
            @RequestParam(value = "year", required = false) String year) {
        logger.info("Get movie by title request received: title={}, year={}", title, year);
        MovieDetailsResponse response = omdbService.getMovieByTitle(title, year);
        return ResponseEntity.ok(response);
    }
}
