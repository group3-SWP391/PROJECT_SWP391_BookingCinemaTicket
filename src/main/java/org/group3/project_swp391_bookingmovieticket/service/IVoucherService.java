package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.dto.VoucherDTO;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.entity.Voucher;

import java.util.List;

public interface IVoucherService {

    List<Voucher> getValidVouchers(Integer userId);

    Voucher applyVoucher(Integer userId, String voucherCode, double totalAmount);

    Voucher applyVoucher(Integer userId, String voucherCode, double totalAmount, String event, String ticketType, String userType);

    Voucher getVoucherByCode(String code);

    List<Voucher> getAllVouchers();

    Voucher getVoucherById(Integer id);

    List<Voucher> getVouchersByUser(User user);
}