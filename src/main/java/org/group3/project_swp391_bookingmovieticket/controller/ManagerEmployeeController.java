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

    @GetMapping("/employees")
    public String showEmployees(Model model, HttpSession session) {
        User manager = (User) session.getAttribute("userLogin");
        if (manager == null) return "redirect:/login";

        List<User> employees = userRepository.findAll();
        List<Role> roles = roleRepository.findAll();

        model.addAttribute("employees", employees);
        model.addAttribute("roles", roles);
        model.addAttribute("content", "manager/employees"); // view: manager/layout.html
        return "manager/layout";
    }

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
                response.put("message", "Phone number already exists.");
                return ResponseEntity.ok(response);
            }

            Optional<Role> roleOpt = roleRepository.findById(roleId);
            if (!roleOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Invalid role ID.");
                return ResponseEntity.ok(response);
            }

            User user = new User();
            user.setPhone(phone);
            user.setPassword(password); // Hash in real app!
            user.setFullname(fullname);
            user.setUsername(username);
            user.setEmail(email);
            user.setRole(roleOpt.get());

            userRepository.save(user);

            response.put("success", true);
            response.put("message", "Employee created successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating employee: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/employees/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateEmployee(@PathVariable int id, @RequestBody Map<String, Object> employeeData) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (!userOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Employee not found.");
                return ResponseEntity.ok(response);
            }

            User user = userOpt.get();

            user.setFullname((String) employeeData.get("fullname"));
            user.setPhone((String) employeeData.get("phone"));
            user.setUsername((String) employeeData.get("username"));
            user.setEmail((String) employeeData.get("email"));

            if (employeeData.get("roleId") != null) {
                Integer roleId = (Integer) employeeData.get("roleId");
                Optional<Role> roleOpt = roleRepository.findById(roleId);
                roleOpt.ifPresent(user::setRole);
            }

            userRepository.save(user);

            response.put("success", true);
            response.put("message", "Employee updated successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating employee: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/employees/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteEmployee(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (!userOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Employee not found.");
                return ResponseEntity.ok(response);
            }

            userRepository.deleteById(id);
            response.put("success", true);
            response.put("message", "Employee deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting employee: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
