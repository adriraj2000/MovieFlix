package com.example.MovieFlix.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response containing movie vibe analysis
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieVibeResponse {
    private String title;
    private String year;
    private String vibe;
    private List<String> themes;
    private List<String> moods;
    private String reasoning;
}
