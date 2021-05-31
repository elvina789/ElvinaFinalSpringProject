package com.jb.ElvinaFinalSpringProject.Login;

import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.TokenRecord;
import com.jb.ElvinaFinalSpringProject.errors.Exeptions.LoginManagerException;

public interface LoginManager {
    TokenRecord login(String email, String password, ClientType clientType) throws LoginManagerException;

    void logout(String token, ClientType clientType) throws LoginManagerException;
}
