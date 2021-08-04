package com.jb.ElvinaFinalSpringProject.validation;

import com.jb.ElvinaFinalSpringProject.Beans.Company;
import com.jb.ElvinaFinalSpringProject.Beans.Coupon;
import com.jb.ElvinaFinalSpringProject.Beans.Customer;

/***
 * Interface for utility used to validate beans in our application
 */
public interface BeanValidator {
    /**
     * Method used to validate company bean
     *
     * @param company company to validate
     * @return true if company is valid else false
     */
    void validate(Company company);

    /**
     * Method used to validate customer bean
     *
     * @param customer customer to validate
     * @return true if customer is valid else false
     */
    void validate(Customer customer);

    /**
     * Method used to validate coupon bean
     *
     * @param coupon coupon to validate
     * @return true if coupon is valid else false
     */
    void validate(Coupon coupon);
}
