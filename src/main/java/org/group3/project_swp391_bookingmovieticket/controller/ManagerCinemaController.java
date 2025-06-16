package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entities.Cinema;
import org.group3.project_swp391_bookingmovieticket.entities.Room;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.services.ICinemaService;
import org.group3.project_swp391_bookingmovieticket.services.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/manager")
public class ManagerCinemaController {

    @Autowired
    private ICinemaService cinemaService;

    @Autowired
    private IRoomService roomService;

    // Cinema Management Pages
    @GetMapping("/cinemas")
    public String showCinemasManagement(Model model, HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user == null) {
            return "redirect:/login";
        }

        List<Cinema> cinemas = cinemaService.getCinemasWithRooms();
        model.addAttribute("cinemas", cinemas);
        model.addAttribute("content", "manager/cinemas");

        return "manager/layout";
    }

    @GetMapping("/cinemas/{id}/rooms")
    public String showCinemaRooms(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user == null) {
            return "redirect:/login";
        }

        Optional<Cinema> cinema = cinemaService.getCinemaById(id);
        if (!cinema.isPresent()) {
            return "redirect:/manager/cinemas";
        }

        List<Room> rooms = roomService.getRoomsByCinemaId(id);
        model.addAttribute("cinema", cinema.get());
        model.addAttribute("rooms", rooms);
        model.addAttribute("content", "manager/cinema-rooms");

        return "manager/layout";
    }

    // Cinema CRUD Operations
    @PostMapping("/cinemas")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveCinema(@RequestBody Map<String, String> cinemaData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validation
            String validationError = validateCinemaData(cinemaData);
            if (validationError != null) {
                response.put("success", false);
                response.put("message", validationError);
                return ResponseEntity.badRequest().body(response);
            }

            Cinema cinema = new Cinema();
            cinema.setName(cinemaData.get("name"));
            cinema.setAddress(cinemaData.get("address"));
            cinema.setPhone(cinemaData.get("phone"));
            cinema.setEmail(cinemaData.get("email"));
            cinema.setDescription(cinemaData.get("description"));
            cinema.setIsActive(1);

            Cinema savedCinema = cinemaService.saveCinema(cinema);

            response.put("success", true);
            response.put("message", "Cinema added successfully!");
            response.put("cinema", savedCinema);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error saving cinema: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/cinemas/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCinema(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Cinema> cinema = cinemaService.getCinemaById(id);
            if (cinema.isPresent()) {
                List<Room> rooms = roomService.getRoomsByCinemaId(id);
                Long roomCount = roomService.countRoomsByCinemaId(id);
                Long totalCapacity = roomService.getTotalCapacityByCinemaId(id);
                
                response.put("success", true);
                response.put("cinema", cinema.get());
                response.put("rooms", rooms);
                response.put("roomCount", roomCount);
                response.put("totalCapacity", totalCapacity);
            } else {
                response.put("success", false);
                response.put("message", "Cinema not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving cinema: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/cinemas/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateCinema(@PathVariable Integer id, @RequestBody Map<String, String> cinemaData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Cinema> cinemaOpt = cinemaService.getCinemaById(id);
            if (cinemaOpt.isPresent()) {
                // Validation
                String validationError = validateCinemaData(cinemaData);
                if (validationError != null) {
                    response.put("success", false);
                    response.put("message", validationError);
                    return ResponseEntity.badRequest().body(response);
                }

                Cinema cinema = cinemaOpt.get();
                cinema.setName(cinemaData.get("name"));
                cinema.setAddress(cinemaData.get("address"));
                cinema.setPhone(cinemaData.get("phone"));
                cinema.setEmail(cinemaData.get("email"));
                cinema.setDescription(cinemaData.get("description"));

                Cinema savedCinema = cinemaService.updateCinema(cinema);

                response.put("success", true);
                response.put("message", "Cinema updated successfully!");
                response.put("cinema", savedCinema);
            } else {
                response.put("success", false);
                response.put("message", "Cinema not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating cinema: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cinemas/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteCinema(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (cinemaService.existsById(id)) {
                cinemaService.deleteCinema(id);
                response.put("success", true);
                response.put("message", "Cinema deleted successfully!");
            } else {
                response.put("success", false);
                response.put("message", "Cinema not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting cinema: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // Room CRUD Operations
    @PostMapping("/cinemas/{cinemaId}/rooms")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveRoom(@PathVariable Integer cinemaId, @RequestBody Map<String, Object> roomData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validation
            String validationError = validateRoomData(roomData, cinemaId);
            if (validationError != null) {
                response.put("success", false);
                response.put("message", validationError);
                return ResponseEntity.badRequest().body(response);
            }

            Optional<Cinema> cinema = cinemaService.getCinemaById(cinemaId);
            if (!cinema.isPresent()) {
                response.put("success", false);
                response.put("message", "Cinema not found");
                return ResponseEntity.badRequest().body(response);
            }

            Room room = new Room();
            room.setName((String) roomData.get("name"));
            room.setCapacity(Integer.parseInt(roomData.get("capacity").toString()));
            room.setRoomType((String) roomData.get("roomType"));
            room.setDescription((String) roomData.get("description"));
            room.setCinema(cinema.get());
            
            if (roomData.get("rowCount") != null) {
                room.setRowCount(Integer.parseInt(roomData.get("rowCount").toString()));
            }
            if (roomData.get("seatsPerRow") != null) {
                room.setSeatsPerRow(Integer.parseInt(roomData.get("seatsPerRow").toString()));
            }

            Room savedRoom = roomService.saveRoom(room);

            response.put("success", true);
            response.put("message", "Room added successfully!");
            response.put("room", savedRoom);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error saving room: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/rooms/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getRoom(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Room> room = roomService.getRoomById(id);
            if (room.isPresent()) {
                response.put("success", true);
                response.put("room", room.get());
            } else {
                response.put("success", false);
                response.put("message", "Room not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving room: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/rooms/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateRoom(@PathVariable Integer id, @RequestBody Map<String, Object> roomData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Room> roomOpt = roomService.getRoomById(id);
            if (roomOpt.isPresent()) {
                Room room = roomOpt.get();
                
                // Validation
                String validationError = validateRoomData(roomData, room.getCinema().getId());
                if (validationError != null) {
                    response.put("success", false);
                    response.put("message", validationError);
                    return ResponseEntity.badRequest().body(response);
                }

                room.setName((String) roomData.get("name"));
                room.setCapacity(Integer.parseInt(roomData.get("capacity").toString()));
                room.setRoomType((String) roomData.get("roomType"));
                room.setDescription((String) roomData.get("description"));
                
                if (roomData.get("rowCount") != null) {
                    room.setRowCount(Integer.parseInt(roomData.get("rowCount").toString()));
                }
                if (roomData.get("seatsPerRow") != null) {
                    room.setSeatsPerRow(Integer.parseInt(roomData.get("seatsPerRow").toString()));
                }

                Room savedRoom = roomService.updateRoom(room);

                response.put("success", true);
                response.put("message", "Room updated successfully!");
                response.put("room", savedRoom);
            } else {
                response.put("success", false);
                response.put("message", "Room not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating room: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/rooms/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRoom(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (roomService.existsById(id)) {
                roomService.deleteRoom(id);
                response.put("success", true);
                response.put("message", "Room deleted successfully!");
            } else {
                response.put("success", false);
                response.put("message", "Room not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting room: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // Additional API endpoints
    @GetMapping("/api/cinemas/active")
    @ResponseBody
    public ResponseEntity<List<Cinema>> getActiveCinemas() {
        List<Cinema> cinemas = cinemaService.getActiveCinemas();
        return ResponseEntity.ok(cinemas);
    }

    @GetMapping("/api/rooms/types")
    @ResponseBody
    public ResponseEntity<List<String>> getRoomTypes() {
        List<String> roomTypes = roomService.getAllRoomTypes();
        return ResponseEntity.ok(roomTypes);
    }

    // Validation methods
    private String validateCinemaData(Map<String, String> cinemaData) {
        if (cinemaData.get("name") == null || cinemaData.get("name").trim().isEmpty()) {
            return "Cinema name is required";
        }
        if (cinemaData.get("name").length() > 255) {
            return "Cinema name cannot exceed 255 characters";
        }
        if (cinemaData.get("address") != null && cinemaData.get("address").length() > 500) {
            return "Address cannot exceed 500 characters";
        }
        if (cinemaData.get("phone") != null && cinemaData.get("phone").length() > 20) {
            return "Phone number cannot exceed 20 characters";
        }
        if (cinemaData.get("email") != null && cinemaData.get("email").length() > 100) {
            return "Email cannot exceed 100 characters";
        }
        if (cinemaData.get("description") != null && cinemaData.get("description").length() > 1000) {
            return "Description cannot exceed 1000 characters";
        }
        
        return null;
    }

    private String validateRoomData(Map<String, Object> roomData, Integer cinemaId) {
        if (roomData.get("name") == null || roomData.get("name").toString().trim().isEmpty()) {
            return "Room name is required";
        }
        if (roomData.get("name").toString().length() > 100) {
            return "Room name cannot exceed 100 characters";
        }
        if (roomData.get("capacity") == null) {
            return "Room capacity is required";
        }
        
        try {
            Integer capacity = Integer.parseInt(roomData.get("capacity").toString());
            if (capacity <= 0) {
                return "Room capacity must be greater than 0";
            }
        } catch (NumberFormatException e) {
            return "Invalid capacity value";
        }
        
        if (roomData.get("roomType") != null && roomData.get("roomType").toString().length() > 50) {
            return "Room type cannot exceed 50 characters";
        }
        if (roomData.get("description") != null && roomData.get("description").toString().length() > 500) {
            return "Description cannot exceed 500 characters";
        }
        
        return null;
    }
} 