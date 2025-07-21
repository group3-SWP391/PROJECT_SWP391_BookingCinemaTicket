package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.*;
import org.group3.project_swp391_bookingmovieticket.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/manager")
public class ManagerScheduleController {

    @Autowired
    private IScheduleRepository scheduleRepository;

    @Autowired
    private IBranchRepository branchRepository;

    @Autowired
    private IMovieRepository movieRepository;

    @Autowired
    private IRoomRepository roomRepository;

    // Schedule Management Pages
    @GetMapping("/schedules")
    public String showSchedulesManagement(Model model, HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user == null) {
            return "redirect:/login";
        }

        List<Schedule> schedules = scheduleRepository.findAll();
        List<Branch> branches = branchRepository.findAll();
        List<Movie> movies = movieRepository.findAll();
        List<Room> rooms = roomRepository.findAll();

        model.addAttribute("schedules", schedules);
        model.addAttribute("branches", branches);
        model.addAttribute("movies", movies);
        model.addAttribute("rooms", rooms);
        model.addAttribute("content", "manager/schedules");

        return "manager/layout";
    }

    // Schedule CRUD Operations
    @PostMapping("/schedules")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveSchedule(@RequestBody Map<String, Object> scheduleData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validation
            String validationError = validateScheduleData(scheduleData);
            if (validationError != null) {
                response.put("success", false);
                response.put("message", validationError);
                return ResponseEntity.badRequest().body(response);
            }

            Schedule schedule = new Schedule();
            schedule.setPrice(Double.parseDouble(scheduleData.get("price").toString()));
            schedule.setStartDate(LocalDate.parse(scheduleData.get("startDate").toString()));
            schedule.setStartTime(LocalTime.parse(scheduleData.get("startTime").toString()));
            schedule.setEndTime(LocalTime.parse(scheduleData.get("endTime").toString()));

            // Set related entity
            Integer branchId = Integer.parseInt(scheduleData.get("branchId").toString());
            Integer movieId = Integer.parseInt(scheduleData.get("movieId").toString());
            Integer roomId = Integer.parseInt(scheduleData.get("roomId").toString());

            Optional<Branch> branch = branchRepository.findById(branchId);
            Optional<Movie> movie = movieRepository.findById(movieId);
            Optional<Room> room = roomRepository.findById(roomId);

            if (!branch.isPresent() || !movie.isPresent() || !room.isPresent()) {
                response.put("success", false);
                response.put("message", "Invalid branch, movie, or room selected");
                return ResponseEntity.badRequest().body(response);
            }

            // Check for time conflicts
            List<Schedule> existingSchedules = scheduleRepository.findSchedulesByRoomAndDate(
                roomId, schedule.getStartDate());
            
            boolean hasConflict = existingSchedules.stream().anyMatch(existing -> 
                isTimeOverlapping(schedule.getStartTime(), schedule.getEndTime(), 
                                existing.getStartTime(), existing.getEndTime()));
            
            if (hasConflict) {
                response.put("success", false);
                response.put("message", "Time conflict detected with existing schedule in this room");
                return ResponseEntity.badRequest().body(response);
            }

            schedule.setBranch(branch.get());
            schedule.setMovie(movie.get());
            schedule.setRoom(room.get());

            Schedule savedSchedule = scheduleRepository.save(schedule);

            // Manually serialize the saved schedule to avoid Hibernate proxy issues
            Map<String, Object> savedScheduleData = new HashMap<>();
            savedScheduleData.put("id", savedSchedule.getId());
            savedScheduleData.put("price", savedSchedule.getPrice());
            savedScheduleData.put("startDate", savedSchedule.getStartDate().toString());
            savedScheduleData.put("startTime", savedSchedule.getStartTime().toString());
            savedScheduleData.put("endTime", savedSchedule.getEndTime().toString());

            response.put("success", true);
            response.put("message", "Schedule added successfully!");
            response.put("schedule", savedScheduleData);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error saving schedule: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/schedules/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSchedule(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Schedule> scheduleOpt = scheduleRepository.findByIdWithDetails(id);
            if (scheduleOpt.isPresent()) {
                Schedule schedule = scheduleOpt.get();
                
                // Create a response object with properly initialized data
                Map<String, Object> scheduleData = new HashMap<>();
                scheduleData.put("id", schedule.getId());
                scheduleData.put("price", schedule.getPrice());
                scheduleData.put("startDate", schedule.getStartDate().toString());
                scheduleData.put("startTime", schedule.getStartTime().toString());
                scheduleData.put("endTime", schedule.getEndTime().toString());
                
                // Manually serialize related entity to avoid lazy loading issues
                Map<String, Object> branchData = new HashMap<>();
                branchData.put("id", schedule.getBranch().getId());
                branchData.put("name", schedule.getBranch().getName());
                scheduleData.put("branch", branchData);
                
                Map<String, Object> movieData = new HashMap<>();
                movieData.put("id", schedule.getMovie().getId());
                movieData.put("name", schedule.getMovie().getName());
                scheduleData.put("movie", movieData);
                
                Map<String, Object> roomData = new HashMap<>();
                roomData.put("id", schedule.getRoom().getId());
                roomData.put("name", schedule.getRoom().getName());
                scheduleData.put("room", roomData);
                
                response.put("success", true);
                response.put("schedule", scheduleData);
            } else {
                response.put("success", false);
                response.put("message", "Schedule not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving schedule: " + e.getMessage());
            e.printStackTrace(); // For debugging
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/schedules/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateSchedule(@PathVariable Integer id, @RequestBody Map<String, Object> scheduleData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Schedule> scheduleOpt = scheduleRepository.findById(id);
            if (scheduleOpt.isPresent()) {
                // Validation
                String validationError = validateScheduleData(scheduleData);
                if (validationError != null) {
                    response.put("success", false);
                    response.put("message", validationError);
                    return ResponseEntity.badRequest().body(response);
                }

                Schedule schedule = scheduleOpt.get();
                schedule.setPrice(Double.parseDouble(scheduleData.get("price").toString()));
                schedule.setStartDate(LocalDate.parse(scheduleData.get("startDate").toString()));
                schedule.setStartTime(LocalTime.parse(scheduleData.get("startTime").toString()));
                schedule.setEndTime(LocalTime.parse(scheduleData.get("endTime").toString()));

                // Set related entity
                Integer branchId = Integer.parseInt(scheduleData.get("branchId").toString());
                Integer movieId = Integer.parseInt(scheduleData.get("movieId").toString());
                Integer roomId = Integer.parseInt(scheduleData.get("roomId").toString());

                Optional<Branch> branch = branchRepository.findById(branchId);
                Optional<Movie> movie = movieRepository.findById(movieId);
                Optional<Room> room = roomRepository.findById(roomId);

                if (!branch.isPresent() || !movie.isPresent() || !room.isPresent()) {
                    response.put("success", false);
                    response.put("message", "Invalid branch, movie, or room selected");
                    return ResponseEntity.badRequest().body(response);
                }

                // Check for time conflicts (excluding current schedule)
                List<Schedule> existingSchedules = scheduleRepository.findSchedulesByRoomAndDate(
                    roomId, schedule.getStartDate());
                
                boolean hasConflict = existingSchedules.stream()
                        .filter(s -> !s.getId().equals(id))
                        .anyMatch(existing -> 
                            isTimeOverlapping(schedule.getStartTime(), schedule.getEndTime(), 
                                            existing.getStartTime(), existing.getEndTime()));
                
                if (hasConflict) {
                    response.put("success", false);
                    response.put("message", "Time conflict detected with existing schedule in this room");
                    return ResponseEntity.badRequest().body(response);
                }

                schedule.setBranch(branch.get());
                schedule.setMovie(movie.get());
                schedule.setRoom(room.get());

                Schedule savedSchedule = scheduleRepository.save(schedule);

                // Manually serialize the saved schedule to avoid Hibernate proxy issues
                Map<String, Object> savedScheduleData = new HashMap<>();
                savedScheduleData.put("id", savedSchedule.getId());
                savedScheduleData.put("price", savedSchedule.getPrice());
                savedScheduleData.put("startDate", savedSchedule.getStartDate().toString());
                savedScheduleData.put("startTime", savedSchedule.getStartTime().toString());
                savedScheduleData.put("endTime", savedSchedule.getEndTime().toString());

                response.put("success", true);
                response.put("message", "Schedule updated successfully!");
                response.put("schedule", savedScheduleData);
            } else {
                response.put("success", false);
                response.put("message", "Schedule not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating schedule: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/schedules/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteSchedule(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (scheduleRepository.existsById(id)) {
                scheduleRepository.deleteById(id);
                response.put("success", true);
                response.put("message", "Schedule deleted successfully!");
            } else {
                response.put("success", false);
                response.put("message", "Schedule not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting schedule: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // Additional API endpoints
    @GetMapping("/api/schedules/branch/{branchId}")
    @ResponseBody
    public ResponseEntity<List<Schedule>> getSchedulesByBranch(@PathVariable Integer branchId) {
        List<Schedule> schedules = scheduleRepository.findByBranchId(branchId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/api/schedules/movie/{movieId}")
    @ResponseBody
    public ResponseEntity<List<Schedule>> getSchedulesByMovie(@PathVariable Integer movieId) {
        List<Schedule> schedules = scheduleRepository.findByMovieId(movieId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/api/schedules/room/{roomId}")
    @ResponseBody
    public ResponseEntity<List<Schedule>> getSchedulesByRoom(@PathVariable Integer roomId) {
        List<Schedule> schedules = scheduleRepository.findByRoomId(roomId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/api/rooms/branch/{branchId}")
    @ResponseBody
    public ResponseEntity<List<Room>> getRoomsByBranch(@PathVariable Integer branchId) {
        List<Room> rooms = roomRepository.findByBranchId(branchId);
        return ResponseEntity.ok(rooms);
    }

    // Helper method to check if two time ranges overlap
    private boolean isTimeOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        // Two time ranges overlap if they don't NOT overlap
        // They don't overlap if one ends before the other starts
        return !(end1.isBefore(start2) || end1.equals(start2) || start1.isAfter(end2) || start1.equals(end2));
    }

    // Validation methods
    private String validateScheduleData(Map<String, Object> scheduleData) {
        if (scheduleData.get("price") == null) {
            return "Price is required";
        }
        
        try {
            double price = Double.parseDouble(scheduleData.get("price").toString());
            if (price <= 0) {
                return "Price must be greater than 0";
            }
        } catch (NumberFormatException e) {
            return "Invalid price format";
        }

        if (scheduleData.get("startDate") == null || scheduleData.get("startDate").toString().trim().isEmpty()) {
            return "Start date is required";
        }

        if (scheduleData.get("startTime") == null || scheduleData.get("startTime").toString().trim().isEmpty()) {
            return "Start time is required";
        }

        if (scheduleData.get("endTime") == null || scheduleData.get("endTime").toString().trim().isEmpty()) {
            return "End time is required";
        }

        try {
            LocalTime startTime = LocalTime.parse(scheduleData.get("startTime").toString());
            LocalTime endTime = LocalTime.parse(scheduleData.get("endTime").toString());
            
            if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
                return "End time must be after start time";
            }
        } catch (Exception e) {
            return "Invalid time format";
        }

        if (scheduleData.get("branchId") == null) {
            return "Branch is required";
        }

        if (scheduleData.get("movieId") == null) {
            return "Movie is required";
        }

        if (scheduleData.get("roomId") == null) {
            return "Room is required";
        }

        return null;
    }
} 