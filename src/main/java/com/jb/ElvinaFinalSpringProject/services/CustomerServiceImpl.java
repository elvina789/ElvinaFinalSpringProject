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

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final BeanValidator beanValidator;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final CouponRepository couponRepository;
    private final SessionManager sessionManager;

    @Autowired
    public CustomerServiceImpl(BeanValidator beanValidator, CompanyRepository companyRepository, CustomerRepository customerRepository, CouponRepository couponRepository, SessionManager sessionManager) {
        this.beanValidator = beanValidator;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.couponRepository = couponRepository;
        this.sessionManager = sessionManager;
    }

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

    @Override
    public void logout(String token) {
        sessionManager.deleteSession(token);
    }

    @Override
    public void purchaseCoupon(int customerId, Coupon coupon) throws CustomerServiceException, InvalidCouponException {
        if (!beanValidator.validate(coupon)) {
            throw new InvalidCouponException();
        }
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
            couponRepository.save(coupon);
        } catch (RuntimeException e) {
            throw new CustomerServiceException("Something gone wrong when we tried to purchase coupon" + e.getMessage());
        }
    }

    @Override
    public List<Coupon> getCustomerCoupons(int customerId) throws CustomerServiceException {
        try {
            return customerRepository.getOne(customerId).getCoupons();
        } catch (RuntimeException e) {
            throw new CustomerServiceException("Something gone wrong when we tried to get customer coupons" + e.getMessage());
        }
    }

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

    @Override
    public Customer getCustomerDetails(int customerId) throws CustomerServiceException {
        try {
            return customerRepository.getOne(customerId);
        } catch (RuntimeException e) {
            throw new CustomerServiceException("Something gone wrong when we tried to get customer details" + e.getMessage());
        }
    }
}
