package com.jb.ElvinaFinalSpringProject.Exeptions;

/**
 * Interface that contains functions for companies CRUD
 */
public class LoginManagerException extends RuntimeException {
    public LoginManagerException() {
        super("Something gone wrong when we tried to login");
    }

    public LoginManagerException(String message) {
        super("Something gone wrong when we tried to login - " + message);
    }
}
