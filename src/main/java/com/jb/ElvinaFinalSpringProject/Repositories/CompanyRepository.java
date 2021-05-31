package com.jb.ElvinaFinalSpringProject.Repositories;

import com.jb.ElvinaFinalSpringProject.Beans.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/***
 * Repository to manage companies of our application in db
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    /***
     * Method used to check if customer exists by name or email
     * @param name name used in query
     * @param email email used in query
     * @return true if found match else false
     */
    boolean existsCompanyByNameOrEmail(String name, String email);

    /***
     * Method used to get company by email and password
     * @param email email used in query
     * @param password password used in query
     * @return true if found match else false
     */
    Company getCompanyByEmailAndPassword(String email, String password);
}

