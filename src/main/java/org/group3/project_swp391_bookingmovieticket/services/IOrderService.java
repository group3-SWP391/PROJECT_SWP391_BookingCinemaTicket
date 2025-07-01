package org.group3.project_swp391_bookingmovieticket.services;



import org.group3.project_swp391_bookingmovieticket.dtos.OrderDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Order;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<Order> getAllOrders();
    Optional<Order> getOrderById(Integer id);

    List<Order> getPaidOrdersByUserId(Integer userId);

    Order createOrder(OrderDTO orderDTO);
    Order updateOrder(Integer id, OrderDTO orderDTO);
    void deleteOrder(Integer id);
    List<Order> getOrdersByUserId(Integer userId);
}