package org.group3.project_swp391_bookingmovieticket.services.impl;

import jakarta.transaction.Transactional;
import org.group3.project_swp391_bookingmovieticket.dtos.BookingRequestDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Bill;
import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.group3.project_swp391_bookingmovieticket.entities.Ticket;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.repositories.*;
import org.group3.project_swp391_bookingmovieticket.services.IBillService;
import org.group3.project_swp391_bookingmovieticket.services.IScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class BillService implements IBillService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IBillRepository billRepository;

    @Autowired
    private IScheduleRepository scheduleRepository;

    @Autowired
    private ITicketRepository ticketRepository;

    @Autowired
    private ISeatRepository seatRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Bill> findAll() {
        return List.of();
    }

    @Override
    public Optional<Bill> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void update(Bill bill) {

    }

    @Override
    public void remove(Integer id) {

    }

    @Transactional
    @Override
    public void createNewBill(BookingRequestDTO bookingRequestDTO) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        Schedule schedule = scheduleRepository.getById(bookingRequestDTO.getScheduleId());
        if (schedule.getStartDate().compareTo(date) > 0 ||
                (schedule.getStartDate().compareTo(date) == 0 && schedule.getStartTime().compareTo(time) > 0)) {
            User user = userRepository.getById(bookingRequestDTO.getUserId());

            Bill billToCreate = new Bill();
            billToCreate.setUser(user);
            billToCreate.setCreatedTime(LocalDateTime.now());
            billToCreate.setPrice(bookingRequestDTO.getTotalPrice());
            Bill createdBill = billRepository.save(billToCreate);

            bookingRequestDTO.getListSeatId().forEach(seatId -> {
                // nếu kh rỗng
                if (!ticketRepository.findTicketsBySchedule_IdAndSeat_Id(bookingRequestDTO.getScheduleId(), seatId)
                        .isEmpty()) {
                    throw new RuntimeException("Someone was quicker and booked the seat, please choose again!");
                }
                Ticket ticket = new Ticket();
                ticket.setSchedule(schedule);
                ticket.setSeat(seatRepository.getById(seatId));
                ticket.setBill(createdBill);
                ticketRepository.save(ticket);
            });
        } else {
            throw new RuntimeException("The schedule has ended, you cannot book a seat!");
        }
    }
}
