package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.dtos.SeatDTO;
import org.group3.project_swp391_bookingmovieticket.entities.PaymentLink;
import org.group3.project_swp391_bookingmovieticket.entities.Room;
import org.group3.project_swp391_bookingmovieticket.entities.Seat;
import org.group3.project_swp391_bookingmovieticket.repositories.IPaymentLinkRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.IScheduleRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.ISeatRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.ITicketRepository;
import org.group3.project_swp391_bookingmovieticket.services.ISeatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeatService implements ISeatService {
    @Autowired
    private ISeatRepository seatRepository;
    @Autowired
    private IScheduleRepository scheduleRepository;
    @Autowired
    private ITicketRepository ticketRepository;
    @Autowired
    private IPaymentLinkRepository paymentLinkRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<SeatDTO> getSeatsByScheduleIdAndUserId(Integer scheduleId, Integer userId) {
        Room room = scheduleRepository.getById(scheduleId).getRoom();
        List<Seat> listSeat = seatRepository.getSeatByRoom_Id(room.getId());

        // các ghế của người khác đặt thành công và đã có trong bảng bill
        List<Seat> occupiedSeats = ticketRepository.findTicketsOfOtherUser(scheduleId, userId)
                .stream().map(ticket -> ticket.getSeat()).collect(Collectors.toList());

        // các ghế đã thanh toán và đã có trong bảng bill
        List<Seat> checkedSeats = ticketRepository.findTicketsOfCurrentUserAndScheduleId(userId, scheduleId)
                .stream().map(ticket -> ticket.getSeat()).collect(Collectors.toList());

        // lấy về các payment link còn khả dụng
        List<PaymentLink> allPendingLinks = paymentLinkRepository.findAllByStatus("PENDING");

        //  Tách xử lý seatDTOs
        List<SeatDTO> seatDTOs = listSeat.stream().map(seat -> {
            SeatDTO seatDTO = modelMapper.map(seat, SeatDTO.class);

            // đánh dấu các ghế đã được mua bởi user khác
            if (occupiedSeats.stream().anyMatch(s -> Objects.equals(s.getId(), seatDTO.getId()))) {
                seatDTO.setOccupied(true);
            } else {
                // đánh dấu các ghế của user khác chưa được thanh toán
                boolean isSeatHeld = allPendingLinks.stream().anyMatch(link ->
                                Objects.equals(link.getSchedule().getId(), scheduleId) &&
                                        link.getSeatList().contains(seatDTO.getName()) &&
                                        !Objects.equals(link.getUser().getId(), userId) // chỉ lấy những ghế không phải của user
                        // hiện tại mà ở trong allPendingLinks để khóa những ghế dó lại
                );
                seatDTO.setOccupied(isSeatHeld);
            }

            // checkedSeats là những ghế trong bảng bill đã thanh toán
            if (checkedSeats.stream().anyMatch(s -> Objects.equals(s.getId(), seatDTO.getId()))) {
                seatDTO.setChecked(true);
            } else { // else khi những ghế đó đã được ở trong paymentLink nhưng ch đc thanh toán nhưng vẫn phải khóa tạm thời
                // đánh dấu các ghế của user đã được thanh toán
                boolean isSeatHeld = allPendingLinks.stream().anyMatch(link ->
                        Objects.equals(link.getSchedule().getId(), scheduleId) &&
                                link.getSeatList().contains(seatDTO.getName()) &&
                                Objects.equals(link.getUser().getId(), userId)
                );
                seatDTO.setChecked(isSeatHeld);
            }
            return seatDTO;
        }).collect(Collectors.toList());

        // thay đổi trạng thái các link đã quá 5 phút
        List<PaymentLink> expiredLinks = allPendingLinks.stream()
                .filter(link -> Duration.between(link.getCreatedAt(), LocalDateTime.now()).toMinutes() > 5)
                .collect(Collectors.toList());
        expiredLinks.forEach(link -> link.setStatus("CANCELLED"));
        paymentLinkRepository.saveAll(expiredLinks);

        return seatDTOs;
    }


    @Override
    public List<String> findSeatNamesByIdList(List<Integer> ids) {
        return seatRepository.findSeatNamesByIdList(ids);
    }


    @Override
    public List<Seat> findAll() {
        return List.of();
    }

    @Override
    public Optional<Seat> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void update(Seat seat) {

    }

    @Override
    public void remove(Integer id) {
    }
}
