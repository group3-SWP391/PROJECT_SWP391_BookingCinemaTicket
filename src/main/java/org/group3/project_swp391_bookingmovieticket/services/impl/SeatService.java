package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.dtos.SeatDTO;
import org.group3.project_swp391_bookingmovieticket.entities.PaymentLink;
import org.group3.project_swp391_bookingmovieticket.entities.Room;
import org.group3.project_swp391_bookingmovieticket.entities.Seat;
import org.group3.project_swp391_bookingmovieticket.entities.Ticket;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        System.out.println("getSeatsByScheduleIdAndUserId");
        Room room = scheduleRepository.getById(scheduleId).getRoom();
        List<Seat> listSeat = seatRepository.getSeatByRoom_Id(room.getId());

        // các ghế của người khác đặt thành công và đã có trong bảng bill
        List<Seat> occupiedSeats = ticketRepository.findTicketsOfOtherUser(scheduleId, userId)
                .stream()
                .map(Ticket::getSeat).toList();

        // các ghế đã thanh toán và đã có trong bảng bill
        List<Seat> checkedSeats = ticketRepository.findTicketsOfCurrentUserAndScheduleId(userId, scheduleId)
                .stream()
                .map(Ticket::getSeat).toList();

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
                                        // vì seat list là cả giá tiền nên phải dùng regex để tách
                                        extractSeatNames(link.getSeatList())
                                                .stream()
                                                .anyMatch(seatDTO.getName()::equals) &&
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
                                extractSeatNames(link.getSeatList())
                                        .stream()
                                        .anyMatch(seatDTO.getName()::equals) &&
                                // vì trong cùng 1 payment link và cùng 1 schedule có nhiều user đặt nên phải so sánh với user hiện tại để xem có phải là ghế của user hiện tại không
                                Objects.equals(link.getUser().getId(), userId)
                );
                seatDTO.setChecked(isSeatHeld);
            }
            return seatDTO;
        }).toList();

        // thay đổi trạng thái các link đã quá 5 phút
        List<PaymentLink> expiredLinks = allPendingLinks.stream()
                .filter(link -> Duration.between(link.getCreatedAt(), LocalDateTime.now()).toMinutes() > 5)
                .toList();
        expiredLinks.forEach(link -> link.setStatus("CANCELLED"));
        paymentLinkRepository.saveAll(expiredLinks);

        return seatDTOs;
    }

    private List<String> extractSeatNames(String seatList) {
        List<String> names = new ArrayList<>();
        // Tạo biểu thức chính quy để tìm tên ghế.
        // Pattern này khớp với: 1 hoặc nhiều chữ in hoa + 1 hoặc nhiều số, đứng trước dấu "("
        Pattern pattern = Pattern.compile("\\b([A-Z]+\\d+)\\s*\\(");
        // Tạo matcher để áp dụng biểu thức pattern lên chuỗi đầu vào
        Matcher matcher = pattern.matcher(seatList);
        // Duyệt từng phần khớp được trong chuỗi
        while (matcher.find()) {
            // Lấy nhóm thứ nhất trong biểu thức (tên ghế) và thêm vào danh sách kết quả
            // group(1) tương ứng với phần ([A-Z]+\\d+) trong regex
            // group(0) tương ứng toàn bộ phần regex
            names.add(matcher.group(1));
        }
        return names;
    }

    @Override
    public List<String> findSeatNamesByIdList(List<Integer> ids) {
        return seatRepository.findSeatNamesByIdList(ids);
    }

    @Override
    public String findSeatNameById(Integer id) {
        return seatRepository.findSeatNameById(id);
    }


    @Override
    public List<Seat> findAll() {
        return List.of();
    }

    @Override
    public Optional<Seat> findById(Integer id) {
        return seatRepository.findById(id);
    }

    @Override
    public void update(Seat seat) {

    }

    @Override
    public void remove(Integer id) {
    }
}
