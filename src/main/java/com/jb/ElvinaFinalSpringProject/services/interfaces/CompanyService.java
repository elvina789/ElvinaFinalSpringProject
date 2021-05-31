package com.jb.ElvinaFinalSpringProject.services.interfaces;

import com.jb.ElvinaFinalSpringProject.Beans.Company;
import com.jb.ElvinaFinalSpringProject.Beans.Coupon;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.Category;
import com.jb.ElvinaFinalSpringProject.Beans.TokenRecord;
import com.jb.ElvinaFinalSpringProject.Exeptions.CompanyServiceException;
import com.jb.ElvinaFinalSpringProject.Exeptions.InvalidCouponException;

import java.util.List;

public interface CompanyService {
    TokenRecord login(String email, String password) throws CompanyServiceException;

    void logout(String token);

    void addCoupon(int companyId, Coupon coupon) throws CompanyServiceException, InvalidCouponException;

    void updateCoupon(int companyId, Coupon coupon) throws CompanyServiceException, InvalidCouponException;

    void deleteCoupon(int companyId, int couponId) throws CompanyServiceException;

    List<Coupon> getCompanyCoupons(int companyId) throws CompanyServiceException;

    List<Coupon> getCompanyCoupons(int companyId, Category category) throws CompanyServiceException;

    List<Coupon> getCompanyCoupons(int companyId, double maxPrice) throws CompanyServiceException;

    Company getCompanyDetails(int companyId) throws CompanyServiceException;
}
