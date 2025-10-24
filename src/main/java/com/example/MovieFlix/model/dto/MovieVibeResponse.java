package com.example.MovieFlix.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response containing movie metadata and vibe analysis
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieVibeResponse {
    private String title;
    private String year;
    private String genre;
    private String vibe;
}
