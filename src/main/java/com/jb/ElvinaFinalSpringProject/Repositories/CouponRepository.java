package com.jb.ElvinaFinalSpringProject.Repositories;

import com.jb.ElvinaFinalSpringProject.Beans.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

/***
 * Repository to manage coupons of our application in db
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    /***
     * Method to check if coupon exists by id and title
     * @param id id to use in the query
     * @param title title to use in the query
     * @return true if found match else false
     */
    boolean existsCouponByCompanyIdAndTitle(int id, String title);

    /***
     * Method to get coupons of specific company
     * @param companyId company id to use in query
     * @return list of matching coupons coupons
     */
    List<Coupon> getCouponsByCompanyId(int companyId);

    /***
     * Method to get coupons of specific company and category
     * @param companyId company id to use in query
     * @param id category id to use in query
     * @return list of matching coupons coupons
     */
    List<Coupon> getCouponsByCompanyIdAndCategoryId(int companyId, int id);

    /***
     *  Method to get coupons of specific company and max price
     * @param companyId company id to use in query
     * @param price max price to use in query
     * @return list of matching coupons coupons
     */
    List<Coupon> getCouponsByCompanyIdAndPriceLessThanEqual(int companyId, double price);

    /***
     * Method to delete all coupons with end date less than the given one
     * @param date date to use in query
     */
    void deleteByEndDateBefore(Date date);

    Coupon getCouponByIdAndCompanyId(int id, int companyId);

    List<Coupon> getCouponsByAmountGreaterThan(int amount);
}


