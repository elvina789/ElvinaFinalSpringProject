package com.jb.ElvinaFinalSpringProject.services.interfaces;

import com.jb.ElvinaFinalSpringProject.Beans.Coupon;
import com.jb.ElvinaFinalSpringProject.Beans.Customer;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.Category;
import com.jb.ElvinaFinalSpringProject.Beans.TokenRecord;
import com.jb.ElvinaFinalSpringProject.Exeptions.CustomerServiceException;
import com.jb.ElvinaFinalSpringProject.Exeptions.InvalidCouponException;

import java.util.List;

public interface CustomerService {
    TokenRecord login(String email, String password) throws CustomerServiceException;

    void logout(String token);

    void purchaseCoupon(int customerId, Coupon coupon) throws CustomerServiceException, InvalidCouponException;

    List<Coupon> getCustomerCoupons(int customerId) throws CustomerServiceException;

    List<Coupon> getCustomerCoupons(int customerId, Category category) throws CustomerServiceException;

    List<Coupon> getCustomerCoupons(int customerId, double maxPrice) throws CustomerServiceException;

    Customer getCustomerDetails(int customerId) throws CustomerServiceException;
}
