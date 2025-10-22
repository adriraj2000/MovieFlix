package com.example.MovieFlix.service;

import com.example.MovieFlix.model.dto.omdb.MovieDetailsResponse;
import com.example.MovieFlix.model.dto.omdb.MovieSearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;

/**
 * Service for interacting with the OMDB API
 */
@Service
public class OmdbService {

    private static final Logger logger = LoggerFactory.getLogger(OmdbService.class);

    private final WebClient webClient;
    private final String apiKey;

    public OmdbService(
            @Value("${omdb.api.base-url}") String baseUrl,
            @Value("${omdb.api.key}") String apiKey,
            @Value("${omdb.api.timeout:5000}") int timeout) {
        this.apiKey = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        logger.info("OMDB Service initialized with base URL: {}", baseUrl);
    }

    /**
     * Search for movies by title
     *
     * @param title Movie title to search
     * @param page  Page number (optional, defaults to 1)
     * @return MovieSearchResponse containing search results
     */
    public MovieSearchResponse searchMovies(String title, Integer page) {
        logger.info("Searching movies with title: {}, page: {}", title, page);

        try {
            MovieSearchResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("apikey", apiKey)
                            .queryParam("s", title)
                            .queryParam("page", page != null ? page : 1)
                            .build())
                    .retrieve()
                    .bodyToMono(MovieSearchResponse.class)
                    .timeout(Duration.ofMillis(5000))
                    .block();

            if (response != null && "False".equals(response.getResponse())) {
                logger.warn("OMDB API returned error: {}", response.getError());
            } else if (response != null) {
                logger.info("Found {} results for title: {}", response.getTotalResults(), title);
            }

            return response;
        } catch (WebClientResponseException e) {
            logger.error("OMDB API error: {} - {}", e.getStatusCode(), e.getMessage());
            throw new RuntimeException("Failed to search movies: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while searching movies", e);
            throw new RuntimeException("Failed to search movies: " + e.getMessage());
        }
    }

    /**
     * Get detailed movie information by IMDB ID
     *
     * @param imdbId IMDB ID of the movie
     * @return MovieDetailsResponse containing detailed movie information
     */
    public MovieDetailsResponse getMovieDetails(String imdbId) {
        logger.info("Fetching movie details for IMDB ID: {}", imdbId);

        try {
            MovieDetailsResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("apikey", apiKey)
                            .queryParam("i", imdbId)
                            .queryParam("plot", "full")
                            .build())
                    .retrieve()
                    .bodyToMono(MovieDetailsResponse.class)
                    .timeout(Duration.ofMillis(5000))
                    .block();

            if (response != null && "False".equals(response.getResponse())) {
                logger.warn("OMDB API returned error: {}", response.getError());
                throw new RuntimeException("Movie not found: " + response.getError());
            } else if (response != null) {
                logger.info("Successfully fetched details for: {}", response.getTitle());
            }

            return response;
        } catch (WebClientResponseException e) {
            logger.error("OMDB API error: {} - {}", e.getStatusCode(), e.getMessage());
            throw new RuntimeException("Failed to fetch movie details: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while fetching movie details", e);
            throw new RuntimeException("Failed to fetch movie details: " + e.getMessage());
        }
    }

    /**
     * Get movie details by title
     *
     * @param title Movie title
     * @param year  Release year (optional)
     * @return MovieDetailsResponse containing detailed movie information
     */
    public MovieDetailsResponse getMovieByTitle(String title, String year) {
        logger.info("Fetching movie details for title: {}, year: {}", title, year);

        try {
            MovieDetailsResponse response = webClient.get()
                    .uri(uriBuilder -> {
                        var builder = uriBuilder
                                .queryParam("apikey", apiKey)
                                .queryParam("t", title)
                                .queryParam("plot", "full");
                        if (year != null && !year.isEmpty()) {
                            builder.queryParam("y", year);
                        }
                        return builder.build();
                    })
                    .retrieve()
                    .bodyToMono(MovieDetailsResponse.class)
                    .timeout(Duration.ofMillis(5000))
                    .block();

            if (response != null && "False".equals(response.getResponse())) {
                logger.warn("OMDB API returned error: {}", response.getError());
                throw new RuntimeException("Movie not found: " + response.getError());
            } else if (response != null) {
                logger.info("Successfully fetched details for: {}", response.getTitle());
            }

            return response;
        } catch (WebClientResponseException e) {
            logger.error("OMDB API error: {} - {}", e.getStatusCode(), e.getMessage());
            throw new RuntimeException("Failed to fetch movie details: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while fetching movie details", e);
            throw new RuntimeException("Failed to fetch movie details: " + e.getMessage());
        }
    }
}
