package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entities.*;
import org.group3.project_swp391_bookingmovieticket.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @PostMapping("/movies/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addMovie(@RequestBody Map<String, Object> movieData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Movie movie = new Movie();
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
            movie.setViews(0);

            // Set director
            if (movieData.get("directorId") != null) {
                Integer directorId = Integer.parseInt(movieData.get("directorId").toString());
                Optional<Director> director = directorRepository.findById(directorId);
                director.ifPresent(movie::setDirector);
            }

            // Set dates
            movie.setReleaseDate(new Date());
            if (movieData.get("endDate") != null) {
                // Parse end date if provided
                movie.setEndDate(new Date());
            }

            Movie savedMovie = movieRepository.save(movie);

            // Handle actors and characters
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> actorList = (List<Map<String, Object>>) movieData.get("actors");
            if (actorList != null) {
                for (Map<String, Object> actorData : actorList) {
                    Integer actorId = Integer.parseInt(actorData.get("actorId").toString());
                    String characterName = (String) actorData.get("characterName");
                    
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

            response.put("success", true);
            response.put("message", "Movie added successfully!");
            response.put("movieId", savedMovie.getId());

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding movie: " + e.getMessage());
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
                if (actorList != null) {
                    for (Map<String, Object> actorData : actorList) {
                        Integer actorId = Integer.parseInt(actorData.get("actorId").toString());
                        String characterName = (String) actorData.get("characterName");
                        
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

    // Director endpoint for JavaScript (matches the JS call)
    @PostMapping("/directors")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createDirector(@RequestBody Map<String, String> directorData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Director director = new Director();
            director.setName(directorData.get("name"));
            director.setDescription(directorData.get("biography")); // Map biography to description field
            
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

    // Actor CRUD endpoints
    @PostMapping("/actors/add")
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

    // Actor endpoint for JavaScript (matches the JS call)
    @PostMapping("/actors")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createActor(@RequestBody Map<String, String> actorData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Actor actor = new Actor();
            actor.setName(actorData.get("name"));
            // Note: Actor entity only has 'name' field, so we ignore biography for now
            
            Actor savedActor = actorRepository.save(actor);
            
            response.put("success", true);
            response.put("message", "Actor created successfully!");
            response.put("actor", savedActor);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating actor: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
} 