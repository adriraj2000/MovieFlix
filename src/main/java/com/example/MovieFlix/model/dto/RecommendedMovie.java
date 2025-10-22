package com.example.MovieFlix.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A recommended movie based on vibe similarity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedMovie {
    private String title;
    private String year;
    private String reason;
}
