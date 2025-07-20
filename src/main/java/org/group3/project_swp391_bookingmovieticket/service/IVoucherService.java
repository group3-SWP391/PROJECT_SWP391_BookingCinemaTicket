package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.dto.VoucherDTO;

public interface IVoucherService {
    void generateVoucherForUser(Integer userId);
    VoucherDTO getVoucherById(Long voucherId);
}