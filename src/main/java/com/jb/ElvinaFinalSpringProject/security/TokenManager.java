package com.jb.ElvinaFinalSpringProject.security;

import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.TokenRecord;

public interface TokenManager {
    TokenRecord createTokenRecord(int beanId, ClientType clientType);

    void deleteTokenRecord(String token);

    TokenRecord getTokenRecord(String token);

    boolean validateToken(String token, ClientType clientType);
}
