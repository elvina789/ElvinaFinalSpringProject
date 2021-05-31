package com.jb.ElvinaFinalSpringProject.Repositories;

import com.jb.ElvinaFinalSpringProject.Beans.TokenRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/***
 * Repository to manage login tokens of our application in db
 */
@Repository
public interface TokenRepository extends JpaRepository<TokenRecord, String> {
    /***
     * Method used to delete tokens which passed expiration date
     * @param expirationDate expiration date used in the delete query
     */
    @Transactional
    void deleteByExpirationDateLessThan(long expirationDate);
}
