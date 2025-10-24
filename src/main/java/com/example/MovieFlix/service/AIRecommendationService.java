package com.example.MovieFlix.service;

import com.example.MovieFlix.model.dto.omdb.MovieDetailsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AI-powered recommendation service using OpenAI chat
 * Works immediately without requiring pre-indexed movies
 */
@Service
public class AIRecommendationService {

    private static final Logger logger = LoggerFactory.getLogger(AIRecommendationService.class);
    private final ChatModel chatModel;

    private static final String VIBE_AND_RECOMMENDATION_PROMPT = """
            Analyze this movie and recommend 5 similar movies:

            Title: %s (%s)
            Genre: %s
            Plot: %s
            Director: %s
            Actors: %s

            First, analyze the movie's vibe in 2-3 sentences focusing on:
            - Emotional atmosphere
            - Themes and motifs
            - Tone and style

            Then recommend 5 movies with similar vibes. For each recommendation provide:
            MOVIE: [Title (Year)]
            REASON: [Why it matches the vibe in 1-2 sentences]

            Focus on emotional similarity, not just genre matching.
            Include movies from different time periods for variety.
            """;

    public AIRecommendationService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * Get AI-powered recommendations based on movie metadata
     * No database required - works on first use
     */
    public AIRecommendationResult getRecommendations(MovieDetailsResponse movie) {
        logger.info("Getting AI recommendations for: {}", movie.getTitle());

        String promptText = String.format(VIBE_AND_RECOMMENDATION_PROMPT,
                movie.getTitle(),
                movie.getYear(),
                movie.getGenre() != null ? movie.getGenre() : "Unknown",
                movie.getPlot() != null ? movie.getPlot() : "No plot available",
                movie.getDirector() != null ? movie.getDirector() : "Unknown",
                movie.getActors() != null ? movie.getActors() : "Unknown");

        String response = chatModel.call(new Prompt(promptText)).getResult().getOutput().getContent();
        logger.debug("OpenAI response: {}", response);

        return parseResponse(response, movie.getTitle());
    }

    private AIRecommendationResult parseResponse(String response, String sourceTitle) {
        // Extract vibe analysis (everything before first "MOVIE:")
        String vibe = extractVibe(response);

        // Extract movie recommendations
        List<MovieRecommendation> recommendations = parseRecommendations(response);

        return new AIRecommendationResult(sourceTitle, vibe, recommendations);
    }

    private String extractVibe(String response) {
        // Get everything before the first "MOVIE:"
        int movieIndex = response.indexOf("MOVIE:");
        if (movieIndex > 0) {
            return response.substring(0, movieIndex).trim();
        }
        return "Unable to analyze vibe";
    }

    private List<MovieRecommendation> parseRecommendations(String response) {
        List<MovieRecommendation> recommendations = new ArrayList<>();

        Pattern moviePattern = Pattern.compile("MOVIE:\\s*(.+?)\\s*\\((\\d{4})\\)", Pattern.CASE_INSENSITIVE);
        Pattern reasonPattern = Pattern.compile("REASON:\\s*(.+?)(?=MOVIE:|$)",
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

        Matcher movieMatcher = moviePattern.matcher(response);
        Matcher reasonMatcher = reasonPattern.matcher(response);

        while (movieMatcher.find() && reasonMatcher.find()) {
            String title = movieMatcher.group(1).trim();
            String year = movieMatcher.group(2).trim();
            String reason = reasonMatcher.group(1).trim();

            // Remove quotes from title if present
            title = title.replaceAll("^\"|\"$", "");

            recommendations.add(new MovieRecommendation(title, year, reason));
        }

        logger.info("Found {} recommendations", recommendations.size());
        return recommendations;
    }

    /**
     * Result container for AI recommendations
     */
    public static class AIRecommendationResult {
        private final String sourceTitle;
        private final String vibe;
        private final List<MovieRecommendation> recommendations;

        public AIRecommendationResult(String sourceTitle, String vibe, List<MovieRecommendation> recommendations) {
            this.sourceTitle = sourceTitle;
            this.vibe = vibe;
            this.recommendations = recommendations;
        }

        public String getSourceTitle() {
            return sourceTitle;
        }

        public String getVibe() {
            return vibe;
        }

        public List<MovieRecommendation> getRecommendations() {
            return recommendations;
        }
    }

    /**
     * Individual movie recommendation
     */
    public static class MovieRecommendation {
        private final String title;
        private final String year;
        private final String reason;

        public MovieRecommendation(String title, String year, String reason) {
            this.title = title;
            this.year = year;
            this.reason = reason;
        }

        public String getTitle() {
            return title;
        }

        public String getYear() {
            return year;
        }

        public String getReason() {
            return reason;
        }
    }
}
