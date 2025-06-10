package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entities.*;
import org.group3.project_swp391_bookingmovieticket.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/manager")
public class  ManagerMovieController {

    @Autowired
    private IMovieRepository movieRepository;

    @Autowired
    private IDirectorRepository directorRepository;

    @Autowired
    private IActorRepository actorRepository;

    @Autowired
    private IMovieActorRepository movieActorRepository;
    
    private static final String UPLOAD_DIR = "uploads/movies/";
    
    private String saveFile(MultipartFile file, String type) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR + type);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;
        
        // Save file
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Return relative path for database storage
        return "/" + UPLOAD_DIR + type + "/" + filename;
    }

    @GetMapping("/movies")
    public String showMoviesManagement(Model model, HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user == null) {
            return "redirect:/login";
        }

        // Get all movies with their relationships
        List<Movie> movies = movieRepository.findAll();
        List<Director> directors = directorRepository.findAll();
        List<Actor> actors = actorRepository.findAll();

        model.addAttribute("movies", movies);
        model.addAttribute("directors", directors);
        model.addAttribute("actors", actors);
        model.addAttribute("content", "manager/movies");

        return "manager/layout";
    }

    @PostMapping("/movies")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveMovie(
            @RequestParam("name") String name,
            @RequestParam("duration") Integer duration,
            @RequestParam("categories") String categories,
            @RequestParam("language") String language,
            @RequestParam(value = "rated", required = false) String rated,
            @RequestParam(value = "format", required = false) String format,
            @RequestParam("isShowing") Integer isShowing,
            @RequestParam(value = "directorId", required = false) Integer directorId,
            @RequestParam(value = "shortDescription", required = false) String shortDescription,
            @RequestParam(value = "longDescription", required = false) String longDescription,
            @RequestParam(value = "smallImageFile", required = false) MultipartFile smallImageFile,
            @RequestParam(value = "largeImageFile", required = false) MultipartFile largeImageFile,
            @RequestParam(value = "trailerFile", required = false) MultipartFile trailerFile,
            @RequestParam(value = "actors", required = false) String actorsJson,
            @RequestParam(value = "_method", required = false) String method,
            @RequestParam(value = "movieId", required = false) Integer movieId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean isUpdate = "PUT".equals(method) && movieId != null;
            Movie movie;
            
            if (isUpdate) {
                // Update existing movie
                Optional<Movie> movieOpt = movieRepository.findById(movieId);
                if (!movieOpt.isPresent()) {
                    response.put("success", false);
                    response.put("message", "Movie not found");
                    return ResponseEntity.ok(response);
                }
                movie = movieOpt.get();
                
                // Delete existing movie actors for update
                movieActorRepository.deleteByMovie_Id(movieId);
            } else {
                // Create new movie
                movie = new Movie();
                movie.setViews(0);
                movie.setReleaseDate(new Date());
                if (movieId != null) {
                    movie.setEndDate(new Date());
                }
            }
            
            // Set common properties
            movie.setName(name);
            movie.setCategories(categories);
            movie.setDuration(duration);
            movie.setLanguage(language);
            movie.setRated(rated);
            movie.setShortDescription(shortDescription);
            movie.setLongDescription(longDescription);
            movie.setFormat(format != null ? format.trim() : null);
            movie.setIsShowing(isShowing);

            // Handle file uploads (only update if new files provided)
            try {
                String smallImagePath = saveFile(smallImageFile, "images");
                String largeImagePath = saveFile(largeImageFile, "images");
                String trailerPath = saveFile(trailerFile, "videos");
                
                if (smallImagePath != null) {
                    movie.setSmallImageUrl(smallImagePath);
                }
                if (largeImagePath != null) {
                    movie.setLargeImageUrl(largeImagePath);
                }
                if (trailerPath != null) {
                    movie.setTrailerUrlWatchLink(trailerPath);
                }
            } catch (IOException e) {
                response.put("success", false);
                response.put("message", "Error uploading files: " + e.getMessage());
                return ResponseEntity.ok(response);
            }

            // Set director
            if (directorId != null) {
                Optional<Director> director = directorRepository.findById(directorId);
                director.ifPresent(movie::setDirector);
            }

            // Set dates only for new movies
            if (!isUpdate) {
                movie.setReleaseDate(new Date());
            }

            Movie savedMovie = movieRepository.save(movie);

            // Handle actors and characters
            if (actorsJson != null && !actorsJson.trim().isEmpty()) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> actorList = objectMapper.readValue(actorsJson, List.class);
                    System.out.println("Actor list received: " + actorList); // Debug log
                    
                    for (Map<String, Object> actorData : actorList) {
                        System.out.println("Processing actor data: " + actorData); // Debug log
                        
                        if (actorData.get("actorId") != null && actorData.get("characterName") != null) {
                            Integer actorId = Integer.parseInt(actorData.get("actorId").toString());
                            String characterName = (String) actorData.get("characterName");
                            
                            Optional<Actor> actor = actorRepository.findById(actorId);
                            if (actor.isPresent()) {
                                MovieActor movieActor = new MovieActor();
                                movieActor.setMovie(savedMovie);
                                movieActor.setActor(actor.get());
                                movieActor.setNameInMovie(characterName);
                                movieActorRepository.save(movieActor);
                                System.out.println("Saved MovieActor: movie=" + savedMovie.getId() + ", actor=" + actor.get().getId() + ", character=" + characterName);
                            } else {
                                System.out.println("Actor not found with ID: " + actorId);
                            }
                        } else {
                            System.out.println("Missing actorId or characterName in actorData: " + actorData);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing actors JSON: " + e.getMessage());
                }
            } else {
                System.out.println("No actors to save for this movie");
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
                
                response.put("success", true);
                response.put("movie", movie);
                response.put("movieActors", movieActors);
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

    @PutMapping("/movies/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateMovie(@PathVariable Integer id, @RequestBody Map<String, Object> movieData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Movie> movieOpt = movieRepository.findById(id);
            if (movieOpt.isPresent()) {
                Movie movie = movieOpt.get();
                
                movie.setName((String) movieData.get("name"));
                movie.setCategories((String) movieData.get("categories"));
                movie.setDuration(Integer.parseInt(movieData.get("duration").toString()));
                movie.setLanguage((String) movieData.get("language"));
                movie.setRated((String) movieData.get("rated"));
                movie.setShortDescription((String) movieData.get("shortDescription"));
                movie.setLongDescription((String) movieData.get("longDescription"));
                movie.setLargeImageUrl((String) movieData.get("largeImageUrl"));
                movie.setSmallImageUrl((String) movieData.get("smallImageUrl"));
                movie.setTrailerUrlWatchLink((String) movieData.get("trailerUrl"));
                movie.setFormat((String) movieData.get("format"));
                movie.setIsShowing(Integer.parseInt(movieData.get("isShowing").toString()));

                // Update director
                if (movieData.get("directorId") != null) {
                    Integer directorId = Integer.parseInt(movieData.get("directorId").toString());
                    Optional<Director> director = directorRepository.findById(directorId);
                    director.ifPresent(movie::setDirector);
                }

                Movie savedMovie = movieRepository.save(movie);

                // Delete existing movie actors and add new ones
                movieActorRepository.deleteByMovie_Id(id);
                
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> actorList = (List<Map<String, Object>>) movieData.get("actors");
                System.out.println("Update - Actor list received: " + actorList); // Debug log
                
                if (actorList != null && !actorList.isEmpty()) {
                    for (Map<String, Object> actorData : actorList) {
                        System.out.println("Update - Processing actor data: " + actorData); // Debug log
                        
                        if (actorData.get("actorId") != null && actorData.get("characterName") != null) {
                            Integer actorId = Integer.parseInt(actorData.get("actorId").toString());
                            String characterName = (String) actorData.get("characterName");
                            
                            Optional<Actor> actor = actorRepository.findById(actorId);
                            if (actor.isPresent()) {
                                MovieActor movieActor = new MovieActor();
                                movieActor.setMovie(savedMovie);
                                movieActor.setActor(actor.get());
                                movieActor.setNameInMovie(characterName);
                                movieActorRepository.save(movieActor);
                                System.out.println("Update - Saved MovieActor: movie=" + savedMovie.getId() + ", actor=" + actor.get().getId() + ", character=" + characterName);
                            } else {
                                System.out.println("Update - Actor not found with ID: " + actorId);
                            }
                        } else {
                            System.out.println("Update - Missing actorId or characterName in actorData: " + actorData);
                        }
                    }
                } else {
                    System.out.println("Update - No actors to save for this movie");
                }

                response.put("success", true);
                response.put("message", "Movie updated successfully!");
            } else {
                response.put("success", false);
                response.put("message", "Movie not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating movie: " + e.getMessage());
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
                // Delete movie actors first
                movieActorRepository.deleteByMovie_Id(id);
                
                // Delete movie
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

    // Director CRUD endpoints
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
}