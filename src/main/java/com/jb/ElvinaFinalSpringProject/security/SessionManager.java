package com.jb.ElvinaFinalSpringProject.security;

import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.Session;

public interface SessionManager {
    Session createSession(int beanId, ClientType clientType);

    void deleteSession(String token);

    Session getSession(String token);

    void validateToken(String token, ClientType clientType);
}
