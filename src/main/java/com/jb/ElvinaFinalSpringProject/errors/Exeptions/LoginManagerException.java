package com.jb.ElvinaFinalSpringProject.errors.Exeptions;

public class LoginManagerException extends Exception {
    public LoginManagerException() {
        super("Something gone wrong when we tried to login");
    }

    public LoginManagerException(String message) {
        super("Something gone wrong when we tried to login - " + message);
    }
}
