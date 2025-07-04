package org.group3.project_swp391_bookingmovieticket.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
public class MovieDTO {
    private Integer movieId;

    @NotBlank(message = "Movie name cannot be blank")
    @Size(max = 255, message = "Movie name cannot exceed 255 characters")
    private String name;

    @Min(value = 1, message = "Duration must be greater than 0")
    private Integer duration;

    @NotBlank(message = "Categories cannot be blank")
    @Size(max = 100, message = "Categories cannot exceed 100 characters")
    private String categories;

    @NotBlank(message = "Language cannot be blank")
    @Size(max = 255, message = "Language cannot exceed 255 characters")
    private String language;

    @Size(max = 500, message = "Short description cannot exceed 500 characters")
    private String shortDescription;

    @Size(max = 1000, message = "Long description cannot exceed 1000 characters")
    private String longDescription;

    private Integer directorId;
    private Integer ratingId;
    private String format;
    private Integer isShowing;
    private String actorsJson;

    // File uploads
    private MultipartFile smallImageFile;
    private MultipartFile largeImageFile;

    // URLs for existing files (used in updates)
    private String smallImageUrl;
    private String largeImageUrl;
    
    // YouTube trailer URL
    @Size(max = 1000, message = "Trailer URL cannot exceed 1000 characters")
    private String trailerUrl;
} 