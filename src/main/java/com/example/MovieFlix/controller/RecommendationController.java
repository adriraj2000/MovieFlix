package com.example.MovieFlix.controller;

import com.example.MovieFlix.model.dto.RecommendationResponse;
import com.example.MovieFlix.model.dto.RecommendedMovie;
import com.example.MovieFlix.model.dto.omdb.MovieDetailsResponse;
import com.example.MovieFlix.service.AIRecommendationService;
import com.example.MovieFlix.service.OmdbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Main recommendation controller - Single public endpoint
 */
@RestController
@RequestMapping("/api")
public class RecommendationController {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);

    private final OmdbService omdbService;
    private final AIRecommendationService aiRecommendationService;

    public RecommendationController(OmdbService omdbService, AIRecommendationService aiRecommendationService) {
        this.omdbService = omdbService;
        this.aiRecommendationService = aiRecommendationService;
    }

    /**
     * Get AI-powered movie recommendations
     * 
     * Single endpoint that does everything:
     * 1. Fetches movie metadata from OMDB API
     * 2. Infers vibe from metadata using AI
     * 3. Generates 5 similar movie recommendations based on vibe
     * 
     * Works immediately - no setup required!
     */
    @GetMapping("/recommendations")
    public ResponseEntity<RecommendationResponse> getRecommendations(
            @RequestParam String title,
            @RequestParam(required = false) String year) {
        logger.info("Get recommendations for: title='{}', year='{}'", title, year);

        // Step 1: Get movie metadata from OMDB
        MovieDetailsResponse movie = omdbService.getMovieByTitle(title, year);

        // Step 2 & 3: Infer vibe and generate recommendations using AI
        AIRecommendationService.AIRecommendationResult result = aiRecommendationService.getRecommendations(movie);

        // Format response using proper DTOs
        List<RecommendedMovie> recommendations = result.getRecommendations().stream()
                .map(r -> new RecommendedMovie(r.getTitle(), r.getYear(), r.getReason()))
                .collect(Collectors.toList());

        RecommendationResponse response = new RecommendationResponse(
                movie.getTitle(),
                movie.getYear(),
                movie.getGenre(),
                result.getVibe(),
                recommendations);

        return ResponseEntity.ok(response);
    }
}
