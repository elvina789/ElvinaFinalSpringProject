package com.jb.ElvinaFinalSpringProject.Exeptions;

public class AdminServiceException extends Exception {
    public AdminServiceException(String message) {
        super(message);
    }

    public AdminServiceException(Throwable cause) {
        super(cause);
    }
}
