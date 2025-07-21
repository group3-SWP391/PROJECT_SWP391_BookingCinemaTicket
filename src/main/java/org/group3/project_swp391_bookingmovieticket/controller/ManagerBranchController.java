package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.Branch;
import org.group3.project_swp391_bookingmovieticket.entity.Room;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.service.IBranchService;
import org.group3.project_swp391_bookingmovieticket.service.IRoomService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/manager")
public class ManagerBranchController {

    @Autowired
    private IBranchService branchService;

    @Autowired
    private IRoomService roomService;

    private static final String UPLOAD_DIR = "uploads/branchs/";
    
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
    public ResponseEntity<Map<String, Object>> saveBranch(
            @RequestParam("name") String name,
            @RequestParam("location") String location,
            @RequestParam("phoneNo") String phoneNo,
            @RequestParam("description") String description,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "imgUrl", required = false) String imgUrl) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Create validation data map
            Map<String, String> branchData = new HashMap<>();
            branchData.put("name", name);
            branchData.put("location", location);
            branchData.put("phoneNo", phoneNo);
            branchData.put("description", description);
            
            // Validation
            String validationError = validateBranchData(branchData);
            if (validationError != null) {
                response.put("success", false);
                response.put("message", validationError);
                return ResponseEntity.badRequest().body(response);
            }

            Branch branch = new Branch();
            branch.setName(name);
            branch.setLocation(location);
            branch.setPhoneNo(phoneNo);
            branch.setDescription(description);
            
            // Handle image upload
            try {
                String imagePath = saveFile(imageFile, "images");
                
                if (imagePath != null) {
                    branch.setImgUrl(imagePath);
                }
            } catch (IOException e) {
                response.put("success", false);
                response.put("message", "Error uploading file: " + e.getMessage());
                return ResponseEntity.ok(response);
            }
            
            Branch savedBranch = branchService.saveBranch(branch);

            response.put("success", true);
            response.put("message", "Branch added successfully!");
            response.put("branch", savedBranch);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error saving branch: " + e.getMessage());
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
    public ResponseEntity<Map<String, Object>> updateBranch(
            @PathVariable Integer id,
            @RequestParam("name") String name,
            @RequestParam("location") String location,
            @RequestParam("phoneNo") String phoneNo,
            @RequestParam("description") String description,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "imgUrl", required = false) String imgUrl) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Branch> branchOpt = branchService.getBranchById(id);
            if (branchOpt.isPresent()) {
                // Create validation data map
                Map<String, String> branchData = new HashMap<>();
                branchData.put("name", name);
                branchData.put("location", location);
                branchData.put("phoneNo", phoneNo);
                branchData.put("description", description);
                
                // Validation
                String validationError = validateBranchData(branchData);
                if (validationError != null) {
                    response.put("success", false);
                    response.put("message", validationError);
                    return ResponseEntity.badRequest().body(response);
                }

                Branch branch = branchOpt.get();
                branch.setName(name);
                branch.setLocation(location);
                branch.setPhoneNo(phoneNo);
                branch.setDescription(description);

                // Handle image upload
                try {
                    String imagePath = saveFile(imageFile, "images");
                    
                    if (imagePath != null) {
                        branch.setImgUrl(imagePath);
                    } else {
                        // Keep existing image if no new file uploaded
                        branch.setImgUrl(imgUrl);
                    }
                } catch (IOException e) {
                    response.put("success", false);
                    response.put("message", "Error uploading file: " + e.getMessage());
                    return ResponseEntity.ok(response);
                }

                Branch savedBranch = branchService.updateBranch(branch);

                response.put("success", true);
                response.put("message", "Branch updated successfully!");
                response.put("branch", savedBranch);
            } else {
                response.put("success", false);
                response.put("message", "Branch not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating branch: " + e.getMessage());
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
            
            // Get capacity and row count from user input
            int capacity = Integer.parseInt(roomData.get("capacity").toString());
            int rowCount = Integer.parseInt(roomData.get("rowCount").toString());
            
            // Auto-calculate seats per row
            int seatsPerRow = capacity / rowCount;
            int extraSeats = capacity % rowCount;
            
            room.setCapacity(capacity);
            room.setRowCount(rowCount);
            room.setSeatsPerRow(seatsPerRow); // Calculated value
            room.setRoomType((String) roomData.get("roomType"));
            room.setDescription((String) roomData.get("description"));
            room.setBranch(Branch.get());
            
            // Handle VIP seats data
            if (roomData.get("vipSeats") != null) {
                String vipSeats = roomData.get("vipSeats").toString();
                room.setVipSeats(vipSeats);
            } else {
                room.setVipSeats(""); // Default to no VIP seats
            }
            
            // Add isActive status
            if (roomData.get("isActive") != null) {
                room.setIsActive(Integer.parseInt(roomData.get("isActive").toString()));
            } else {
                room.setIsActive(1); // Default to active
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
                
                // Get capacity and row count from user input
                int capacity = Integer.parseInt(roomData.get("capacity").toString());
                int rowCount = Integer.parseInt(roomData.get("rowCount").toString());
                
                // Auto-calculate seats per row
                int seatsPerRow = capacity / rowCount;
                int extraSeats = capacity % rowCount;
                
                room.setCapacity(capacity);
                room.setRowCount(rowCount);
                room.setSeatsPerRow(seatsPerRow); // Calculated value
                room.setRoomType((String) roomData.get("roomType"));
                room.setDescription((String) roomData.get("description"));
                
                // Handle VIP seats data
                if (roomData.get("vipSeats") != null) {
                    String vipSeats = roomData.get("vipSeats").toString();
                    room.setVipSeats(vipSeats);
                } else {
                    room.setVipSeats(""); // Default to no VIP seats
                }
                
                // Update isActive status
                if (roomData.get("isActive") != null) {
                    room.setIsActive(Integer.parseInt(roomData.get("isActive").toString()));
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
    
    @PostMapping("/api/rooms/preview-layout")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> generateSeatLayoutPreview(@RequestBody Map<String, Object> layoutData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            int capacity = Integer.parseInt(layoutData.get("capacity").toString());
            int rowCount = Integer.parseInt(layoutData.get("rowCount").toString());
            
            if (capacity <= 0 || rowCount <= 0) {
                response.put("success", false);
                response.put("message", "Invalid capacity or row count");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (capacity < rowCount) {
                response.put("success", false);
                response.put("message", "Capacity must be at least equal to row count");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Calculate seat layout
            int seatsPerRow = capacity / rowCount;
            int extraSeats = capacity % rowCount;
            
            // Generate layout data
            Map<String, Object> layoutInfo = new HashMap<>();
            layoutInfo.put("capacity", capacity);
            layoutInfo.put("rowCount", rowCount);
            layoutInfo.put("seatsPerRow", seatsPerRow);
            layoutInfo.put("extraSeats", extraSeats);
            layoutInfo.put("layout", generateSeatLayout(capacity, rowCount, seatsPerRow, extraSeats));
            
            response.put("success", true);
            response.put("layout", layoutInfo);
            
        } catch (NumberFormatException e) {
            response.put("success", false);
            response.put("message", "Invalid input values");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error generating layout: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    private List<Map<String, Object>> generateSeatLayout(int capacity, int rowCount, int seatsPerRow, int extraSeats) {
        List<Map<String, Object>> layout = new ArrayList<>();
        int seatCounter = 1;
        
        for (int row = 0; row < rowCount; row++) {
            Map<String, Object> rowData = new HashMap<>();
            char rowLetter = (char) ('A' + row);
            
            // Calculate seats in this row (extra seats go to first few rows)
            int seatsInThisRow = seatsPerRow + (row < extraSeats ? 1 : 0);
            
            List<Map<String, Object>> seats = new ArrayList<>();
            for (int seat = 1; seat <= seatsInThisRow; seat++) {
                Map<String, Object> seatData = new HashMap<>();
                seatData.put("id", rowLetter + String.valueOf(seat));
                seatData.put("row", String.valueOf(rowLetter));
                seatData.put("number", seat);
                seatData.put("type", "REGULAR"); // Default type
                seatData.put("available", true);
                seats.add(seatData);
            }
            
            rowData.put("rowId", String.valueOf(rowLetter));
            rowData.put("rowNumber", row + 1);
            rowData.put("seats", seats);
            rowData.put("seatCount", seatsInThisRow);
            
            layout.add(rowData);
        }
        
        return layout;
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
        
        // Validate row count (now required)
        if (roomData.get("rowCount") == null) {
            return "Row count is required";
        }
        
        try {
            int rowCount = Integer.parseInt(roomData.get("rowCount").toString());
            if (rowCount <= 0) {
                return "Row count must be greater than 0";
            }
            
            int capacity = Integer.parseInt(roomData.get("capacity").toString());
            if (capacity < rowCount) {
                return "Capacity must be at least equal to row count";
            }
        } catch (NumberFormatException e) {
            return "Invalid row count value";
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