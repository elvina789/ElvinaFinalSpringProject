package com.jb.ElvinaFinalSpringProject.security;

import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.Session;
import com.jb.ElvinaFinalSpringProject.Exeptions.InvalidTokenException;
import com.jb.ElvinaFinalSpringProject.Repositories.CompanyRepository;
import com.jb.ElvinaFinalSpringProject.Repositories.CustomerRepository;
import com.jb.ElvinaFinalSpringProject.Repositories.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Implementation for the SessionManager, that contains the logic to manage sessions
 */
@Slf4j
@Component
public class SessionManagerImpl implements SessionManager {
    SessionRepository sessionRepository;
    CompanyRepository companyRepository;
    CustomerRepository customerRepository;

    /**
     * Constructor of the SessionManagerImpl object
     * @param sessionRepository sessionRepository of the SessionManagerImpl
     */
    @Autowired
    public SessionManagerImpl(SessionRepository sessionRepository, CompanyRepository companyRepository, CustomerRepository customerRepository) {
        this.sessionRepository = sessionRepository;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        new Thread(this::clearExpiredRecords).start();
    }
    /**
     * Method used to create session
     * @param beanId id used in query
     * @param clientType client type used in query
     * @return
     */
    @Override
    public Session createSession(int beanId, ClientType clientType) {
        log.info("Registering token of type {} for id {}", beanId, clientType);
        String token = UUID.randomUUID().toString();
        long expirationDate = DateTime.now().plusMinutes(30).getMillis();
        Session session = Session.builder()
                .token(token)
                .expirationDate(expirationDate)
                .beanId(beanId)
                .clientType(clientType.getId())
                .build();
        return sessionRepository.save(session);
    }
    /**
     * Method used to delete session
     * @param token token used in query
     */
    @Override
    public void deleteSession(String token) {
        sessionRepository.deleteById(token);
    }
    /**
     * Method used to get session
     * @param token token used in query
     * @return
     */
    @Override
    public Session getSession(String token) {
        return sessionRepository.getOne(token);
    }

    /**
     * Method used to validate token
     * @param token token in query
     * @param clientType client type used in query
     */
    @Override
    public void validateToken(String token, ClientType clientType) {
        boolean valid = false;
        if (sessionRepository.existsById(token)) {
            Session session = sessionRepository.getOne(token);
            valid = clientType.getId() == session.getClientType() && DateTime.now().getMillis() <= session.getExpirationDate();
            if (valid) {
                if (clientType.equals(ClientType.Customer)) {
                    valid = customerRepository.existsById(session.getBeanId());
                } else if (clientType.equals(ClientType.Company)) {
                    valid = companyRepository.existsById(session.getBeanId());
                }
            }
        }
        if (!valid) {
            throw new InvalidTokenException();
        }
    }

    /**
     *  Method used to clear expired records
     */
    private void clearExpiredRecords() {
        while (true) {
            try {
                log.info("Performing cleaning of expired records");
                sessionRepository.deleteByExpirationDateLessThan(DateTime.now().getMillis());
            } catch (Exception e) {
                log.error("Something gone wrong during expired records cleanup, error - {}", e.getMessage());
            } finally {
                try {
                    TimeUnit.SECONDS.sleep(30);
                } catch (InterruptedException e) {
                    log.warn(e.getMessage());
                }
            }
        }
    }
}
