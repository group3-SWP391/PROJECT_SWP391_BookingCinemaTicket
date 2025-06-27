package org.group3.project_swp391_bookingmovieticket.services.impl;

import jakarta.transaction.Transactional;
import org.group3.project_swp391_bookingmovieticket.dtos.OrderDTO;
import org.group3.project_swp391_bookingmovieticket.entities.*;
import org.group3.project_swp391_bookingmovieticket.repositories.*;
import org.group3.project_swp391_bookingmovieticket.services.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements IOrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired private OrderRepository orderRepository;
    @Autowired private VoucherService voucherService;
    @Autowired private IUserRepository userRepository;
    @Autowired private BillRepository billRepository;
    @Autowired private SeatRepository seatRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    @Transactional
    public Order createOrder(OrderDTO dto) {
        logger.info("Creating order for userId: {}", dto.getUserId());

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Bill bill = billRepository.findById(dto.getBillId())
                .orElseThrow(() -> new IllegalArgumentException("Bill not found"));
        Seat seat = seatRepository.findById(dto.getSeatId())
                .orElseThrow(() -> new IllegalArgumentException("Seat not found"));

        Order order = new Order(
                user,
                bill,
                dto.getMovieName(), // ✅ Lưu movieName
                seat,
                dto.getPrice() != null ? dto.getPrice() : BigDecimal.ZERO,
                dto.getTransactionDate() != null ? dto.getTransactionDate() : LocalDateTime.now(),
                dto.getStatus()
        );

        Order savedOrder = orderRepository.save(order);

        try {
            voucherService.generateVoucherForUser(user.getId());
            logger.info("Voucher generation attempted for userId: {}", user.getId());
        } catch (Exception e) {
            logger.error("Failed to generate voucher for userId: {}. Error: {}", user.getId(), e.getMessage(), e);
        }

        return savedOrder;
    }

    @Override
    public Order updateOrder(Integer id, OrderDTO dto) {
        Optional<Order> optional = orderRepository.findById(id);
        if (optional.isPresent()) {
            Order order = optional.get();

            order.setUser(userRepository.findById(dto.getUserId()).orElseThrow());
            order.setBill(billRepository.findById(dto.getBillId()).orElseThrow());
            order.setMovieName(dto.getMovieName()); // ✅ Sửa movieName
            order.setSeat(seatRepository.findById(dto.getSeatId()).orElseThrow());

            order.setPrice(dto.getPrice() != null ? dto.getPrice() : order.getPrice());
            order.setTransactionDate(dto.getTransactionDate() != null ? dto.getTransactionDate() : order.getTransactionDate());
            order.setStatus(dto.getStatus());

            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found with id: " + id);
    }

    @Override
    public void deleteOrder(Integer id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        } else {
            throw new RuntimeException("Order not found with id: " + id);
        }
    }

    @Override
    public List<Order> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order saveOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        voucherService.generateVoucherForUser(order.getUser().getId());
        return savedOrder;
    }

    public boolean hasWatchedMovie(Integer userId, String movieName) {
        List<Order> orders = orderRepository.findByUserIdAndMovieName(userId, movieName);
        return orders.stream()
                .anyMatch(order -> order.getTransactionDate().isBefore(LocalDateTime.now()));
    }
}
