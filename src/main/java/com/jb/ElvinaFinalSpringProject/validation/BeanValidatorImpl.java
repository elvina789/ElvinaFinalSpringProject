package com.jb.ElvinaFinalSpringProject.validation;

import com.jb.ElvinaFinalSpringProject.Beans.Company;
import com.jb.ElvinaFinalSpringProject.Beans.Coupon;
import com.jb.ElvinaFinalSpringProject.Beans.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/***
 * Utility class used to validate beans in our application
 */
@Component
@Slf4j
public class BeanValidatorImpl implements BeanValidator {
    /**
     * Method used to validate company bean
     *
     * @param company company to validate
     * @return true if company is valid else false
     */
    @Override
    public boolean validate(Company company) {
        boolean validationResult = company != null &&
                company.getEmail() != null &&
                company.getName() != null &&
                company.getPassword() != null;
        if (!validationResult) {
            log.warn("Company validation failed");
        }
        return validationResult;
    }

    /**
     * Method used to validate customer bean
     *
     * @param customer customer to validate
     * @return true if customer is valid else false
     */
    @Override
    public boolean validate(Customer customer) {
        boolean validationResult = customer != null &&
                customer.getEmail() != null &&
                customer.getFirstName() != null &&
                customer.getLastName() != null &&
                customer.getPassword() != null;
        if (!validationResult) {
            log.warn("Customer validation failed");
        }
        return validationResult;
    }

    /**
     * Method used to validate coupon bean
     *
     * @param coupon coupon to validate
     * @return true if coupon is valid else false
     */
    @Override
    public boolean validate(Coupon coupon) {
        boolean validationResult = coupon != null &&
                coupon.getDescription() != null &&
                coupon.getStartDate() != null &&
                coupon.getEndDate() != null &&
                coupon.getImage() != null &&
                coupon.getTitle() != null &&
                coupon.getCompanyId() > 0 &&
                coupon.getCategoryId() > 0;
        if (!validationResult) {
            log.warn("Coupon validation failed");
        }
        return validationResult;
    }
}
