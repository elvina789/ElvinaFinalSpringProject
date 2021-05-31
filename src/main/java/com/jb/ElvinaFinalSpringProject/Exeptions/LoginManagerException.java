package com.jb.ElvinaFinalSpringProject.Exeptions;

public class LoginManagerException extends Exception {
    public LoginManagerException() {
        super("Something gone wrong when we tried to login");
    }

    public LoginManagerException(String message) {
        super("Something gone wrong when we tried to login - " + message);
    }
}