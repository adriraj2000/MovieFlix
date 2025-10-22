package com.example.MovieFlix.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard API response wrapper
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private Map<String, Object> errors;
    private LocalDateTime timestamp;
    private int status;

    private ApiResponse(boolean success, String message, T data, Map<String, Object> errors, int status) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, null, 200);
    }

    public static <T> ApiResponse<T> success(String message, T data, int status) {
        return new ApiResponse<>(true, message, data, null, status);
    }

    public static <T> ApiResponse<T> error(String message, Map<String, Object> errors, int status) {
        return new ApiResponse<>(false, message, null, errors, status);
    }

    public static <T> ApiResponse<T> error(String message, int status) {
        return new ApiResponse<>(false, message, null, null, status);
    }
}
