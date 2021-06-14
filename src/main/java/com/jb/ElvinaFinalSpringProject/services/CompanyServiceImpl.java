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
import com.jb.ElvinaFinalSpringProject.security.SessionManager;
import com.jb.ElvinaFinalSpringProject.services.interfaces.CompanyService;
import com.jb.ElvinaFinalSpringProject.validation.BeanValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation for the CompanyService, that contains the logic of Company Service functions
 */

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {
    private final BeanValidator beanValidator;
    private final CompanyRepository companyRepository;
    private final CouponRepository couponRepository;
    private final SessionManager sessionManager;

    /**
     * Constructor of the CompanyService type object
     *
     * @param beanValidator     beanValidator of CompanyServiceImpl object
     * @param companyRepository companyRepository of CompanyServiceImpl object
     * @param couponRepository  couponRepository of CompanyServiceImpl object
     * @param sessionManager    sessionManager of CompanyServiceImpl object
     */
    @Autowired
    public CompanyServiceImpl(BeanValidator beanValidator, CompanyRepository companyRepository, CouponRepository couponRepository, SessionManager sessionManager) {
        this.beanValidator = beanValidator;
        this.companyRepository = companyRepository;
        this.couponRepository = couponRepository;
        this.sessionManager = sessionManager;
    }

    /**
     * Method to login Company Service
     *
     * @param email    email for login
     * @param password password for login
     * @return returns session if succseeded to login and null if not
     * @throws CompanyServiceException exception
     */
    @Override
    public Session login(String email, String password) throws CompanyServiceException {
        try {
            Company company = companyRepository.getCompanyByEmailAndPassword(email, password);
            if (company != null) {
                return sessionManager.createSession(company.getId(), ClientType.Company);
            }
            return null;
        } catch (RuntimeException e) {
            throw new CompanyServiceException("Something gone wrong when we tried to login company with email " + email + ", error- " + e.getMessage());
        }
    }

    /**
     * Method to logout Company Service
     *
     * @param token token for logout
     */
    @Override
    public void logout(String token) {
        sessionManager.deleteSession(token);
    }

    /**
     * Method to add coupon
     *
     * @param companyId company id for adding coupon
     * @param coupon    coupon to add
     * @throws CompanyServiceException exception
     * @throws InvalidCouponException  exception
     */
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

    /**
     * +
     * Method to update coupon
     *
     * @param companyId company id for updating coupon
     * @param coupon    coupon to update
     * @throws CompanyServiceException exception
     * @throws InvalidCouponException  exception
     */
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

    /**
     * Method to delete Coupon
     *
     * @param companyId company id for deleting coupon
     * @param couponID  coupon id that we want to delete
     * @throws CompanyServiceException exception
     */

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

    /**
     * Method to get Company coupons by company id
     *
     * @param companyID company id to get coupons
     * @return list of coupons
     * @throws CompanyServiceException exception
     */

    @Override
    public List<Coupon> getCompanyCoupons(int companyID) throws CompanyServiceException {
        try {
            return couponRepository.getCouponsByCompanyId(companyID);
        } catch (Exception e) {
            throw new CompanyServiceException(e);
        }
    }

    /**
     * Method to get company coupons by company id and category
     *
     * @param companyID company id for getting company coupons
     * @param category  category of the the company
     * @return List of the Coupons
     * @throws CompanyServiceException exception
     */
    @Override
    public List<Coupon> getCompanyCoupons(int companyID, Category category) throws CompanyServiceException {
        try {
            return couponRepository.getCouponsByCompanyIdAndCategoryId(companyID, category.getId());
        } catch (RuntimeException e) {
            throw new CompanyServiceException("Something gone wrong when we tried to get coupons by category " + e.getMessage());
        }
    }

    /**
     * Method to get Company coupons by company id and max price
     *
     * @param companyID company id for getting company coupons
     * @param maxPrice  max price of the coupon
     * @return List of the coupons
     * @throws CompanyServiceException exception
     */
    @Override
    public List<Coupon> getCompanyCoupons(int companyID, double maxPrice) throws CompanyServiceException {
        try {
            return couponRepository.getCouponsByCompanyIdAndPriceLessThanEqual(companyID, maxPrice);
        } catch (Exception e) {
            throw new CompanyServiceException("Something gone wrong when we tried to get coupons by max price " + e.getMessage());
        }
    }

    /**
     * Method to get Company Details by company id
     *
     * @param companyID id to get company details
     * @return the detail of the company
     * @throws CompanyServiceException exception
     */
    @Override
    public Company getCompanyDetails(int companyID) throws CompanyServiceException {
        try {
            return companyRepository.getOne(companyID);
        } catch (Exception e) {
            throw new CompanyServiceException("Something gone wrong when we tried to get company details " + e.getMessage());
        }
    }
}
