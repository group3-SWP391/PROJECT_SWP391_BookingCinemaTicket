package org.group3.project_swp391_bookingmovieticket;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "org.group3.project_swp391_bookingmovieticket")
public class ProjectSwp391BookingMovieTicketApplication {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        System.out.println("=== APPLICATION STARTED ===");
        SpringApplication.run(ProjectSwp391BookingMovieTicketApplication.class, args);
    }

}
