package org.group3.project_swp391_bookingmovieticket.controller.manager;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.Role;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.repository.IRoleRepository;
import org.group3.project_swp391_bookingmovieticket.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/manager")
public class ManagerEmployeeController {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    // Hiển thị danh sách nhân viên
    @GetMapping("/employees")
    public String showEmployees(Model model, HttpSession session) {
        User manager = (User) session.getAttribute("userLogin");
        if (manager == null) return "redirect:/login";

        List<User> employees = userRepository.findAllByRole_Name("staff");
        List<Role> roles = roleRepository.findAll();
        roles.removeIf(role -> "manager".equals(role.getName()));
        model.addAttribute("employees", employees);
        model.addAttribute("roles", roles);
        model.addAttribute("content", "manager/employees");
        return "manager/layout";
    }

    // Tạo nhân viên
    @PostMapping("/employees")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createEmployee(@RequestBody Map<String, Object> employeeData) {
        Map<String, Object> response = new HashMap<>();
        try {
            String phone = (String) employeeData.get("phone");
            String password = (String) employeeData.get("password");
            String fullname = (String) employeeData.get("fullname");
            String username = (String) employeeData.get("username");
            String email = (String) employeeData.get("email");
            //Integer roleId = (Integer) employeeData.get("roleId");

            if (userRepository.findByPhone(phone).isPresent()) {
                response.put("success", false);
                response.put("message", "Số điện thoại đã tồn tại.");
                return ResponseEntity.ok(response);
            }
            if (!userRepository.findByUsername(username).isEmpty()) {
                response.put("success", false);
                response.put("message", "Tên đăng nhập đã tồn tại.");
                return ResponseEntity.ok(response);
            }
            if (!userRepository.findByEmail(email).isEmpty()) {
                response.put("success", false);
                response.put("message", "Email đã tồn tại.");
                return ResponseEntity.ok(response);
            }
            Optional<Role> roleOpt = roleRepository.findByName("staff");
            if (roleOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Vai trò không hợp lệ.");
                return ResponseEntity.ok(response);
            }

            User user = new User();
            user.setPhone(phone);
            user.setPassword(password);
            user.setFullname(fullname);
            user.setUsername(username);
            user.setEmail(email);
            user.setRole(roleOpt.get());
            user.setStatus(true);

            userRepository.save(user);

            response.put("success", true);
            response.put("message", "Thêm nhân viên thành công.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi tạo nhân viên: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Lấy chi tiết nhân viên theo ID (sửa)
    @GetMapping("/employees/{id}")
    @ResponseBody
    public ResponseEntity<User> getEmployeeById(@PathVariable int id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Cập nhật nhân viên
    @PutMapping("/employees/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateEmployee(@PathVariable int id, @RequestBody Map<String, Object> employeeData) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (!userOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Không tìm thấy nhân viên.");
                return ResponseEntity.ok(response);
            }

            User user = userOpt.get();

            // Get the new values from request
            String phone = (String) employeeData.get("phone");
            String username = (String) employeeData.get("username");
            String email = (String) employeeData.get("email");
            String fullname = (String) employeeData.get("fullname");

            String password = (String) employeeData.get("password");
            //Integer roleId = (Integer) employeeData.get("roleId");

            // Check if phone exists and is not the current user's phone
            Optional<User> existingPhoneUser = userRepository.findByPhone(phone);
            if (existingPhoneUser.isPresent() && existingPhoneUser.get().getId() != id) {
                response.put("success", false);
                response.put("message", "Số điện thoại đã tồn tại.");
                return ResponseEntity.ok(response);
            }

            // Check if username exists and is not the current user's username
            List<User> existingUsernameUsers = userRepository.findByUsername(username);
            if (!existingUsernameUsers.isEmpty() && existingUsernameUsers.stream().anyMatch(u -> u.getId() != id)) {
                response.put("success", false);
                response.put("message", "Tên đăng nhập đã tồn tại.");
                return ResponseEntity.ok(response);
            }

            // Check if email exists and is not the current user's email
            List<User> existingEmailUsers = userRepository.findByEmailUserList(email);
            if (!existingEmailUsers.isEmpty() && existingEmailUsers.stream().anyMatch(u -> u.getId() != id)) {
                response.put("success", false);
                response.put("message", "Email đã tồn tại.");
                return ResponseEntity.ok(response);
            }

            // Check if role exists
//            Optional<Role> roleOpt = roleRepository.findById(roleId);
//            if (!roleOpt.isPresent()) {
//                response.put("success", false);
//                response.put("message", "Vai trò không hợp lệ.");
//                return ResponseEntity.ok(response);
//            }

            // Update user fields
            user.setFullname(fullname);
            user.setPhone(phone);
            user.setUsername(username);
            user.setEmail(email);
            //user.setRole(roleOpt.get());

            // Update password if provided
            if (password != null && !password.trim().isEmpty()) {
                user.setPassword(password);
            }

            userRepository.save(user);

            response.put("success", true);
            response.put("message", "Cập nhật nhân viên thành công.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi cập nhật: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Fire (Deactivate) Employee
    @PutMapping("/employees/{id}/fire")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> fireEmployee(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (!userOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Không tìm thấy nhân viên.");
                return ResponseEntity.ok(response);
            }

            User user = userOpt.get();
            user.setStatus(false);
            userRepository.save(user);

            response.put("success", true);
            response.put("message", "Đã sa thải nhân viên thành công.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi sa thải: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Activate Employee
    @PutMapping("/employees/{id}/activate")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> activateEmployee(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (!userOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Không tìm thấy nhân viên.");
                return ResponseEntity.ok(response);
            }

            User user = userOpt.get();
            user.setStatus(true);
            userRepository.save(user);

            response.put("success", true);
            response.put("message", "Đã kích hoạt nhân viên thành công.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi kích hoạt: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}