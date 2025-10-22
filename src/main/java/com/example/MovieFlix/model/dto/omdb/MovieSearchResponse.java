package com.example.MovieFlix.model.dto.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response from OMDB API search endpoint
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieSearchResponse {

    @JsonProperty("Search")
    private List<MovieSearchResult> search;

    @JsonProperty("totalResults")
    private String totalResults;

    @JsonProperty("Response")
    private String response;

    @JsonProperty("Error")
    private String error;
}
