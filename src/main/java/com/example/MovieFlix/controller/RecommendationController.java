package com.example.MovieFlix.controller;

import com.example.MovieFlix.model.dto.MovieVibeResponse;
import com.example.MovieFlix.model.dto.RecommendationResponse;
import com.example.MovieFlix.model.dto.RecommendedMovie;
import com.example.MovieFlix.model.dto.omdb.MovieDetailsResponse;
import com.example.MovieFlix.service.OmdbService;
import com.example.MovieFlix.service.MovieRecommendationService;
import com.example.MovieFlix.service.MovieVibeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI-powered movie recommendations based on vibe analysis
 */
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);

    private final OmdbService omdbService;
    private final MovieVibeService movieVibeService;
    private final MovieRecommendationService movieRecommendationService;

    public RecommendationController(
            OmdbService omdbService,
            MovieVibeService movieVibeService,
            MovieRecommendationService movieRecommendationService) {
        this.omdbService = omdbService;
        this.movieVibeService = movieVibeService;
        this.movieRecommendationService = movieRecommendationService;
    }

    /**
     * Get vibe-based recommendations for a movie by title
     *
     * @param title Movie title
     * @param year  Optional year for disambiguation
     * @return Vibe analysis + recommendations
     */
    @GetMapping("/{title}")
    public ResponseEntity<RecommendationResponse> getRecommendations(
            @PathVariable String title,
            @RequestParam(required = false) String year) {
        logger.info("Get recommendations for title: {}, year: {}", title, year);

        // 1. Get movie from OMDB
        MovieDetailsResponse movie = omdbService.getMovieByTitle(title, year);

        // 2. Analyze vibe with AI
        MovieVibeResponse vibe = movieVibeService.analyzeVibe(movie);

        // 3. Get AI recommendations
        List<RecommendedMovie> recommendations = movieRecommendationService.getRecommendations(vibe);

        return ResponseEntity.ok(new RecommendationResponse(vibe, recommendations));
    }

    /**
     * Analyze vibe only (no recommendations)
     *
     * @param title Movie title
     * @param year  Optional year for disambiguation
     * @return Vibe analysis
     */
    @GetMapping("/vibe/{title}")
    public ResponseEntity<MovieVibeResponse> analyzeVibe(
            @PathVariable String title,
            @RequestParam(required = false) String year) {
        logger.info("Analyze vibe for title: {}", title);

        MovieDetailsResponse movie = omdbService.getMovieByTitle(title, year);
        MovieVibeResponse vibe = movieVibeService.analyzeVibe(movie);

        return ResponseEntity.ok(vibe);
    }
}
