package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.Branch;
import org.group3.project_swp391_bookingmovieticket.entity.Room;
import org.group3.project_swp391_bookingmovieticket.entity.Seat;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.service.IBranchService;
import org.group3.project_swp391_bookingmovieticket.service.IRoomService;
import org.group3.project_swp391_bookingmovieticket.service.ISeatService;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/manager")
public class ManagerBranchController {

    @Autowired
    private IBranchService branchService;

    @Autowired
    private IRoomService roomService;

    @Autowired
    private ISeatService seatService;

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
        response.put("message", "Lấy danh sách phòng thành công!");
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
            @RequestParam(value = "locationDetail", required = false) String locationDetail,
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
            branchData.put("locationDetail", locationDetail);
            
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
            branch.setLocationDetail(locationDetail);
            
            // Handle image upload
            try {
                String imagePath = saveFile(imageFile, "images");
                
                if (imagePath != null) {
                    branch.setImgUrl(imagePath);
                }
            } catch (IOException e) {
                response.put("success", false);
                response.put("message", "Lỗi khi tải tệp lên: " + e.getMessage());
                return ResponseEntity.ok(response);
            }
            
            Branch savedBranch = branchService.saveBranch(branch);

            response.put("success", true);
            response.put("message", "Thêm chi nhánh thành công!");
            response.put("branch", savedBranch);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi lưu chi nhánh: " + e.getMessage());
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
                response.put("message", "Không tìm thấy chi nhánh");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi lấy thông tin chi nhánh: " + e.getMessage());
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
            @RequestParam(value = "locationDetail", required = false) String locationDetail,
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
                branchData.put("locationDetail", locationDetail);
                
