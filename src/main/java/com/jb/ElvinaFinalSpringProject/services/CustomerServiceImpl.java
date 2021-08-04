package com.jb.ElvinaFinalSpringProject.services;

import com.jb.ElvinaFinalSpringProject.Beans.Coupon;
import com.jb.ElvinaFinalSpringProject.Beans.Customer;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.Category;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.Session;
import com.jb.ElvinaFinalSpringProject.Exeptions.CustomerServiceException;
import com.jb.ElvinaFinalSpringProject.Exeptions.InvalidCouponException;
import com.jb.ElvinaFinalSpringProject.Repositories.CompanyRepository;
import com.jb.ElvinaFinalSpringProject.Repositories.CouponRepository;
import com.jb.ElvinaFinalSpringProject.Repositories.CustomerRepository;
import com.jb.ElvinaFinalSpringProject.security.SessionManager;
import com.jb.ElvinaFinalSpringProject.services.interfaces.CustomerService;
import com.jb.ElvinaFinalSpringProject.validation.BeanValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation for the CompanyService, that contains the logic of Customer Service functions
 */

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final BeanValidator beanValidator;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final CouponRepository couponRepository;
    private final SessionManager sessionManager;

    /**
     * Constructor of the CustomerService type object
     *
     * @param beanValidator      beanValidator of CustomerServiceImpl object
     * @param companyRepository  companyRepository of CustomerServiceImpl object
     * @param customerRepository customerRepository of CustomerServiceImpl object
     * @param couponRepository   couponRepository of CustomerServiceImpl object
     * @param sessionManager     sessionManager of CustomerServiceImpl object
     */
    @Autowired
    public CustomerServiceImpl(BeanValidator beanValidator, CompanyRepository companyRepository, CustomerRepository customerRepository, CouponRepository couponRepository, SessionManager sessionManager) {
        this.beanValidator = beanValidator;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.couponRepository = couponRepository;
        this.sessionManager = sessionManager;
    }

    /**
     * Method to login Customer Service
     *
     * @param email    email for login
     * @param password password for login
     * @return returns session if succseeded to login and null if not
     * @throws CustomerServiceException exception
     */
    @Override
    public Session login(String email, String password) throws CustomerServiceException {
        try {
            Customer customer = customerRepository.getCustomerByEmailAndPassword(email, password);
            if (customer != null) {
                return sessionManager.createSession(customer.getId(), ClientType.Customer);
            }
            return null;
        } catch (Exception e) {
            throw new CustomerServiceException("Something gone wrong when we tried to login customer with email " + email + ", error - " + e.getMessage());
        }
    }

    /**
     * Method to logout Customer Service
     *
     * @param token token for logout
     */
    @Override
    public void logout(String token) {
        sessionManager.deleteSession(token);
    }

    /**
     * Method to purchase coupon
     *
     * @param customerId customer id
     * @param coupon     coupon to purchase
     * @throws CustomerServiceException exception
     * @throws InvalidCouponException   exception
     */
    @Override
    public void purchaseCoupon(int customerId, Coupon coupon) throws CustomerServiceException, InvalidCouponException {
        beanValidator.validate(coupon);
        try {
            Coupon couponInDb = couponRepository.getOne(coupon.getId());
            Customer customer = customerRepository.getOne(customerId);

            boolean alreadyPurchased = customer.getCoupons() != null &&
                    customer.getCoupons().stream().anyMatch(e -> e.getId() == coupon.getId());

            if (alreadyPurchased) {
                throw new CustomerServiceException("Customer - " + customerId + " already purchased coupon - " + coupon.getId());
            }

            if (couponInDb.getAmount() <= 0) {
                throw new CustomerServiceException("There is not enough amount  for coupon - " + coupon.getId());
            }

            if (couponInDb.getEndDate().getTime() <= System.currentTimeMillis()) {
                throw new CustomerServiceException("The coupon - " + coupon.getId() + " is expired");
            }

            if (customer.getCoupons() == null) {
                customer.setCoupons(new ArrayList<>());
            }

            customer.getCoupons().add(couponInDb);
            customerRepository.save(customer);
            couponInDb.setAmount(couponInDb.getAmount() - 1);
            couponRepository.save(couponInDb);
        } catch (RuntimeException e) {
            throw new CustomerServiceException("Something gone wrong when we tried to purchase coupon" + e.getMessage());
        }
    }

    /**
     * Method to get customer coupons by customer id
     *
     * @param customerId customer id
     * @return List of coupons
     * @throws CustomerServiceException exception
     */
    @Override
    public List<Coupon> getCustomerCoupons(int customerId) throws CustomerServiceException {
        try {
            return customerRepository.getOne(customerId).getCoupons();
        } catch (RuntimeException e) {
            throw new CustomerServiceException("Something gone wrong when we tried to get customer coupons" + e.getMessage());
        }
    }

    /**
     * Method to get customer coupons by customer id and category
     *
     * @param customerId customer id for coupons
     * @param category   category
     * @return List of coupons
     * @throws CustomerServiceException exception
     */
    @Override
    public List<Coupon> getCustomerCoupons(int customerId, Category category) throws CustomerServiceException {
        try {
            return customerRepository.getOne(customerId)
                    .getCoupons().stream()
                    .filter(e -> e.getCategoryId() == category.getId())
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new CustomerServiceException("Something gone wrong when we tried to get customer coupons by category" + e.getMessage());
        }
    }

    /**
     * Method to get customer coupons by customer id and max price
     *
     * @param customerId customer id for coupons
     * @param maxPrice   max price of the coupon
     * @return List of coupons
     * @throws CustomerServiceException exception
     */
    @Override
    public List<Coupon> getCustomerCoupons(int customerId, double maxPrice) throws CustomerServiceException {
        try {
            return customerRepository.getOne(customerId)
                    .getCoupons().stream()
                    .filter(e -> e.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new CustomerServiceException("Something gone wrong when we tried to get customer coupons by max price" + e.getMessage());
        }
    }

    /**
     * Method to get customer details
     *
     * @param customerId customer id to get details
     * @return details of the customer
     * @throws CustomerServiceException exception
     */
    @Override
    public Customer getCustomerDetails(int customerId) throws CustomerServiceException {
        try {
            return customerRepository.getOne(customerId);
        } catch (RuntimeException e) {
            throw new CustomerServiceException("Something gone wrong when we tried to get customer details" + e.getMessage());
        }
    }

    @Override
    public List<Coupon> getAvailableCoupons() throws CustomerServiceException {
        try {
            return couponRepository.getCouponsByAmountGreaterThan(0);
        } catch (RuntimeException e) {
            throw new CustomerServiceException("Something gone wrong when we tried to get available coupons" + e.getMessage());
        }
    }
}
