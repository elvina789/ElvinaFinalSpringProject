package com.jb.ElvinaFinalSpringProject.security;

import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.Session;

public interface TokenManager {
    Session createTokenRecord(int beanId, ClientType clientType);

    void deleteTokenRecord(String token);

    Session getTokenRecord(String token);

    boolean validateToken(String token, ClientType clientType);
}
