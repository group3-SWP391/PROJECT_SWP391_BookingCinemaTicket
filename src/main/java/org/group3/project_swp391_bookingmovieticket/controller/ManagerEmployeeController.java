package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entities.Role;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.repositories.IRoleRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.IUserRepository;
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

        List<User> employees = userRepository.findAll();
        List<Role> roles = roleRepository.findAll();

        model.addAttribute("employees", employees);
        model.addAttribute("roles", roles);
        model.addAttribute("content", "manager/employees"); // render vào layout.html
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
            Integer roleId = (Integer) employeeData.get("roleId");

            if (userRepository.findByPhone(phone) != null) {
                response.put("success", false);
                response.put("message", "Số điện thoại đã tồn tại.");
                return ResponseEntity.ok(response);
            }

            Optional<Role> roleOpt = roleRepository.findById(roleId);
            if (!roleOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Vai trò không hợp lệ.");
                return ResponseEntity.ok(response);
            }

            User user = new User();
            user.setPhone(phone);
            user.setPassword(password); // bạn nên hash mật khẩu trong ứng dụng thật
            user.setFullname(fullname);
            user.setUsername(username);
            user.setEmail(email);
            user.setRole(roleOpt.get());

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
            user.setFullname((String) employeeData.get("fullname"));
            user.setPhone((String) employeeData.get("phone"));
            user.setUsername((String) employeeData.get("username"));
            user.setEmail((String) employeeData.get("email"));

            Integer roleId = (Integer) employeeData.get("roleId");
            Optional<Role> roleOpt = roleRepository.findById(roleId);
            roleOpt.ifPresent(user::setRole);

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

    // Xoá nhân viên
    @DeleteMapping("/employees/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteEmployee(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (!userRepository.existsById(id)) {
                response.put("success", false);
                response.put("message", "Không tìm thấy nhân viên.");
                return ResponseEntity.ok(response);
            }

            userRepository.deleteById(id);
            response.put("success", true);
            response.put("message", "Xoá nhân viên thành công.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi xoá: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
