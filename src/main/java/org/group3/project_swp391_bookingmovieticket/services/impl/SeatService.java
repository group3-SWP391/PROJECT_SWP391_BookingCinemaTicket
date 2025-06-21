package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.dtos.SeatDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Room;
import org.group3.project_swp391_bookingmovieticket.entities.Seat;
import org.group3.project_swp391_bookingmovieticket.repositories.IScheduleRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.ISeatRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.ITicketRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeatService {

    @Autowired
    private ISeatRepository seatRepository;
    @Autowired
    private IScheduleRepository scheduleRepository;
    @Autowired
    private ITicketRepository ticketRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<SeatDTO> getSeatsByScheduleIdAndUserId(Integer scheduleId, Integer userId) {
        Room room = scheduleRepository.getById(scheduleId).getRoom();
        List<Seat> listSeat = seatRepository.getSeatByRoom_Id(room.getId());

        // các ghế của người khác đặt thành công và đã có trong bảng bill
        List<Seat> occupiedSeats = ticketRepository.findTicketsOfOtherUser(scheduleId, userId)
                .stream().map(ticket -> ticket.getSeat()).collect(Collectors.toList());

        // các ghế đã thanh toán và đã có trong bảng bill
        List<Seat> checkedSeats = ticketRepository.findTicketsOfCurrentUserAndScheduleId(userId, scheduleId)
                .stream().map(ticket -> ticket.getSeat()).collect(Collectors.toList());

        //  Tách xử lý seatDTOs
        return listSeat.stream().map(seat -> {
            SeatDTO seatDTO = modelMapper.map(seat, SeatDTO.class);

            // đánh dấu các ghế đã được mua bởi user khác
            if (occupiedSeats.stream().anyMatch(s -> Objects.equals(s.getId(), seatDTO.getId()))) {
                seatDTO.setOccupied(true);
            }

            // checkedSeats là những ghế trong bảng bill đã thanh toán
            if (checkedSeats.stream().anyMatch(s -> Objects.equals(s.getId(), seatDTO.getId()))) {
                seatDTO.setChecked(true);
            }
            return seatDTO;
        }).collect(Collectors.toList());

    }

    // Lấy danh sách ghế theo roomId
//    public List<SeatDTO> getSeatsByRoomId(Integer roomId) {
//        return seatRepository.findByRoomId(roomId).stream()
//                .map(this::convertToSeatDTO)
//                .collect(Collectors.toList());
//    }

    // Lấy thông tin ghế theo id
    public SeatDTO getSeatById(Integer id) {
        Optional<Seat> seatOpt = seatRepository.findById(id);
        return seatOpt.map(this::convertToSeatDTO).orElse(null);
    }

    // Convert từ Entity sang DTO
    private SeatDTO convertToSeatDTO(Seat seat) {
        SeatDTO dto = new SeatDTO();
        dto.setId(seat.getId());
        dto.setName(seat.getName());
        dto.setIsActive(seat.getIsActive());
        dto.setIsVip(seat.getIsVip());
        return dto;
    }
}
