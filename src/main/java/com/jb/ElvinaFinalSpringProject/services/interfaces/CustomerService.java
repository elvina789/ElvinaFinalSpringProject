package com.jb.ElvinaFinalSpringProject.services.interfaces;

import com.jb.ElvinaFinalSpringProject.Beans.Coupon;
import com.jb.ElvinaFinalSpringProject.Beans.Customer;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.Category;
import com.jb.ElvinaFinalSpringProject.Beans.Session;
import com.jb.ElvinaFinalSpringProject.Exeptions.CompanyServiceException;
import com.jb.ElvinaFinalSpringProject.Exeptions.CustomerServiceException;
import com.jb.ElvinaFinalSpringProject.Exeptions.InvalidCouponException;

import java.util.List;

/**
 * Interface that contains functions for Customer Service
 */

public interface CustomerService {

    /**
     * Method to login Customer Service
     * @param email email for login
     * @param password password for login
     * @return returns session if succseeded to login and null if not
     * @throws CustomerServiceException exception
     */
    Session login(String email, String password) throws CustomerServiceException;

    /**
     * Method to logout Customer Service
     * @param token token for logout
     */
    void logout(String token);

    /**
     * Method to purchase coupon
     * @param customerId customer id
     * @param coupon coupon to purchase
     * @throws CustomerServiceException exception
     * @throws InvalidCouponException exception
     */
    void purchaseCoupon(int customerId, Coupon coupon) throws CustomerServiceException, InvalidCouponException;

    /**
     * Method to get customer coupons by customer id
     * @param customerId customer id
     * @return List of coupons
     * @throws CustomerServiceException exception
     */

    List<Coupon> getCustomerCoupons(int customerId) throws CustomerServiceException;

    /**
     * Method to get customer coupons by customer id and category
     * @param customerId customer id for coupons
     * @param category category
     * @return List of coupons
     * @throws CustomerServiceException exception
     */
    List<Coupon> getCustomerCoupons(int customerId, Category category) throws CustomerServiceException;

    /**
     * Method to get customer coupons by customer id and max price
     * @param customerId customer id for coupons
     * @param maxPrice max price of the coupon
     * @return List of coupons
     * @throws CustomerServiceException exception
     */
    List<Coupon> getCustomerCoupons(int customerId, double maxPrice) throws CustomerServiceException;

    /**
     * Method to get customer details
     * @param customerId customer id to get details
     * @return details of the customer
     * @throws CustomerServiceException exception
     */
    Customer getCustomerDetails(int customerId) throws CustomerServiceException;
}
