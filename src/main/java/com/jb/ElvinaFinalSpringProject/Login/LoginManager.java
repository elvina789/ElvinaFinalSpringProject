package com.jb.ElvinaFinalSpringProject.Login;

import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.Session;
import com.jb.ElvinaFinalSpringProject.Exeptions.LoginManagerException;

public interface LoginManager {
    Session login(String email, String password, ClientType clientType) throws LoginManagerException;

    void logout(String token, ClientType clientType) throws LoginManagerException;
}
