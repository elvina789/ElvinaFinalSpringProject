package com.jb.ElvinaFinalSpringProject.Repositories;

import com.jb.ElvinaFinalSpringProject.Beans.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/***
 * Repository to manage customers of our application in db
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    /***
     * Method used to check if customer exists by given email
     * @param email email used in query
     * @return true if found match else false
     */
    boolean existsCustomerByEmail(String email);

    /***
     * Method used to get customer by email and password
     * @param email email used in query
     * @param password password used in query
     * @return matching customer
     */
    Customer getCustomerByEmailAndPassword(String email, String password);
}
