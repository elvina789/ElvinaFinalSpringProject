package com.jb.ElvinaFinalSpringProject.scheduler;

import com.jb.ElvinaFinalSpringProject.Repositories.CouponRepository;
import com.jb.ElvinaFinalSpringProject.Repositories.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

/**
 * Implementation for the ScheduledTaskManager, that contains the logic of daily cleaning job
 */
@Slf4j
@Component
public class ScheduledTaskManagerImpl implements ScheduledTaskManager {
    private final CouponRepository couponRepository;
    private boolean cleanExpiredCoupons;

    /**
     * Constructor of ScheduledTaskManagerImpl object
     * @param sessionRepository sessionRepository of the ScheduledTaskManagerImpl type
     * @param couponRepository couponRepository of the ScheduledTaskManagerImpl type
     */
    public ScheduledTaskManagerImpl(SessionRepository sessionRepository, CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
        this.cleanExpiredCoupons = true;
        Thread cleanExpiredCouponsThread = new Thread(this::clearExpiredCoupons);
        cleanExpiredCouponsThread.start();
    }

    /**
     * Method used to clear expired coupons
     */
    private void clearExpiredCoupons() {
        while (true) {
            try {
                if (cleanExpiredCoupons) {
                    log.info("Performing cleaning of expired coupons");
                    Date currentDate = new Date(DateTime.now().getMillis());
                    couponRepository.deleteByEndDateBefore(currentDate);
                }
            } catch (Exception e) {
                log.error("Something gone wrong during expired records cleanup, error - {}", e.getMessage());
            } finally {
                try {
                    log.info("Sleeping for one day before next cleaning");
                    TimeUnit.DAYS.sleep(1);
                } catch (InterruptedException e) {
                    log.warn(e.getMessage());
                }
            }
        }
    }

    /**
     * Method used to start daily cleaning for the expired coupons
     */
    @Override
    public void startExpiredCouponDailyClean() {
        log.info("Starting clean expired coupons daily job");
        this.cleanExpiredCoupons = true;
    }

    /**
     * Method used to stop daily cleaning for the expired coupons
     */
    @Override
    public void stopExpiredCouponDailyClean() {
        log.info("Stopping clean expired coupons daily job");
        this.cleanExpiredCoupons = false;
    }
}
