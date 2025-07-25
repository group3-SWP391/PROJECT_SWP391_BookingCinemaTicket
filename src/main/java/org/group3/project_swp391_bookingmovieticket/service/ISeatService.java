package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.dto.SeatDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Room;
import org.group3.project_swp391_bookingmovieticket.entity.Seat;


import java.util.List;

public interface ISeatService extends IGeneralService<Seat> {
    List<SeatDTO> getSeatsByScheduleIdAndUserId(Integer scheduleId, Integer userId);
    List<String> findSeatNamesByIdList(List<Integer> ids);
    String findSeatNameById(Integer id);
    SeatDTO getSeatById(Integer id);

    List<Seat> getSeatsByRoomId(Integer roomId);
    List<Seat> getActiveSeatsByRoomId(Integer roomId);
    List<Seat> getVipSeatsByRoomId(Integer roomId);
    Seat saveSeat(Seat seat);
    void deleteSeatsByRoomId(Integer roomId);
    void generateSeatsForRoom(Room room, String vipSeatsInput);
    void updateSeatsForRoom(Room room, String vipSeatsInput);
    List<String> parseVipSeats(String vipSeatsInput);
    List<String> generateAllSeatNames(int capacity, int rowCount);

//phu
List<Seat> getListSeatById(int roomId);
    List<Seat> findSeatsByIds(List<Integer> ids);
}