package com.jb.ElvinaFinalSpringProject.scheduler;

/**
 * Interface that contains functions for the scheduled task manager
 */
public interface ScheduledTaskManager {
    /**
     * Method used to start daily cleaning for the expired coupons
     */
    void startExpiredCouponDailyClean();

    /**
     * Method used to stop daily cleaning for the expired coupons
     */
    void stopExpiredCouponDailyClean();
}
