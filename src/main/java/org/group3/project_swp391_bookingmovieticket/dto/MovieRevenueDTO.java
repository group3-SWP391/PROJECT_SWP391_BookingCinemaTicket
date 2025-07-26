package org.group3.project_swp391_bookingmovieticket.dto;

import java.util.List;

public class MovieRevenueDTO {
    private final String name;
    private final Double totalRevenue;
    private final Integer ticketSold;
    private final String lastShowDate;
    private final Integer id;
    private List<Double> monthlyRevenue;

    public MovieRevenueDTO(String name, Double totalRevenue, Integer ticketSold, String lastShowDate, Integer id, List<Double> monthlyRevenue) {
        this.name = name;
        this.totalRevenue = totalRevenue;
        this.ticketSold = ticketSold;
        this.lastShowDate = lastShowDate;
        this.id = id;
        this.monthlyRevenue = monthlyRevenue;
    }

    public String getName() { return name; }
    public Double getTotalRevenue() { return totalRevenue; }
    public Integer getTicketSold() { return ticketSold; }
    public String getLastShowDate() { return lastShowDate; }
    public Integer getId() { return id; }
    public List<Double> getMonthlyRevenue() { return monthlyRevenue; }

    public void setMonthlyRevenue(List<Double> monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }
} 