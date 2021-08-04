package com.jb.ElvinaFinalSpringProject.Exeptions;

public class InvalidCouponException extends RuntimeException {
    public InvalidCouponException() {
        super("The coupon is invalid");
    }

    public InvalidCouponException(String message) {
        super(message);
    }
}
