package com.jb.ElvinaFinalSpringProject.security;

import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.Session;
import com.jb.ElvinaFinalSpringProject.Repositories.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TokenManagerImpl implements TokenManager {
    TokenRepository tokenRepository;

    @Autowired
    public TokenManagerImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
        new Thread(this::clearExpiredRecords).start();
    }

    @Override
    public Session createTokenRecord(int beanId, ClientType clientType) {
        log.info("Registering token of type {} for id {}", beanId, clientType);
        String token = UUID.randomUUID().toString();
        long expirationDate = DateTime.now().plusMinutes(30).getMillis();
        Session session = Session.builder()
                .token(token)
                .expirationDate(expirationDate)
                .beanId(beanId)
                .clientType(clientType.getId())
                .build();
        return tokenRepository.save(session);
    }

    @Override
    public void deleteTokenRecord(String token) {
        tokenRepository.deleteById(token);
    }

    @Override
    public Session getTokenRecord(String token) {
        return tokenRepository.getOne(token);
    }

    @Override
    public boolean validateToken(String token, ClientType clientType) {
        if (tokenRepository.existsById(token)) {
            Session session = tokenRepository.getOne(token);
            return clientType.getId() == session.getClientType() &&
                    DateTime.now().getMillis() <= session.getExpirationDate();
        }
        return false;
    }

    private void clearExpiredRecords() {
        while (true) {
            try {
                log.info("Performing cleaning of expired records");
                tokenRepository.deleteByExpirationDateLessThan(DateTime.now().getMillis());
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
