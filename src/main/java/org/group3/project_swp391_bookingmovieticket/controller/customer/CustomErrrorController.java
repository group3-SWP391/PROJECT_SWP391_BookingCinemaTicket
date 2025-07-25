package org.group3.project_swp391_bookingmovieticket.controller.customer;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError() {
        return "redirect:/home";
    }
}
