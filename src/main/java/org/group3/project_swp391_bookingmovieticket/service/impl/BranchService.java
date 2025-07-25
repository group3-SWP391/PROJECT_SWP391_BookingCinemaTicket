package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Branch;
import org.group3.project_swp391_bookingmovieticket.entity.Room;
import org.group3.project_swp391_bookingmovieticket.entity.Schedule;
import org.group3.project_swp391_bookingmovieticket.entity.Ticket;
import org.group3.project_swp391_bookingmovieticket.entity.Seat;
import org.group3.project_swp391_bookingmovieticket.repository.IBranchRepository;
import org.group3.project_swp391_bookingmovieticket.repository.IRoomRepository;
import org.group3.project_swp391_bookingmovieticket.repository.IScheduleRepository;
import org.group3.project_swp391_bookingmovieticket.repository.ITicketRepository;
import org.group3.project_swp391_bookingmovieticket.repository.ISeatRepository;
import org.group3.project_swp391_bookingmovieticket.service.IBranchService;
import org.group3.project_swp391_bookingmovieticket.service.ISeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BranchService implements IBranchService {
    
    @Autowired
    private IBranchRepository branchRepository;
    
    @Autowired
    private IRoomRepository roomRepository;
    
    @Autowired
    private IScheduleRepository scheduleRepository;
    
    @Autowired
    private ITicketRepository ticketRepository;
    
    @Autowired
    private ISeatRepository seatRepository;
    
    @Autowired
    private ISeatService seatService;
    
    @Override
    public List<Branch> getAllBranchs() {
        return branchRepository.findAll();
    }
    
    @Override
    public Optional<Branch> getBranchById(Integer id) {
        return branchRepository.findById(id);
    }
    
    @Override
    public Branch saveBranch(Branch branch) {
        return branchRepository.save(branch);
    }
    
    @Override
    public Branch updateBranch(Branch branch) {
        return branchRepository.save(branch);
    }
    
    @Override
    public void deleteBranch(Integer id) {
        Optional<Branch> branchOpt = branchRepository.findById(id);
        if (branchOpt.isPresent()) {
            // STEP 1: Delete all tickets related to schedules in this branch
            List<Schedule> branchSchedules = scheduleRepository.findByBranchId(id);
            for (Schedule schedule : branchSchedules) {
                List<Ticket> scheduleTickets = ticketRepository.findByScheduleId(schedule.getId());
                ticketRepository.deleteAll(scheduleTickets);
            }
            
            // STEP 2: Delete all tickets related to seats in rooms of this branch
            List<Room> rooms = roomRepository.findByBranchId(id);
            for (Room room : rooms) {
                List<Seat> roomSeats = seatRepository.findByRoomId(room.getId());
                for (Seat seat : roomSeats) {
                    List<Ticket> seatTickets = ticketRepository.findBySeatId(seat.getId());
                    if (seatTickets != null && !seatTickets.isEmpty()) {
                        ticketRepository.deleteAll(seatTickets);
                    }
                }
            }
            
            // STEP 3: Delete all schedules for this branch
            scheduleRepository.deleteAll(branchSchedules);
            
            // STEP 4: Delete all schedules for rooms in this branch (room-specific schedules)
            for (Room room : rooms) {
                List<Schedule> roomSchedules = scheduleRepository.findByRoomId(room.getId());
                scheduleRepository.deleteAll(roomSchedules);
            }
            
            // STEP 5: Delete all seats for each room
            for (Room room : rooms) {
                seatService.deleteSeatsByRoomId(room.getId());
            }
            
            // STEP 6: Delete all rooms for this branch
            roomRepository.deleteAll(rooms);
            
            // STEP 7: Finally delete the branch
            branchRepository.deleteById(id);
        }
    }
    
    @Override
    public List<Branch> getBranchsByCity(String city) {
        return branchRepository.findByCity(city);
    }
    
    @Override
    public Optional<Branch> getBranchByName(String name) {
        return branchRepository.findByName(name);
    }
    
    @Override
    public Optional<Branch> getBranchByPhone(String phone) {
        return branchRepository.findByPhoneNo(phone);
    }
    

    @Override
    public List<Branch> getBranchsWithRooms() {
        return branchRepository.findBranchsWithRooms();
    }
    
    @Override
    public Long getRoomCount(Integer branchId) {
        return branchRepository.countActiveRoomsByBranchId(branchId);
    }
    
    @Override
    public Long getTotalCapacity(Integer branchId) {
        Long capacity = roomRepository.getTotalCapacityByBranchId(branchId);
        return capacity != null ? capacity : 0L;
    }
    
    @Override
    public List<Branch> searchBranchsByAddress(String address) {
        return branchRepository.findByLocationContainingIgnoreCase(address);
    }
    
    @Override
    public boolean isBranchNameUnique(String name) {
        return branchRepository.findByName(name).isEmpty();
    }
    
    @Override
    public boolean isBranchPhoneUnique(String phone) {
        return branchRepository.findByPhoneNo(phone).isPresent();
    }
    

    @Override
    public void activateBranch(Integer id) {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isPresent()) {
            Branch existingBranch = branch.get();
            branchRepository.save(existingBranch);
        }
    }
    
    @Override
    public void deactivateBranch(Integer id) {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isPresent()) {
            Branch existingBranch = branch.get();
            branchRepository.save(existingBranch);
        }
    }
    
    @Override
    public boolean existsById(Integer id) {
        return branchRepository.existsById(id);
    }
    
    @Override
    public boolean isActiveBranch(Integer id) {
        Optional<Branch> branch = branchRepository.findById(id);
        return branch.isPresent();
    }
} 