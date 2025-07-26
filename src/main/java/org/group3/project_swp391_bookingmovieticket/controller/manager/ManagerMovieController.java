package org.group3.project_swp391_bookingmovieticket.controller.manager;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.dto.MovieDTO;
import org.group3.project_swp391_bookingmovieticket.entity.*;
import org.group3.project_swp391_bookingmovieticket.repository.*;
import org.group3.project_swp391_bookingmovieticket.service.impl.MovieService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/manager")
public class ManagerMovieController {

    @Autowired
    private IMovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private IDirectorRepository directorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IActorRepository actorRepository;

    @Autowired
    private IMovieActorRepository movieActorRepository;

    @Autowired
    private IRatingRepository ratingRepository;

    private static final String UPLOAD_DIR = "uploads/movies/";

    private String saveFile(MultipartFile file, String type) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        Path uploadPath = Paths.get(UPLOAD_DIR + type);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;

        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/" + UPLOAD_DIR + type + "/" + filename;
    }

    private String processYouTubeUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return null;
        }
        String videoId = extractYouTubeVideoId(url);
        if (videoId != null) {
            return "https://www.youtube.com/embed/" + videoId;
        }

        return null;
    }

    private String extractYouTubeVideoId(String url) {
        String pattern = "(?:youtube\\.com/(?:[^/]+/.+/|(?:v|e(?:mbed)?)/|.*[?&]v=)|youtu\\.be/)([^\"&?/ ]{11})";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private boolean isYouTubeUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        // Check if URL contains YouTube domains
        return url.toLowerCase().contains("youtube.com") ||
                url.toLowerCase().contains("youtu.be");
    }

    private String convertYouTubeToWatchUrl(String embedUrl) {
        if (embedUrl != null && embedUrl.contains("youtube.com/embed/")) {
            String videoId = embedUrl.substring(embedUrl.lastIndexOf("/") + 1);
            // Remove any query parameters
            if (videoId.contains("?")) {
                videoId = videoId.substring(0, videoId.indexOf("?"));
            }
            return "https://www.youtube.com/watch?v=" + videoId;
        }
        return embedUrl;
    }

    private String validateDirectorData(String name, String description) {
        if (name == null || name.trim().isEmpty()) {
            return "Director name is required";
        }
        if (name.length() > 255) {
            return "Director name cannot exceed 255 characters";
        }
        if (description != null && description.length() > 255) {
            return "Director description cannot exceed 255 characters";
        }

        return null; // No validation errors
    }

    private String validateActorData(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Actor name is required";
        }
        if (name.length() > 255) {
            return "Actor name cannot exceed 255 characters";
        }

        return null; // No validation errors
    }

    private String validateRatingData(String name, String description) {
        if (name == null || name.trim().isEmpty()) {
            return "Rating name is required";
        }
        if (name.length() > 50) {
            return "Rating name cannot exceed 50 characters";
        }
        if (description != null && description.length() > 255) {
            return "Rating description cannot exceed 255 characters";
        }

        return null; // No validation errors
    }

    @GetMapping("/movies")
    public String showMoviesManagement(Model model, HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user == null) {
            return "redirect:/login";
        }

        List<Movie> movies = movieService.findAllMovie();
        System.out.println(movies + " movies");
        List<Director> directors = directorRepository.findAll();
        List<Actor> actors = actorRepository.findAll();
        List<Rating> ratings = ratingRepository.findByIsActive(true);

        model.addAttribute("movieAll", movies);
        model.addAttribute("directors", directors);
        model.addAttribute("actors", actors);
        model.addAttribute("ratings", ratings);
        model.addAttribute("content", "manager/movies");

        return "manager/layout";
    }

    @PostMapping("/movies")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveMovie(@ModelAttribute MovieDTO movieDTO, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("success", false);
            response.put("message", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            boolean isUpdate = movieDTO.getId() != 0;
            Movie movie;

            if (isUpdate) {
                Optional<Movie> movieOpt = movieRepository.findById(movieDTO.getId());
                if (!movieOpt.isPresent()) {
                    response.put("success", false);
                    response.put("message", "Movie not found");
                    return ResponseEntity.ok(response);
                }
                movie = movieOpt.get();
                movieActorRepository.deleteByMovie_Id(movieDTO.getId());
            } else {
                movie = new Movie();
                movie.setViews(0);
                movie.setReleaseDate(LocalDate.now());
                if (movieDTO.getId() != 0) {
                    movie.setEndDate(LocalDate.now());
                }
            }

            movie.setName(movieDTO.getName());
            movie.setCategories(movieDTO.getCategories());
            movie.setDuration(movieDTO.getDuration());
            movie.setLanguage(movieDTO.getLanguage());
            //movie.setRated(movieDTO.getRatingId());
            movie.setShortDescription(movieDTO.getShortDescription());
            movie.setLongDescription(movieDTO.getLongDescription());
            movie.setFormat(movieDTO.getFormat() != null ? movieDTO.getFormat().trim() : null);
            movie.setStatusShowing(movieDTO.getStatusShowing());

            try {
                String smallImagePath = saveFile(movieDTO.getSmallImageFile(), "images");
                String largeImagePath = saveFile(movieDTO.getLargeImageFile(), "images");

                if (smallImagePath != null) {
                    movie.setSmallImageURL(smallImagePath);
                } else if (isUpdate) {
                    movie.setSmallImageURL(movieDTO.getSmallImageURL());
                }

                if (largeImagePath != null) {
                    movie.setLargeImageURL(largeImagePath);
                } else if (isUpdate) {
                    movie.setLargeImageURL(movieDTO.getLargeImageURL());
                }

                if (movieDTO.getTrailerURLWatchLink() != null && !movieDTO.getTrailerURLWatchLink().trim().isEmpty()) {
                    String processedYoutubeUrl = processYouTubeUrl(movieDTO.getTrailerURLWatchLink().trim());
                    if (processedYoutubeUrl != null) {
                        movie.setTrailerURLWatchLink(processedYoutubeUrl);
                    } else {
                        response.put("success", false);
                        response.put("message", "Invalid YouTube URL format");
                        return ResponseEntity.ok(response);
                    }
                }
//                else if (movieDTO.getTrailerFile() != null && !movieDTO.getTrailerFile().isEmpty()) {
//                    String trailerPath = saveFile(movieDTO.getTrailerFile(), "videos");
//                    if (trailerPath != null) {
//                        movie.setTrailerUrlWatchLink(trailerPath);
//                    }
//                }
//                else if (isUpdate) {
//                    movie.setTrailerUrlWatchLink(movieDTO.getTrailerUrl());
//                }

            } catch (IOException e) {
                response.put("success", false);
                response.put("message", "Error uploading files: " + e.getMessage());
                return ResponseEntity.ok(response);
            }

            if (movieDTO.getDirector() != null) {
                Optional<Director> director = directorRepository.findByNameContainingIgnoreCase((movieDTO.getDirector()));
                director.ifPresent(movie::setDirector);
            }

            if (movieDTO.getRatingDTO() != null) {
                Optional<Rating> rating = ratingRepository.findById(movieDTO.getRatingDTO().getId());
                rating.ifPresent(movie::setRating);
            }

            if (!isUpdate) {
                movie.setReleaseDate(LocalDate.now());
            }

            Movie savedMovie = movieRepository.save(movie);

            if (movieDTO.getActors() != null && !movieDTO.getActors().trim().isEmpty()) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> actorList = objectMapper.readValue(movieDTO.getActors(), List.class);

                    for (Map<String, Object> actorData : actorList) {
                        if (actorData.get("actorId") != null && actorData.get("characterName") != null) {
                            String characterName = (String) actorData.get("characterName");
                            if (characterName.length() > 255) {
                                response.put("success", false);
                                response.put("message", "Character name '" + characterName + "' cannot exceed 255 characters");
                                return ResponseEntity.badRequest().body(response);
                            }

                            Integer actorId = Integer.parseInt(actorData.get("actorId").toString());

                            Optional<Actor> actor = actorRepository.findById(actorId);
                            if (actor.isPresent()) {
                                MovieActor movieActor = new MovieActor();
                                movieActor.setMovie(savedMovie);
                                movieActor.setActor(actor.get());
                                movieActor.setNameInMovie(characterName);
                                movieActorRepository.save(movieActor);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing actors JSON: " + e.getMessage());
                }
            }
            response.put("success", true);
            response.put("message", isUpdate ? "Movie updated successfully!" : "Movie added successfully!");
            response.put("movieId", savedMovie.getId());

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error saving movie: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/movies/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMovie(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Movie> movieOpt = movieRepository.findById(id);
            if (movieOpt.isPresent()) {
                Movie movie = movieOpt.get();

                // Get movie actors for this movie
                List<MovieActor> movieActors = movieActorRepository.findByMovie_Id(id);

                // Determine trailer type for edit mode and prepare URL
                String trailerType = "upload"; // default
                String trailerUrl = movie.getTrailerURLWatchLink();
                String displayTrailerUrl = trailerUrl;

                if (trailerUrl != null && !trailerUrl.trim().isEmpty()) {
                    if (isYouTubeUrl(trailerUrl)) {
                        trailerType = "youtube";
                        // Convert embed URL to watch URL for better user experience
                        displayTrailerUrl = convertYouTubeToWatchUrl(trailerUrl);
                    }
                }

                response.put("success", true);
                response.put("movie", movie);
                response.put("movieActors", movieActors);
                response.put("trailerType", trailerType);
                response.put("displayTrailerUrl", displayTrailerUrl);
            } else {
                response.put("success", false);
                response.put("message", "Movie not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving movie: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/movies/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteMovie(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Movie> movieOpt = movieRepository.findById(id);
            if (movieOpt.isPresent()) {
                movieActorRepository.deleteByMovie_Id(id);
                movieRepository.deleteById(id);

                response.put("success", true);
                response.put("message", "Movie deleted successfully!");
            } else {
                response.put("success", false);
                response.put("message", "Movie not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting movie: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/directors/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addDirector(@RequestBody Map<String, String> directorData) {
        Map<String, Object> response = new HashMap<>();

        try {
            Director director = new Director();
            director.setName(directorData.get("name"));
            director.setDescription(directorData.get("description"));

            Director savedDirector = directorRepository.save(director);

            response.put("success", true);
            response.put("message", "Director added successfully!");
            response.put("director", savedDirector);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding director: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/directors")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createDirector(@RequestBody Map<String, String> directorData) {
        Map<String, Object> response = new HashMap<>();

        // Server-side validation
        String validationError = validateDirectorData(directorData.get("name"), directorData.get("biography"));
        if (validationError != null) {
            response.put("success", false);
            response.put("message", validationError);
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Director director = new Director();
            director.setName(directorData.get("name"));
            director.setDescription(directorData.get("biography"));

            Director savedDirector = directorRepository.save(director);

            response.put("success", true);
            response.put("message", "Director created successfully!");
            response.put("director", savedDirector);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating director: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/actors")
    @ResponseBody
    public ResponseEntity<List<Actor>> getAllActors(HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        List<Actor> actors = actorRepository.findAll();
        return ResponseEntity.ok(actors);
    }

    @PostMapping("/actors")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addActor(@RequestBody Map<String, String> actorData) {
        Map<String, Object> response = new HashMap<>();

        String validationError = validateActorData(actorData.get("name"));
        if (validationError != null) {
            response.put("success", false);
            response.put("message", validationError);
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Actor actor = new Actor();
            actor.setName(actorData.get("name"));

            Actor savedActor = actorRepository.save(actor);

            response.put("success", true);
            response.put("message", "Actor added successfully!");
            response.put("actor", savedActor);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding actor: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ratings")
    @ResponseBody
    public ResponseEntity<List<Rating>> getAllRatings(HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        List<Rating> ratings = ratingRepository.findByIsActive(true);
        return ResponseEntity.ok(ratings);
    }

    @PostMapping("/ratings")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addRating(@RequestBody Map<String, String> ratingData) {
        Map<String, Object> response = new HashMap<>();

        String validationError = validateRatingData(ratingData.get("name"), ratingData.get("description"));
        if (validationError != null) {
            response.put("success", false);
            response.put("message", validationError);
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // Check if rating name already exists
            Rating existingRating = ratingRepository.findByName(ratingData.get("name"));
            if (existingRating != null) {
                response.put("success", false);
                response.put("message", "Rating with this name already exists");
                return ResponseEntity.badRequest().body(response);
            }

            Rating rating = new Rating();
            rating.setName(ratingData.get("name"));
            rating.setDescription(ratingData.get("description"));

            // Parse age limit if provided
            if (ratingData.get("ageLimit") != null && !ratingData.get("ageLimit").trim().isEmpty()) {
                try {
                    rating.setAgeLimit(Integer.parseInt(ratingData.get("ageLimit")));
                } catch (NumberFormatException e) {
                    response.put("success", false);
                    response.put("message", "Invalid age limit format");
                    return ResponseEntity.badRequest().body(response);
                }
            }

            rating.setActive(true); // Default to active

            Rating savedRating = ratingRepository.save(rating);

            response.put("success", true);
            response.put("message", "Rating added successfully!");
            response.put("rating", savedRating);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding rating: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}