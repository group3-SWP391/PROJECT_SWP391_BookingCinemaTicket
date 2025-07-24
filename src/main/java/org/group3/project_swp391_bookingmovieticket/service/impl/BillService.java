package org.group3.project_swp391_bookingmovieticket.service.impl;

import jakarta.transaction.Transactional;
import org.group3.project_swp391_bookingmovieticket.dto.BookingRequestDTO;
import org.group3.project_swp391_bookingmovieticket.dto.PopcornDrinkDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Bill;
import org.group3.project_swp391_bookingmovieticket.entity.Schedule;
import org.group3.project_swp391_bookingmovieticket.entity.Ticket;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.repository.*;
import org.group3.project_swp391_bookingmovieticket.repository.IBillRepository;
import org.group3.project_swp391_bookingmovieticket.service.IBillService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
    public boolean existsBillByUserId(Integer userId) {
        return billRepository.existsByUserId(userId);

    }

    @Override
    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    @Override
    public Optional<Bill> findById(Integer id) {
        return billRepository.findById(id);
    }

    @Override
    public void update(Bill bill) {

    }

    @Override
    public void remove(Integer id) {

    }

    @Transactional
    @Override
    public Bill createNewBill(BookingRequestDTO bookingRequestDTO) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        Schedule schedule = scheduleRepository.getById(bookingRequestDTO.getScheduleId());

        // Kiểm tra nếu lịch chiếu đã diễn ra
        if (schedule.getStartDate().isBefore(date) ||
                (schedule.getStartDate().isEqual(date) && schedule.getStartTime().isBefore(time))) {
            throw new RuntimeException("The schedule has ended, you cannot book a seat!");
        }

        User user = userRepository.getById(bookingRequestDTO.getUserId());

        // Tạo bill
        Bill bill = new Bill();
        bill.setUser(user);
        bill.setCreatedTime(LocalDateTime.now());
        bill.setPrice(bookingRequestDTO.getTotalPrice());

        List<Ticket> ticketList = new ArrayList<>();

        List<String> listPopcornDrinkName = new ArrayList<>();
        if (bookingRequestDTO.getListPopcornDrink() != null) {
            for (PopcornDrinkDTO dto : bookingRequestDTO.getListPopcornDrink()) {
                String nameWithQuantity = dto.getName() + " (x" + dto.getQuantity() + ")";
                listPopcornDrinkName.add(nameWithQuantity);
            }
        }

        for (Integer seatId : bookingRequestDTO.getListSeatId()) {
            // Kiểm tra ghế đã bị người khác đặt chưa
            boolean isAlreadyBooked = !ticketRepository
                    .findTicketsBySchedule_IdAndSeat_Id(bookingRequestDTO.getScheduleId(), seatId)
                    .isEmpty();
            if (isAlreadyBooked) {
                throw new RuntimeException("Someone was quicker and booked seat " + seatId + ", please choose again!");
            }

            Ticket ticket = new Ticket();
            ticket.setSchedule(schedule);
            ticket.setStatus(false);
            ticket.setSeat(seatRepository.getById(seatId));
            ticket.setBill(bill); // gắn ngược lại vào bill
            // thêm list bỏng nước vào
            if (bookingRequestDTO.getListPopcornDrink() != null) {
                ticket.setListPopcornDrinkName(String.join(",", listPopcornDrinkName));
            }

            ticketList.add(ticket);
        }

        // Gắn danh sách ticket vào bill
        bill.setTickets(ticketList);

        // Nhờ cascade = ALL, save bill sẽ tự save ticket
        return billRepository.save(bill);
    }

}
