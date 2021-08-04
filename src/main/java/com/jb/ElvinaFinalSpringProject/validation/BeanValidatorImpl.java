package com.jb.ElvinaFinalSpringProject.validation;

import com.jb.ElvinaFinalSpringProject.Beans.Company;
import com.jb.ElvinaFinalSpringProject.Beans.Coupon;
import com.jb.ElvinaFinalSpringProject.Beans.Customer;
import com.jb.ElvinaFinalSpringProject.Exeptions.InvalidCompanyException;
import com.jb.ElvinaFinalSpringProject.Exeptions.InvalidCouponException;
import com.jb.ElvinaFinalSpringProject.Exeptions.InvalidCustomerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/***
 * Utility class used to validate beans in our application
 */
@Component
@Slf4j
public class BeanValidatorImpl implements BeanValidator {
    Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");

    /**
     * Method used to validate company bean
     *
     * @param company company to validate
     * @return true if company is valid else false
     */
    @Override
    public void validate(Company company) {
        List<String> violations = new ArrayList<>();
        if (company != null) {
            if (StringUtils.isEmpty(company.getEmail()) || !pattern.matcher(company.getEmail()).matches()) {
                violations.add("Company email is invalid");
            }
            if (StringUtils.isEmpty(company.getName())) {
                violations.add("Company name is invalid");
            }
            if (StringUtils.isEmpty(company.getPassword())) {
                violations.add("Company password is invalid");
            }
        } else {
            violations.add("Company cannot be null");
        }
        if (!violations.isEmpty()) {
            String message = "\nCustomer is invalid please fix the following:\n";
            for (String violation : violations) {
                message += "\n* " + violation;
            }
            throw new InvalidCompanyException(message);
        }
    }

    /**
     * Method used to validate customer bean
     *
     * @param customer customer to validate
     * @return true if customer is valid else false
     */
    @Override
    public void validate(Customer customer) {
        List<String> violations = new ArrayList<>();
        if (customer != null) {
            if (StringUtils.isEmpty(customer.getEmail()) || !pattern.matcher(customer.getEmail()).matches()) {
                violations.add("Customer email is invalid");
            }
            if (StringUtils.isEmpty(customer.getFirstName())) {
                violations.add("Customer first name is invalid");
            }
            if (StringUtils.isEmpty(customer.getLastName())) {
                violations.add("Customer last name is invalid");
            }
            if (StringUtils.isEmpty(customer.getPassword())) {
                violations.add("Customer password is invalid");
            }
        } else {
            violations.add("Customer cannot be null");
        }

        if (!violations.isEmpty()) {
            String message = "\nCustomer is invalid please fix the following:\n";
            for (String violation : violations) {
                message += "\n* " + violation;
            }
            throw new InvalidCustomerException(message);
        }
    }

    /**
     * Method used to validate coupon bean
     *
     * @param coupon coupon to validate
     * @return true if coupon is valid else false
     */
    @Override
    public void validate(Coupon coupon) {
        List<String> violations = new ArrayList<>();
        if (coupon != null) {
            if (StringUtils.isEmpty(coupon.getDescription())) {
                violations.add("Coupon description is invalid");
            }
            if (coupon.getStartDate() == null) {
                violations.add("Coupon start date is invalid");
            }
            if (coupon.getEndDate() == null) {
                violations.add("Coupon end date is invalid");
            }
            if (coupon.getStartDate() != null && coupon.getEndDate() != null && coupon.getStartDate().getTime() > coupon.getEndDate().getTime()) {
                violations.add("Coupon start date cannot be greater than end date");
            }
            if (StringUtils.isEmpty(coupon.getImage())) {
                violations.add("Coupon image is invalid");
            }
            if (StringUtils.isEmpty(coupon.getTitle())) {
                violations.add("Coupon title is invalid");
            }
            if (coupon.getCompanyId() <= 0) {
                violations.add("Coupon company id is invalid");
            }
            if (coupon.getCategoryId() <= 0) {
                violations.add("Coupon category id is invalid");
            }
        } else {
            violations.add("Coupon cannot be null");
        }

        if (!violations.isEmpty()) {
            String message = "\nCustomer is invalid please fix the following:\n";
            for (String violation : violations) {
                message += "\n * " + violation;
            }
            throw new InvalidCouponException(message);
        }
    }
}
