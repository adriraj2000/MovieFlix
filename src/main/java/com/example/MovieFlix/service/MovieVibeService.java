package com.example.MovieFlix.service;

import com.example.MovieFlix.model.dto.MovieVibeResponse;
import com.example.MovieFlix.model.dto.omdb.MovieDetailsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Analyzes movie vibe using OpenAI GPT-4
 */
@Service
public class MovieVibeService {

    private static final Logger logger = LoggerFactory.getLogger(MovieVibeService.class);
    private final OpenAiChatModel chatModel;
    private static final String VIBE_PROMPT = """
            Analyze this movie's emotional vibe and thematic feel:

            Title: %s (%s)
            Genre: %s
            Plot: %s
            Director: %s

            Provide analysis in this exact format:
            VIBE: [2-3 word emotional feel]
            THEMES: [3-5 key themes, comma-separated]
            MOODS: [3-5 emotional moods, comma-separated]
            REASONING: [2-3 sentences explaining the vibe]

            Focus on emotional atmosphere, not just genre.
            """;

    public MovieVibeService(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public MovieVibeResponse analyzeVibe(MovieDetailsResponse movie) {
        logger.info("Analyzing vibe for: {}", movie.getTitle());

        String promptText = String.format(VIBE_PROMPT,
                movie.getTitle(),
                movie.getYear(),
                movie.getGenre(),
                movie.getPlot(),
                movie.getDirector());

        String response = chatModel.call(promptText);
        logger.debug("OpenAI response: {}", response);

        return parseResponse(response, movie.getTitle(), movie.getYear());
    }

    private MovieVibeResponse parseResponse(String response, String title, String year) {
        MovieVibeResponse vibe = new MovieVibeResponse();
        vibe.setTitle(title);
        vibe.setYear(year);
        vibe.setVibe(extract(response, "VIBE:"));
        vibe.setThemes(splitList(extract(response, "THEMES:")));
        vibe.setMoods(splitList(extract(response, "MOODS:")));
        vibe.setReasoning(extract(response, "REASONING:"));
        return vibe;
    }

    private String extract(String text, String label) {
        Pattern pattern = Pattern.compile(label + "\\s*(.+?)(?=(\\n[A-Z]+:|$))", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : "";
    }

    private List<String> splitList(String text) {
        return Arrays.stream(text.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}
