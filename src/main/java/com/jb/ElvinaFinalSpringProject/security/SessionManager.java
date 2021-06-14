package com.jb.ElvinaFinalSpringProject.security;

import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.Session;

/**
 * Interface that contains functions for session manager
 */

public interface SessionManager {
    /**
     * Method used to create session
     * @param beanId id used in query
     * @param clientType client type used in query
     * @return
     */
    Session createSession(int beanId, ClientType clientType);

    /**
     * Method used to delete session
     * @param token token used in query
     */
    void deleteSession(String token);

    /**
     * Method used to get session
     * @param token token used in query
     * @return
     */
    Session getSession(String token);

    /**
     * Method used to validate token
     * @param token token in query
     * @param clientType client type used in query
     */
    void validateToken(String token, ClientType clientType);
}
