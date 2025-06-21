package org.group3.project_swp391_bookingmovieticket.services.impl;

import jakarta.transaction.Transactional;
import org.group3.project_swp391_bookingmovieticket.dtos.OrderDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Order;
import org.group3.project_swp391_bookingmovieticket.repositories.OrderRepository;
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

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private VoucherService voucherService;

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
    public Order createOrder(OrderDTO orderDTO) {
        logger.info("Creating order for userId: {}", orderDTO.getUserId());
        Order order = new Order(
                orderDTO.getUserId(),
                orderDTO.getBillId(),
                orderDTO.getMovieName(),
                orderDTO.getSeatId(),
                orderDTO.getPrice() != null ? orderDTO.getPrice() : BigDecimal.ZERO,
                orderDTO.getTransactionDate() != null ? orderDTO.getTransactionDate() : LocalDateTime.now(),
                orderDTO.getStatus()
        );
        Order savedOrder = orderRepository.save(order);
        logger.info("Order saved with ID: {}", savedOrder.getId());
        try {
            voucherService.generateVoucherForUser(order.getUserId());
            logger.info("Voucher generation attempted for userId: {}", order.getUserId());
        } catch (Exception e) {
            logger.error("Failed to generate voucher for userId: {}. Error: {}", order.getUserId(), e.getMessage(), e);
        }
        return savedOrder;
    }

    @Override
    public Order updateOrder(Integer id, OrderDTO orderDTO) {
        Optional<Order> existingOrder = orderRepository.findById(id);
        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();
            order.setUserId(orderDTO.getUserId());
            order.setBillId(orderDTO.getBillId());
            order.setMovieName(orderDTO.getMovieName());
            order.setSeatId(orderDTO.getSeatId());
            order.setPrice(orderDTO.getPrice() != null ? orderDTO.getPrice() : order.getPrice());
            order.setTransactionDate(orderDTO.getTransactionDate() != null ? orderDTO.getTransactionDate() : order.getTransactionDate());
            order.setStatus(orderDTO.getStatus());
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
        voucherService.generateVoucherForUser(order.getUserId());
        return savedOrder;
    }
}