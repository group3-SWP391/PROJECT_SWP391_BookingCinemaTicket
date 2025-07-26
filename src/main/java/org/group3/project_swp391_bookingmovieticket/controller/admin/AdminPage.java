package org.group3.project_swp391_bookingmovieticket.controller.admin;

import org.group3.project_swp391_bookingmovieticket.entity.AdvertisingContactRequest;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.service.IUserService;
import org.group3.project_swp391_bookingmovieticket.service.impl.AdvertisingContactRequestService;
import org.group3.project_swp391_bookingmovieticket.service.impl.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminPage {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private AdvertisingContactRequestService advertisingContactRequestService;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public String adminHomePage() {
        return "admin/home-page-admin";

    }

    @GetMapping("/user")
    public String getAllUser(Model model) {
        List<User> listUser = new ArrayList<>();
        List<User> list = iUserService.findAll();

        for (User user : list) {
            if ("Customer".equalsIgnoreCase(user.getRole().getName())) {
                listUser.add(user);
            }
        }
        model.addAttribute("list", listUser);
        return "admin/list-page-customer";

    }

    @GetMapping("/employee")
    public String getAllEmployee(Model model) {
        List<User> listUser = new ArrayList<>();
        for (User user : iUserService.findAll()) {
            if (user.getRole().getName().equalsIgnoreCase("STAFF")) {
                listUser.add(user);
            }
        }
        model.addAttribute("list", listUser);
        System.out.println(listUser + "staff");
        return "admin/list-page-employee";

    }

    @GetMapping("/advertising")
    public String showAdvertisingPage(Model model) {
        List<AdvertisingContactRequest> advertisingContactRequests = advertisingContactRequestService.findAll();
        model.addAttribute("advertisinglist", advertisingContactRequests);
        return "admin/advertise";
    }

    @PostMapping("/response")
    public String proccessResponse(@RequestParam("id") Integer id,
                                   @RequestParam("responseContent") String responseContent,
                                   RedirectAttributes redirectAttributes) {
        Optional<AdvertisingContactRequest> contactRequestOptional = advertisingContactRequestService.findById(id);
        if (contactRequestOptional.isPresent()) {
            AdvertisingContactRequest advertisingContactRequest = contactRequestOptional.get();
            advertisingContactRequest.setStatus(true);
            advertisingContactRequestService.update(advertisingContactRequest);
            emailService.sendEmail(advertisingContactRequest.getEmail(), "Xác nhận phản hồi quảng cáo", "✅ Yêu cầu quảng cáo của bạn đã được ghi nhận. Cảm ơn bạn đã tin tưởng sử dụng dịch vụ của chúng tôi");
        }
        redirectAttributes.addFlashAttribute("messageSuccess", "Xác nhận phản hồi quảng cáo thành công!!!");
        return "redirect:/admin/advertising";
    }
}
