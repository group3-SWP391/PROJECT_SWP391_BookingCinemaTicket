package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.dtos.SeatDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Room;
import org.group3.project_swp391_bookingmovieticket.entities.Seat;
import org.group3.project_swp391_bookingmovieticket.repositories.IScheduleRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.ISeatRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.ITicketRepository;
import org.group3.project_swp391_bookingmovieticket.services.ISeatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;

import java.util.List;
import java.util.Optional;
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
    private ModelMapper modelMapper;


    @Override
    public List<SeatDTO> getSeatsByScheduleIdAndUserId(Integer scheduleId, Integer userId) {

        // Lấy ra các chỗ ngồi của phòng trong lịch đó
        Room room = scheduleRepository.getById(scheduleId).getRoom();
        // Lấy về các seat của room chứa cái schedule đó
        List<Seat> listSeat = seatRepository.getSeatByRoom_Id(room.getId());

        // Lấy về các ghế đã được đặt, lấy bởi scheduleId đã tồn tại trong bảng ticket
        List<Seat> occupiedSeats = ticketRepository.findTicketsBySchedule_Id(scheduleId)
                .stream()
                .map(ticket -> ticket.getSeat())
                .collect(Collectors.toList());

        //Lấy về các ghế mà user đang đăng nhập đã đặt
        List<Seat> checkedSeats = ticketRepository.findTicketsByUserIdAndScheduleId(userId, scheduleId)
                .stream()
                .map(ticket -> ticket.getSeat())
                .collect(Collectors.toList());

        // Chuyển về seatDTO, trả về các seatDTO
        List<SeatDTO> filterSeats = listSeat
                .stream()
                .map(seat -> {
                    SeatDTO seatDTO = modelMapper.map(seat, SeatDTO.class);
                    if (occupiedSeats
                            .stream()
                            .map(occupiedSeat -> occupiedSeat.getId())
                            .collect(Collectors.toList()).contains(seat.getId())) {
                        seatDTO.setIsOccupied(1); // Nếu ghế đã được đặt bởi người khác thì set = true
                    }
                    return seatDTO;
                }).collect(Collectors.toList());

        // kiếm tra những ghế được đặt bởi người dùng rồi
        filterSeats = filterSeats
                .stream()
                .map(seatDTO -> {
                    if (checkedSeats
                            .stream()
                            .map(checkedSeat -> checkedSeat.getId()).
                            collect(Collectors.toList()).contains(seatDTO.getId())) {
                        seatDTO.setChecked(true);
                    }
                    return seatDTO;
                }).collect(Collectors.toList());
        return filterSeats;
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
