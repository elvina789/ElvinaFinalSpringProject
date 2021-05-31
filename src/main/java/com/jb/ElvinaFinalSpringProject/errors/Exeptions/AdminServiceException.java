package com.jb.ElvinaFinalSpringProject.errors.Exeptions;

public class AdminServiceException extends Exception {
    public AdminServiceException(String message) {
        super(message);
    }

    public AdminServiceException(Throwable cause) {
        super(cause);
    }
}
