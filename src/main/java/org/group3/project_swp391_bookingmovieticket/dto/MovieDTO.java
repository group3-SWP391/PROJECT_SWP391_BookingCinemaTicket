package org.group3.project_swp391_bookingmovieticket.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import org.group3.project_swp391_bookingmovieticket.entity.Rating;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MovieDTO  {

    private int id;
    private String name;
    private String smallImageURL;
    private String shortDescription;
    private String longDescription;
    private String largeImageURL;
    private String director;
    private String actors;
    private String categories;
    private LocalDate releaseDate;
    private LocalDate endDate;
    private int duration;
    private String trailerURLWatchLink;
    private String language;
    private Integer statusShowing;
    private List<ScheduleDTO> schedules;
    private Long total;
    private RatingDTO ratingDTO;
    private Long totalTicket;
    private String format;

    // File uploads
    private MultipartFile smallImageFile;
    private MultipartFile largeImageFile;
}