                // Validation (exclude current branch from duplicate check)
                String validationError = validateBranchData(branchData, id);
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
                branch.setLocationDetail(locationDetail);

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
                    response.put("message", "Lỗi khi tải tệp lên: " + e.getMessage());
                    return ResponseEntity.ok(response);
                }

                Branch savedBranch = branchService.updateBranch(branch);

                response.put("success", true);
                response.put("message", "Cập nhật chi nhánh thành công!");
                response.put("branch", savedBranch);
            } else {
                response.put("success", false);
                response.put("message", "Không tìm thấy chi nhánh");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi cập nhật chi nhánh: " + e.getMessage());
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
                response.put("message", "Xóa chi nhánh thành công!");
            } else {
                response.put("success", false);
                response.put("message", "Không tìm thấy chi nhánh");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi xóa chi nhánh: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // Room CRUD Operations
    @PostMapping("/branchs/{branchId}/rooms")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveRoom(
            @PathVariable Integer branchId, 
            @RequestParam(value = "name") String name,
            @RequestParam(value = "roomType", required = false) String roomType,
            @RequestParam(value = "capacity") String capacity,
            @RequestParam(value = "rowCount") String rowCount,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "vipSeats", required = false) String vipSeats,
            @RequestParam(value = "isActive", required = false) String isActive,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "imgUrl", required = false) String imgUrl) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Create validation data map
            Map<String, Object> roomData = new HashMap<>();
            roomData.put("name", name);
            roomData.put("capacity", capacity);
            roomData.put("rowCount", rowCount);
            roomData.put("roomType", roomType);
            roomData.put("description", description);
            
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
                response.put("message", "Không tìm thấy chi nhánh");
                return ResponseEntity.badRequest().body(response);
            }

            Room room = new Room();
            room.setName(name);
            
            // Get capacity and row count from user input
            int capacityInt = Integer.parseInt(capacity);
            int rowCountInt = Integer.parseInt(rowCount);
            
            // Auto-calculate seats per row
            int seatsPerRow = capacityInt / rowCountInt;
            
            room.setCapacity(capacityInt);
            room.setRowCount(rowCountInt);
            room.setSeatsPerRow(seatsPerRow); // Calculated value
            room.setRoomType(roomType);
            room.setDescription(description);
            room.setBranch(Branch.get());
            
            // Handle VIP seats data
            if (vipSeats != null && !vipSeats.trim().isEmpty()) {
                room.setVipSeats(vipSeats);
            } else {
                room.setVipSeats(""); // Default to no VIP seats
            }
            
            // Add isActive status
            if (isActive != null) {
                room.setIsActive(Integer.parseInt(isActive));
            } else {
                room.setIsActive(1); // Default to active
            }

            // Handle image upload
            try {
                String imagePath = saveFile(imageFile, "rooms");
                if (imagePath != null) {
                    room.setImgUrl(imagePath);
                }
            } catch (IOException e) {
                response.put("success", false);
                response.put("message", "Lỗi khi tải tệp lên: " + e.getMessage());
                return ResponseEntity.ok(response);
            }

            Room savedRoom = roomService.saveRoom(room);

            // Generate seats for the room with VIP marking
            seatService.generateSeatsForRoom(savedRoom, vipSeats);

            response.put("success", true);
            response.put("message", "Thêm phòng và ghế ngồi thành công!");
            response.put("room", savedRoom);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi lưu phòng: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/rooms/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getRoom(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Room> roomOpt = roomService.getRoomById(id);
            if (roomOpt.isPresent()) {
                Room room = roomOpt.get();
                
                // Get VIP seats for this room and reconstruct the comma-separated string
                List<Seat> vipSeats = seatService.getVipSeatsByRoomId(id);
                String vipSeatsString = vipSeats.stream()
                        .map(Seat::getName)
                        .collect(Collectors.joining(","));
                
                // Create room data with VIP seats
                Map<String, Object> roomData = new HashMap<>();
                roomData.put("id", room.getId());
                roomData.put("name", room.getName());
                roomData.put("roomType", room.getRoomType());
                roomData.put("capacity", room.getCapacity());
                roomData.put("rowCount", room.getRowCount());
                roomData.put("description", room.getDescription());
                roomData.put("isActive", room.getIsActive());
                roomData.put("vipSeats", vipSeatsString);
                roomData.put("imgUrl", room.getImgUrl());
                
                response.put("success", true);
                response.put("room", roomData);
            } else {
                response.put("success", false);
                response.put("message", "Không tìm thấy phòng");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi lấy thông tin phòng: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/rooms/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateRoom(
            @PathVariable Integer id,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "roomType", required = false) String roomType,
            @RequestParam(value = "capacity") String capacity,
            @RequestParam(value = "rowCount") String rowCount,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "vipSeats", required = false) String vipSeats,
            @RequestParam(value = "isActive", required = false) String isActive,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "imgUrl", required = false) String imgUrl) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Room> roomOpt = roomService.getRoomById(id);
            if (roomOpt.isPresent()) {
                Room room = roomOpt.get();
                
                // Create validation data map
                Map<String, Object> roomData = new HashMap<>();
                roomData.put("name", name);
                roomData.put("capacity", capacity);
                roomData.put("rowCount", rowCount);
                roomData.put("roomType", roomType);
                roomData.put("description", description);
                
                // Validation
                String validationError = validateRoomData(roomData, room.getBranch().getId());
                if (validationError != null) {
                    response.put("success", false);
                    response.put("message", validationError);
                    return ResponseEntity.badRequest().body(response);
                }

                room.setName(name);
                
                // Get capacity and row count from user input
                int capacityInt = Integer.parseInt(capacity);
                int rowCountInt = Integer.parseInt(rowCount);
                
                // Auto-calculate seats per row
                int seatsPerRow = capacityInt / rowCountInt;
                
                room.setCapacity(capacityInt);
                room.setRowCount(rowCountInt);
                room.setSeatsPerRow(seatsPerRow); // Calculated value
                room.setRoomType(roomType);
                room.setDescription(description);
                
                // Handle VIP seats data
                if (vipSeats != null && !vipSeats.trim().isEmpty()) {
                    room.setVipSeats(vipSeats);
                } else {
                    room.setVipSeats(""); // Default to no VIP seats
                }
                
                // Update isActive status
                if (isActive != null) {
                    room.setIsActive(Integer.parseInt(isActive));
                }

                // Handle image upload
                try {
                    String imagePath = saveFile(imageFile, "rooms");
                    if (imagePath != null) {
                        room.setImgUrl(imagePath);
                    } else {
                        // Keep existing image if no new file uploaded
                        room.setImgUrl(imgUrl);
                    }
                } catch (IOException e) {
                    response.put("success", false);
                    response.put("message", "Lỗi khi tải tệp lên: " + e.getMessage());
                    return ResponseEntity.ok(response);
                }

                Room savedRoom = roomService.updateRoom(room);

                // Update seats for the room with new VIP marking (preserves existing seats with tickets)
                seatService.updateSeatsForRoom(savedRoom, vipSeats);

                response.put("success", true);
                response.put("message", "Cập nhật phòng và ghế ngồi thành công!");
                response.put("room", savedRoom);
            } else {
                response.put("success", false);
                response.put("message", "Không tìm thấy phòng");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi cập nhật phòng: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/rooms/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRoom(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (roomService.existsById(id)) {
                // Delete seats first
                seatService.deleteSeatsByRoomId(id);
                // Then delete room
                roomService.deleteRoom(id);
                response.put("success", true);
                response.put("message", "Xóa phòng và ghế ngồi thành công!");
            } else {
                response.put("success", false);
                response.put("message", "Không tìm thấy phòng");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi xóa phòng: " + e.getMessage());
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
    
    @PostMapping("/api/branchs/check-name")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkBranchNameDuplicate(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String branchName = (String) requestData.get("name");
            Object excludeIdObj = requestData.get("excludeId");
            Integer excludeId = null;
            
            if (excludeIdObj != null && !excludeIdObj.toString().trim().isEmpty()) {
                excludeId = Integer.parseInt(excludeIdObj.toString());
            }
            
            if (branchName == null || branchName.trim().isEmpty()) {
                response.put("exists", false);
                return ResponseEntity.ok(response);
            }
            
            Optional<Branch> existingBranch = branchService.getBranchByName(branchName.trim());
            boolean exists = existingBranch.isPresent() && 
                           (excludeId == null || !existingBranch.get().getId().equals(excludeId));
            
            response.put("exists", exists);
            if (exists) {
                response.put("message", "Tên chi nhánh đã tồn tại");
            }
            
        } catch (Exception e) {
            response.put("exists", false);
            response.put("error", "Lỗi khi kiểm tra tên chi nhánh: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/rooms/types")
    @ResponseBody
    public ResponseEntity<List<String>> getRoomTypes() {
        List<String> roomTypes = roomService.getAllRoomTypes();
        return ResponseEntity.ok(roomTypes);
    }
    
    @PostMapping("/api/rooms/validate-vip-seats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> validateVipSeats(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            int capacity = Integer.parseInt(requestData.get("capacity").toString());
            int rowCount = Integer.parseInt(requestData.get("rowCount").toString());
            String vipSeatsInput = (String) requestData.get("vipSeats");
            
            // Generate all possible seat names
            List<String> allSeatNames = seatService.generateAllSeatNames(capacity, rowCount);
            
            // Parse VIP seats input
            List<String> vipSeatNames = seatService.parseVipSeats(vipSeatsInput);
            
            // Validate VIP seats
            List<String> invalidSeats = vipSeatNames.stream()
                    .filter(seatName -> !allSeatNames.contains(seatName))
                    .collect(Collectors.toList());
            
            if (!invalidSeats.isEmpty()) {
                response.put("success", false);
                response.put("message", "Ghế VIP không hợp lệ: " + String.join(", ", invalidSeats));
                response.put("invalidSeats", invalidSeats);
            } else {
                response.put("success", true);
                response.put("message", "All VIP seats are valid");
                response.put("validVipSeats", vipSeatNames);
                response.put("totalSeats", allSeatNames.size());
                response.put("vipSeatsCount", vipSeatNames.size());
            }
            
        } catch (NumberFormatException e) {
            response.put("success", false);
            response.put("message", "Sức chứa hoặc số hàng không hợp lệ");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi kiểm tra ghế VIP: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    // Validation methods
    private String validateBranchData(Map<String, String> branchData) {
        return validateBranchData(branchData, null);
    }
    
    private String validateBranchData(Map<String, String> branchData, Integer excludeBranchId) {
        if (branchData.get("name") == null || branchData.get("name").trim().isEmpty()) {
            return "Tên chi nhánh là bắt buộc";
        }
        if (branchData.get("name").length() > 255) {
            return "Tên chi nhánh không được vượt quá 255 ký tự";
        }
        
        // Check for duplicate branch name
        String branchName = branchData.get("name").trim();
        Optional<Branch> existingBranch = branchService.getBranchByName(branchName);
        if (existingBranch.isPresent()) {
            // If we're updating and it's not the same branch, it's a duplicate
            if (excludeBranchId == null || !existingBranch.get().getId().equals(excludeBranchId)) {
                return "Tên chi nhánh '" + branchName + "' đã tồn tại. Vui lòng chọn tên khác.";
            }
        }
        
        if (branchData.get("location") != null && branchData.get("location").length() > 255) {
            return "Vị trí không được vượt quá 255 ký tự";
        }
        if (branchData.get("locationDetail") != null && branchData.get("locationDetail").length() > 500) {
            return "Chi tiết địa chỉ không được vượt quá 500 ký tự";
        }
        if (branchData.get("phoneNo") != null && branchData.get("phoneNo").length() > 20) {
            return "Số điện thoại không được vượt quá 20 ký tự";
        }
        if (branchData.get("description") != null && branchData.get("description").length() > 1000) {
            return "Mô tả không được vượt quá 1000 ký tự";
        }
        
        return null;
    }

    private String validateRoomData(Map<String, Object> roomData, Integer branchId) {
        if (roomData.get("name") == null || roomData.get("name").toString().trim().isEmpty()) {
            return "Tên phòng là bắt buộc";
        }
        if (roomData.get("name").toString().length() > 100) {
            return "Tên phòng không được vượt quá 100 ký tự";
        }
        if (roomData.get("capacity") == null) {
            return "Sức chứa phòng là bắt buộc";
        }
        
        try {
            int capacity = Integer.parseInt(roomData.get("capacity").toString());
            if (capacity <= 0) {
                return "Sức chứa phòng phải lớn hơn 0";
            }
        } catch (NumberFormatException e) {
            return "Giá trị sức chứa không hợp lệ";
        }
        
        // Validate row count (now required)
        if (roomData.get("rowCount") == null) {
            return "Số hàng là bắt buộc";
        }
        
        try {
            int rowCount = Integer.parseInt(roomData.get("rowCount").toString());
            if (rowCount <= 0) {
                return "Số hàng phải lớn hơn 0";
            }
            
            int capacity = Integer.parseInt(roomData.get("capacity").toString());
            if (capacity < rowCount) {
                return "Sức chứa phải ít nhất bằng số hàng";
            }
        } catch (NumberFormatException e) {
            return "Giá trị số hàng không hợp lệ";
        }
        
        if (roomData.get("roomType") != null && roomData.get("roomType").toString().length() > 50) {
            return "Loại phòng không được vượt quá 50 ký tự";
        }
        if (roomData.get("description") != null && roomData.get("description").toString().length() > 500) {
            return "Mô tả không được vượt quá 500 ký tự";
        }
        
        return null;
    }
} 