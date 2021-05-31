package com.jb.ElvinaFinalSpringProject.scheduler;

public interface ScheduledTaskManager {
    void startExpiredCouponDailyClean();

    void stopExpiredCouponDailyClean();
}
