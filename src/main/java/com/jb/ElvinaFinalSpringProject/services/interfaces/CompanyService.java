package com.jb.ElvinaFinalSpringProject.services.interfaces;

import com.jb.ElvinaFinalSpringProject.Beans.Company;
import com.jb.ElvinaFinalSpringProject.Beans.Coupon;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.Category;
import com.jb.ElvinaFinalSpringProject.Beans.Session;
import com.jb.ElvinaFinalSpringProject.Exeptions.CompanyServiceException;
import com.jb.ElvinaFinalSpringProject.Exeptions.InvalidCouponException;

import java.util.List;

/**
 * Interface that contains functions for Company Service
 */

public interface CompanyService {
    /**
     * Method to login Company Service
     *
     * @param email    email for login
     * @param password password for login
     * @return returns session if succseeded to login and null if not
     * @throws CompanyServiceException exception
     */
    Session login(String email, String password) throws CompanyServiceException;

    /**
     * Method to logout Company Service
     *
     * @param token token for logout
     */

    void logout(String token);

    /**
     * Method to add coupon
     *
     * @param companyId company id for adding coupon
     * @param coupon    coupon to add
     * @throws CompanyServiceException exception
     * @throws InvalidCouponException  exception
     */

    void addCoupon(int companyId, Coupon coupon) throws CompanyServiceException, InvalidCouponException;

    /**
     * +
     * Method to update coupon
     *
     * @param companyId company id for updating coupon
     * @param coupon    coupon to update
     * @throws CompanyServiceException exception
     * @throws InvalidCouponException  exception
     */
    void updateCoupon(int companyId, Coupon coupon) throws CompanyServiceException, InvalidCouponException;

    /**
     * Method to delete Coupon
     *
     * @param companyId company id for deleting coupon
     * @param couponId  coupon id that we want to delete
     * @throws CompanyServiceException exception
     */
    void deleteCoupon(int companyId, int couponId) throws CompanyServiceException;

    /**
     * Method to get Company coupons by company id
     *
     * @param companyId company id to get coupons
     * @return list of coupons
     * @throws CompanyServiceException exception
     */
    List<Coupon> getCompanyCoupons(int companyId) throws CompanyServiceException;

    /**
     * Method to get company coupons by company id and category
     *
     * @param companyId company id for getting company coupons
     * @param category  category of the the company
     * @return List of the Coupons
     * @throws CompanyServiceException exception
     */
    List<Coupon> getCompanyCoupons(int companyId, Category category) throws CompanyServiceException;

    /**
     * Method to get Company coupons by company id and max price
     *
     * @param companyId company id for getting company coupons
     * @param maxPrice  max price of the coupon
     * @return List of the coupons
     * @throws CompanyServiceException exception
     */
    List<Coupon> getCompanyCoupons(int companyId, double maxPrice) throws CompanyServiceException;

    /**
     * Method to get Company Details by company id
     *
     * @param companyId id to get company details
     * @return the detail of the company
     * @throws CompanyServiceException exception
     */
    Company getCompanyDetails(int companyId) throws CompanyServiceException;

    Coupon getCompanyCoupon(int companyId, int couponId);
}
