package com.jb.ElvinaFinalSpringProject.errors.Exeptions;

public class InvalidCouponException extends Exception {
    public InvalidCouponException() {
        super("The coupon is invalid");
    }
}
