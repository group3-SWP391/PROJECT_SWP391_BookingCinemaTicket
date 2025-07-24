package org.group3.project_swp391_bookingmovieticket.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.group3.project_swp391_bookingmovieticket.dto.SeatDTO;
import org.group3.project_swp391_bookingmovieticket.entity.PaymentLink;
import org.group3.project_swp391_bookingmovieticket.entity.Room;
import org.group3.project_swp391_bookingmovieticket.entity.Seat;
import org.group3.project_swp391_bookingmovieticket.entity.Ticket;
import org.group3.project_swp391_bookingmovieticket.repository.IPaymentLinkRepository;
import org.group3.project_swp391_bookingmovieticket.repository.IScheduleRepository;
import org.group3.project_swp391_bookingmovieticket.repository.ISeatRepository;
import org.group3.project_swp391_bookingmovieticket.repository.ITicketRepository;
import org.group3.project_swp391_bookingmovieticket.service.ISeatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public SeatDTO getSeatById(Integer id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seat not found with id: " + id));

        return modelMapper.map(seat, SeatDTO.class); // giả sử bạn có SeatMapper để chuyển đổi
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


    @Override
    public List<Seat> getSeatsByRoomId(Integer roomId) {
        return seatRepository.findByRoomId(roomId);
    }

    @Override
    public List<Seat> getActiveSeatsByRoomId(Integer roomId) {
        return seatRepository.findByRoomId(roomId)
                .stream()
                .filter(Seat::isActive)
                .collect(Collectors.toList());
    }

    @Override
    public List<Seat> getVipSeatsByRoomId(Integer roomId) {
        return seatRepository.findByRoomId(roomId).stream()
                .filter(Seat::isVip)
                .toList(); // Java 16+ hoặc dùng .collect(Collectors.toList()) nếu Java < 16
    }


    @Override
    public Seat saveSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    @Override
    @Transactional
    public void deleteSeatsByRoomId(Integer roomId) {
        List<Seat> seats = seatRepository.findByRoomId(roomId);
        seatRepository.deleteAll(seats);
    }

    @Override
    @Transactional
    public void generateSeatsForRoom(Room room, String vipSeatsInput) {
        // First, delete existing seats for this room
        deleteSeatsByRoomId(room.getId());

        // Parse VIP seats input
        List<String> vipSeatNames = parseVipSeats(vipSeatsInput);

        // Generate all possible seat names
        List<String> allSeatNames = generateAllSeatNames(room.getCapacity(), room.getRowCount());

        // Create seat entities
        List<Seat> seats = new ArrayList<>();
        for (String seatName : allSeatNames) {
            Seat seat = Seat.builder()
                    .name(seatName)
                    .room(room)
                    .isActive(room.isActive()) // Inherit from room's active status
                    .isVip(vipSeatNames.contains(seatName)) // Check if seat is VIP (both already uppercase)
                    .build();
            seats.add(seat);
        }

        // Save all seats
        seatRepository.saveAll(seats);
    }

    @Override
    @Transactional
    public void updateSeatsForRoom(Room room, String vipSeatsInput) {
        // Parse VIP seats input
        List<String> vipSeatNames = parseVipSeats(vipSeatsInput);

        // Generate all possible seat names based on current capacity and row count
        List<String> allSeatNames = generateAllSeatNames(room.getCapacity(), room.getRowCount());

        // Get existing seats for this room
        List<Seat> existingSeats = seatRepository.findByRoomId(room.getId());

        // Update existing seats and track which ones we've processed
        Map<String, Seat> existingSeatMap = existingSeats.stream()
                .collect(Collectors.toMap(Seat::getName, seat -> seat));

        List<Seat> seatsToSave = new ArrayList<>();
        List<Seat> seatsToDelete = new ArrayList<>();

        // Update or create seats based on new layout
        for (String seatName : allSeatNames) {
            if (existingSeatMap.containsKey(seatName)) {
                // Update existing seat
                Seat existingSeat = existingSeatMap.get(seatName);
                existingSeat.setActive(room.isActive());
                existingSeat.setVip(vipSeatNames.contains(seatName));
                seatsToSave.add(existingSeat);
                existingSeatMap.remove(seatName); // Mark as processed
            } else {
                // Create new seat
                Seat newSeat = Seat.builder()
                        .name(seatName)
                        .room(room)
                        .isActive(room.isActive())
                        .isVip(vipSeatNames.contains(seatName))
                        .build();
                seatsToSave.add(newSeat);
            }
        }

        // Remaining seats in existingSeatMap are no longer needed
        // But we can't delete them if they have tickets, so just mark them as inactive
        for (Seat obsoleteSeat : existingSeatMap.values()) {
            obsoleteSeat.setActive(false);
            seatsToSave.add(obsoleteSeat);
        }

        // Save all updated/new seats
        seatRepository.saveAll(seatsToSave);
    }

    @Override
    public List<String> parseVipSeats(String vipSeatsInput) {
        if (vipSeatsInput == null || vipSeatsInput.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(vipSeatsInput.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .filter(seat -> !seat.isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> generateAllSeatNames(int capacity, int rowCount) {
        List<String> seatNames = new ArrayList<>();

        // Calculate seats per row
        int seatsPerRow = capacity / rowCount;
        int extraSeats = capacity % rowCount;

        for (int row = 0; row < rowCount; row++) {
            char rowLetter = (char) ('A' + row);

            // Calculate seats in this row (extra seats go to first few rows)
            int seatsInThisRow = seatsPerRow + (row < extraSeats ? 1 : 0);

            // Generate seat names for this row
            for (int seat = 1; seat <= seatsInThisRow; seat++) {
                seatNames.add(rowLetter + String.valueOf(seat));
            }
        }

        return seatNames;
    }
}
