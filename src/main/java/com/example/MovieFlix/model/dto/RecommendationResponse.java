package com.example.MovieFlix.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Complete response with source movie info, vibe analysis, and recommendations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponse {
    private String sourceTitle;
    private String sourceYear;
    private String sourceGenre;
    private String vibe;
    private List<RecommendedMovie> recommendations;
}
