package com.jb.ElvinaFinalSpringProject.services;

import com.jb.ElvinaFinalSpringProject.Beans.Company;
import com.jb.ElvinaFinalSpringProject.Beans.Coupon;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.Category;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.Session;
import com.jb.ElvinaFinalSpringProject.Exeptions.CompanyServiceException;
import com.jb.ElvinaFinalSpringProject.Exeptions.InvalidCouponException;
import com.jb.ElvinaFinalSpringProject.Repositories.CompanyRepository;
import com.jb.ElvinaFinalSpringProject.Repositories.CouponRepository;
import com.jb.ElvinaFinalSpringProject.security.TokenManager;
import com.jb.ElvinaFinalSpringProject.services.interfaces.CompanyService;
import com.jb.ElvinaFinalSpringProject.validation.BeanValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {
    private final BeanValidator beanValidator;
    private final CompanyRepository companyRepository;
    private final CouponRepository couponRepository;
    private final TokenManager tokenManager;


    @Autowired
    public CompanyServiceImpl(BeanValidator beanValidator, CompanyRepository companyRepository, CouponRepository couponRepository, TokenManager tokenManager) {
        this.beanValidator = beanValidator;
        this.companyRepository = companyRepository;
        this.couponRepository = couponRepository;
        this.tokenManager = tokenManager;
    }

    @Override
    public Session login(String email, String password) throws CompanyServiceException {
        try {
            Company company = companyRepository.getCompanyByEmailAndPassword(email, password);
            if (company != null) {
                return tokenManager.createTokenRecord(company.getId(), ClientType.Company);
            }
            return null;
        } catch (RuntimeException e) {
            throw new CompanyServiceException("Something gone wrong when we tried to login company with email " + email + ", error- " + e.getMessage());
        }
    }

    @Override
    public void logout(String token) {
        tokenManager.deleteTokenRecord(token);
    }

    @Override
    public void addCoupon(int companyId, Coupon coupon) throws CompanyServiceException, InvalidCouponException {
        if (!beanValidator.validate(coupon)) {
            throw new InvalidCouponException();
        }

        if (coupon.getCompanyId() != companyId) {
            throw new InvalidCouponException();
        }

        try {
            if (!couponRepository.existsCouponByCompanyIdAndTitle(coupon.getCompanyId(), coupon.getTitle())) {
                couponRepository.save(coupon);
            } else {
                throw new CompanyServiceException("Coupon with title - " + coupon.getTitle() + " for company with id - " + coupon.getCompanyId() + " already exists");
            }
        } catch (RuntimeException e) {
            throw new CompanyServiceException("Something gone wrong during addCoupon()" + e.getMessage());
        }
    }

    @Override
    public void updateCoupon(int companyId, Coupon coupon) throws CompanyServiceException, InvalidCouponException {
        if (!beanValidator.validate(coupon)) {
            throw new InvalidCouponException();
        }

        if (coupon.getCompanyId() != companyId) {
            throw new InvalidCouponException();
        }

        try {
            if (!couponRepository.existsById(coupon.getId())) {
                throw new CompanyServiceException("Coupon with id - " + coupon.getId() + " does not exist");
            }
            Coupon couponIdDb = couponRepository.getOne(coupon.getId());
            if (couponIdDb.getCompanyId() != coupon.getCompanyId()) {
                throw new CompanyServiceException("Updating company id for coupon is forbidden");
            }
            couponRepository.save(coupon);
        } catch (RuntimeException e) {
            throw new CompanyServiceException("Something gone wrong when we tried to update  coupon " + e.getMessage());
        }
    }

    @Override
    public void deleteCoupon(int companyId, int couponID) throws CompanyServiceException {
        try {
            if (couponRepository.existsById(couponID)) {
                Coupon coupon = couponRepository.getOne(couponID);
                if (companyId == coupon.getCompanyId()) {
                    couponRepository.deleteById(couponID);
                } else {
                    throw new CompanyServiceException("Coupon - " + couponID + " not available for company " + companyId);
                }
            } else {
                throw new CompanyServiceException("Coupon with id - " + couponID + " does not exist");
            }
        } catch (RuntimeException e) {
            throw new CompanyServiceException("Something gone wrong when we tried to delete coupon " + e.getMessage());
        }
    }


    @Override
    public List<Coupon> getCompanyCoupons(int companyID) throws CompanyServiceException {
        try {
            return couponRepository.getCouponsByCompanyId(companyID);
        } catch (Exception e) {
            throw new CompanyServiceException(e);
        }
    }

    @Override
    public List<Coupon> getCompanyCoupons(int companyID, Category category) throws CompanyServiceException {
        try {
            return couponRepository.getCouponsByCompanyIdAndCategoryId(companyID, category.getId());
        } catch (RuntimeException e) {
            throw new CompanyServiceException("Something gone wrong when we tried to get coupons by category " + e.getMessage());
        }
    }

    @Override
    public List<Coupon> getCompanyCoupons(int companyID, double maxPrice) throws CompanyServiceException {
        try {
            return couponRepository.getCouponsByCompanyIdAndPriceLessThanEqual(companyID, maxPrice);
        } catch (Exception e) {
            throw new CompanyServiceException("Something gone wrong when we tried to get coupons by max price " + e.getMessage());
        }
    }

    @Override
    public Company getCompanyDetails(int companyID) throws CompanyServiceException {
        try {
            return companyRepository.getOne(companyID);
        } catch (Exception e) {
            throw new CompanyServiceException("Something gone wrong when we tried to get company details " + e.getMessage());
        }
    }
}
