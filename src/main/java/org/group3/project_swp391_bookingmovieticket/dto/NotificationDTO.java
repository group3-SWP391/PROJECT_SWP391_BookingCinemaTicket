package org.group3.project_swp391_bookingmovieticket.dto;

public class NotificationDTO {
    private String message;
    private int movieId;

    public NotificationDTO() {
    }

    public NotificationDTO(String message, int movieId) {
        this.message = message;
        this.movieId = movieId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}

