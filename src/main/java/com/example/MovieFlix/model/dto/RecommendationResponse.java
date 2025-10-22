package com.example.MovieFlix.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Complete response with vibe analysis and recommendations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponse {
    private MovieVibeResponse sourceMovie;
    private List<RecommendedMovie> recommendations;
}
