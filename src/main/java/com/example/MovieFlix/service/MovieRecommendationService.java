package com.example.MovieFlix.service;

import com.example.MovieFlix.model.dto.MovieVibeResponse;
import com.example.MovieFlix.model.dto.RecommendedMovie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Recommends movies with similar vibes using OpenAI
 */
@Service
public class MovieRecommendationService {

    private static final Logger logger = LoggerFactory.getLogger(MovieRecommendationService.class);
    private final OpenAiChatModel chatModel;

    private static final String RECOMMENDATION_PROMPT = """
            Based on this movie vibe analysis, recommend 3-5 movies with similar emotional feel:

            Source: %s
            Vibe: %s
            Themes: %s
            Moods: %s

            For each recommendation, provide:
            MOVIE: [Title (Year)]
            REASON: [Why it matches the vibe]

            Focus on emotional atmosphere match, not just genre. Include diverse time periods.
            """;

    public MovieRecommendationService(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public List<RecommendedMovie> getRecommendations(MovieVibeResponse vibe) {
        logger.info("Getting recommendations for vibe: {}", vibe.getVibe());

        String promptText = String.format(RECOMMENDATION_PROMPT,
                vibe.getTitle(),
                vibe.getVibe(),
                String.join(", ", vibe.getThemes()),
                String.join(", ", vibe.getMoods()));

        String response = chatModel.call(promptText);
        logger.debug("OpenAI recommendations: {}", response);

        return parseRecommendations(response);
    }

    private List<RecommendedMovie> parseRecommendations(String response) {
        List<RecommendedMovie> recommendations = new ArrayList<>();

        Pattern moviePattern = Pattern.compile("MOVIE:\\s*(.+?)\\s*\\((\\d{4})\\)", Pattern.CASE_INSENSITIVE);
        Pattern reasonPattern = Pattern.compile("REASON:\\s*(.+?)(?=MOVIE:|$)",
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

        Matcher movieMatcher = moviePattern.matcher(response);
        Matcher reasonMatcher = reasonPattern.matcher(response);

        while (movieMatcher.find() && reasonMatcher.find()) {
            recommendations.add(new RecommendedMovie(
                    movieMatcher.group(1).trim(),
                    movieMatcher.group(2).trim(),
                    reasonMatcher.group(1).trim()));
        }

        logger.info("Found {} recommendations", recommendations.size());
        return recommendations;
    }
}
