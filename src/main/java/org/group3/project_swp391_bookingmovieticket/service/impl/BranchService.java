package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.dto.BranchDTO;
import org.group3.project_swp391_bookingmovieticket.dto.ScheduleDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Branch;
import org.group3.project_swp391_bookingmovieticket.entity.Schedule;
import org.group3.project_swp391_bookingmovieticket.repository.IBranchRepository;
import org.group3.project_swp391_bookingmovieticket.repository.IRoomRepository;
import org.group3.project_swp391_bookingmovieticket.service.IBranchService;
import org.group3.project_swp391_bookingmovieticket.service.IScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service
public class BranchService implements IBranchService {

    @Autowired
    private IBranchRepository branchRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IRoomRepository roomRepository;

    @Override
    public List<String> findAllLocationBranch() {
        return branchRepository.findAllLocationBranch();
    }

    @Override
    public List<BranchDTO> findAll() {
        return branchRepository.findAll()
                .stream()
                .map(branch -> modelMapper.map(branch, BranchDTO.class))
                .toList();
    }

    @Override
    public Optional<BranchDTO> findById(Integer id) {
        Branch branch = branchRepository.findById(id).orElse(null);
        return Optional.ofNullable(modelMapper.map(branch, BranchDTO.class));
    }

    @Override
    public void update(BranchDTO branchDTO) {

    }

    @Override
    public void remove(Integer id) {

    }

    @Override
    public List<BranchDTO> findBranchByLocation(String locationName) {
        return branchRepository.findByLocation(locationName)
                .stream()
                .map(branch -> modelMapper.map(branch, BranchDTO.class))
                .toList();
    }

    @Override
    public List<BranchDTO> findAllBranchesDTO() {
        return branchRepository.findAll().stream()
                .map(branch -> new BranchDTO(branch.getId(), branch.getName(), branch.getLocation()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BranchDTO> getBranchByMovie(Integer movieId) {
        // lấy về time hiện tại
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        LocalTime now = LocalTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));

        System.out.println("today: " + today + ", now: " + now);

        List<BranchDTO> branchDTOList = new ArrayList<>();
        // duyệt qua tất các branch có chiếu movie đó
        for (Branch branch : branchRepository.getBranchByMovie(movieId)) {
            BranchDTO branchDTO = modelMapper.map(branch, BranchDTO.class);
            branchDTO.setScheduleList(new ArrayList<>());
            // duyệt qua những lịch chiếu bộ phim cần tìm có trong branch đó
            for (Schedule schedule : branch.getScheduleList()) {
                // Chỉ lấy những schedule của phim có movieId đúng với movie đang tìm vì schedule có thể chứa lịch của phim khác
                if (schedule.getMovie().getId() != movieId) {
                    continue;
                }
                // Nếu time khả dụng thì add vào
                if (schedule.getStartDate().isAfter(today) ||
                        (schedule.getStartDate().isEqual(today) && schedule.getStartTime().isAfter(now))) {
                    branchDTO.getScheduleList().add(modelMapper.map(schedule, ScheduleDTO.class));
                }
            }
            // nếu như có schedule hợp lệ thì sẽ sắp xếp theo time
            if (!branchDTO.getScheduleList().isEmpty()) {
                branchDTO.setGroupedScheduleByRoomName(
                        branchDTO.getScheduleList()
                                .stream()
                                .sorted(Comparator.comparing(ScheduleDTO::getStartTime)) // sắp xếp trước
                                .collect(Collectors.groupingBy(schedule -> schedule.getRoom().getRoomType()))
                );
                branchDTOList.add(branchDTO);
            }
        }
        return branchDTOList;
    }


    @Override
    public List<BranchDTO> getBranchByStartDate(Integer movieId, String startDate) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        LocalTime now = LocalTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        if (startDate.equals("allDate")) {
            return getBranchByMovie(movieId);
        } else {
            List<BranchDTO> branchDTOList = new ArrayList<>();
            for (Branch branch : branchRepository.getBranchByStartDate(movieId, startDate)) {
                BranchDTO branchDTO = modelMapper.map(branch, BranchDTO.class);
                branchDTO.setScheduleList(new ArrayList<>());
                for (Schedule schedule : branch.getScheduleList()) {
                    // Chỉ lấy những schedule của phim có movieId đúng với movie đang tìm vì schedule có thể chứa lịch của phim khác
                    if (schedule.getMovie().getId() != movieId) {
                        continue;
                    }
                    // Nếu time khả dụng thì add vào
                    if (schedule.getStartDate().isAfter(today) ||
                            (schedule.getStartDate().isEqual(today) && schedule.getStartTime().isAfter(now))) {
                        branchDTO.getScheduleList().add(modelMapper.map(schedule, ScheduleDTO.class));
                    }
                }
                if (!branchDTO.getScheduleList().isEmpty()) {
                    branchDTO.setGroupedScheduleByRoomName(
                            branchDTO.getScheduleList()
                                    .stream()
                                    .sorted(Comparator.comparing(ScheduleDTO::getStartTime))
                                    .collect(Collectors.groupingBy(schedule -> schedule.getRoom().getRoomType()))
                    );
                    branchDTOList.add(branchDTO);
                }
            }
            return branchDTOList;
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
        // Soft delete - deactivate instead of hard delete
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isPresent()) {
            Branch existingBranch = branch.get();
            branchRepository.save(existingBranch);
        }
    }

    @Override
    public List<Branch> getBranchsByCity(String city) {
        return branchRepository.findByCity(city);
    }

    @Override
    public List<Branch> getBranchByName(String name) {
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
}