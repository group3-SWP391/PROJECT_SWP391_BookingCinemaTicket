package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entities.Branch;
import org.group3.project_swp391_bookingmovieticket.entities.Room;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.services.IBranchService;
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
public class ManagerBranchController {

    @Autowired
    private IBranchService branchService;

    @Autowired
    private IRoomService roomService;

    // Branch Management Pages
    @GetMapping("/branchs")
    public String showBranchsManagement(Model model, HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user == null) {
            return "redirect:/login";
        }

        List<Branch> branchs = branchService.getBranchsWithRooms();
        model.addAttribute("branchs", branchs);
        model.addAttribute("content", "manager/branchs");

        return "manager/layout";
    }

    @GetMapping("/branchs/{id}/rooms")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> showBranchRooms(@PathVariable Integer id, Model model, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Optional<Branch> Branch = branchService.getBranchById(id);
        if (Branch.isEmpty()) {
            response.put("success", false);
            response.put("message", "No branch exists");
            return ResponseEntity.badRequest().body(response);
        }

        List<Room> rooms = roomService.getRoomsByBranchId(id);
        response.put("success", true);
        response.put("message", "Get rooms!");
        response.put("rooms", rooms);
        return ResponseEntity.ok(response);
    }

    // Branch CRUD Operations
    @PostMapping("/branchs")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveBranch(@RequestBody Map<String, String> branchData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validation
            String validationError = validateBranchData(branchData);
            if (validationError != null) {
                response.put("success", false);
                response.put("message", validationError);
                return ResponseEntity.badRequest().body(response);
            }

            Branch Branch = new Branch();
            Branch.setName(branchData.get("name"));
            Branch.setLocation(branchData.get("location"));
            Branch.setPhoneNo(branchData.get("phoneNo"));
            Branch.setDescription(branchData.get("description"));
            Branch savedBranch = branchService.saveBranch(Branch);

            response.put("success", true);
            response.put("message", "Branch added successfully!");
            response.put("Branch", savedBranch);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error saving Branch: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/branchs/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getBranch(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Branch> Branch = branchService.getBranchById(id);
            if (Branch.isPresent()) {
                List<Room> rooms = roomService.getRoomsByBranchId(id);
                Long roomCount = roomService.countRoomsByBranchId(id);
                Long totalCapacity = roomService.getTotalCapacityByBranchId(id);
                
                response.put("success", true);
                response.put("Branch", Branch.get());
                response.put("rooms", rooms);
                response.put("roomCount", roomCount);
                response.put("totalCapacity", totalCapacity);
            } else {
                response.put("success", false);
                response.put("message", "Branch not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving Branch: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/branchs/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateBranch(@PathVariable Integer id, @RequestBody Map<String, String> branchData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Branch> branchOpt = branchService.getBranchById(id);
            if (branchOpt.isPresent()) {
                // Validation
                String validationError = validateBranchData(branchData);
                if (validationError != null) {
                    response.put("success", false);
                    response.put("message", validationError);
                    return ResponseEntity.badRequest().body(response);
                }

                Branch Branch = branchOpt.get();
                Branch.setName(branchData.get("name"));
                Branch.setLocation(branchData.get("location"));
                Branch.setPhoneNo(branchData.get("phoneNo"));
                Branch.setDescription(branchData.get("description"));

                Branch savedBranch = branchService.updateBranch(Branch);

                response.put("success", true);
                response.put("message", "Branch updated successfully!");
                response.put("Branch", savedBranch);
            } else {
                response.put("success", false);
                response.put("message", "Branch not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating Branch: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/branchs/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteBranch(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (branchService.existsById(id)) {
                branchService.deleteBranch(id);
                response.put("success", true);
                response.put("message", "Branch deleted successfully!");
            } else {
                response.put("success", false);
                response.put("message", "Branch not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting Branch: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // Room CRUD Operations
    @PostMapping("/branchs/{branchId}/rooms")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveRoom(@PathVariable Integer branchId, @RequestBody Map<String, Object> roomData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validation
            String validationError = validateRoomData(roomData, branchId);
            if (validationError != null) {
                response.put("success", false);
                response.put("message", validationError);
                return ResponseEntity.badRequest().body(response);
            }

            Optional<Branch> Branch = branchService.getBranchById(branchId);
            if (!Branch.isPresent()) {
                response.put("success", false);
                response.put("message", "Branch not found");
                return ResponseEntity.badRequest().body(response);
            }

            Room room = new Room();
            room.setName((String) roomData.get("name"));
            room.setCapacity(Integer.parseInt(roomData.get("capacity").toString()));
            room.setRoomType((String) roomData.get("roomType"));
            room.setDescription((String) roomData.get("description"));
            room.setBranch(Branch.get());
            
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
                String validationError = validateRoomData(roomData, room.getBranch().getId());
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
    @GetMapping("/api/branchs/active")
    @ResponseBody
    public ResponseEntity<List<Branch>> getActiveBranchs() {
        List<Branch> branchs = branchService.getAllBranchs();
        return ResponseEntity.ok(branchs);
    }

    @GetMapping("/api/rooms/types")
    @ResponseBody
    public ResponseEntity<List<String>> getRoomTypes() {
        List<String> roomTypes = roomService.getAllRoomTypes();
        return ResponseEntity.ok(roomTypes);
    }

    // Validation methods
    private String validateBranchData(Map<String, String> branchData) {
        if (branchData.get("name") == null || branchData.get("name").trim().isEmpty()) {
            return "Branch name is required";
        }
        if (branchData.get("name").length() > 255) {
            return "Branch name cannot exceed 255 characters";
        }
        if (branchData.get("location") != null && branchData.get("location").length() > 500) {
            return "location cannot exceed 500 characters";
        }
        if (branchData.get("phoneNo") != null && branchData.get("phoneNo").length() > 20) {
            return "Phone number cannot exceed 20 characters";
        }
        if (branchData.get("description") != null && branchData.get("description").length() > 1000) {
            return "Description cannot exceed 1000 characters";
        }
        
        return null;
    }

    private String validateRoomData(Map<String, Object> roomData, Integer branchId) {
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
            int capacity = Integer.parseInt(roomData.get("capacity").toString());
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