package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.dtos.VoucherDTO;

public interface IVoucherService {
    void generateVoucherForUser(Integer userId);
    VoucherDTO getVoucherById(Long voucherId);
}