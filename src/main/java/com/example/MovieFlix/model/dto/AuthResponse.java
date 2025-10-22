package com.example.MovieFlix.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication response containing JWT token and minimal user info
 * Note: Internal details like user ID and email are not exposed for security
 * reasons
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String type = "Bearer";
    private String username;

    public AuthResponse(String token, String username) {
        this.token = token;
        this.type = "Bearer";
        this.username = username;
    }
}
