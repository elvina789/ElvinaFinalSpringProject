package com.jb.ElvinaFinalSpringProject.Login;

import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.Session;
import com.jb.ElvinaFinalSpringProject.Exeptions.LoginManagerException;

/**
 * Interface that contains login and logout functions for all clients (Admin,Company,Customer)
 */

public interface LoginManager {
    /**
     *
     * @param email
     * @param password
     * @param clientType
     * @return
     * @throws LoginManagerException
     */
    Session login(String email, String password, ClientType clientType) throws LoginManagerException;

    void logout(String token, ClientType clientType) throws LoginManagerException;
}
