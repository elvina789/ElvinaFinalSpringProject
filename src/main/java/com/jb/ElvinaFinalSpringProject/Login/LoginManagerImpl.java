package com.jb.ElvinaFinalSpringProject.Login;

import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.TokenRecord;
import com.jb.ElvinaFinalSpringProject.Exeptions.LoginManagerException;
import com.jb.ElvinaFinalSpringProject.services.interfaces.AdminService;
import com.jb.ElvinaFinalSpringProject.services.interfaces.CompanyService;
import com.jb.ElvinaFinalSpringProject.services.interfaces.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class LoginManagerImpl implements LoginManager {
    private final ApplicationContext applicationContext;

    @Autowired
    public LoginManagerImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public TokenRecord login(String email, String password, ClientType clientType) throws LoginManagerException {
        try {
            switch (clientType) {
                case Company:
                    return applicationContext.getBean(CompanyService.class).login(email, password);
                case Administrator:
                    return applicationContext.getBean(AdminService.class).login(email, password);
                case Customer:
                    return applicationContext.getBean(CustomerService.class).login(email, password);
                default:
                    throw new LoginManagerException();
            }
        } catch (Exception e) {
            throw new LoginManagerException(e.getMessage());
        }
    }

    @Override
    public void logout(String token, ClientType clientType) throws LoginManagerException {
        try {
            switch (clientType) {
                case Company:
                    applicationContext.getBean(CompanyService.class).logout(token);
                    break;
                case Administrator:
                    applicationContext.getBean(AdminService.class).logout(token);
                    break;
                case Customer:
                    applicationContext.getBean(CustomerService.class).logout(token);
                    break;
                default:
                    throw new LoginManagerException();
            }
        } catch (Exception e) {
            throw new LoginManagerException(e.getMessage());
        }
    }
}
