package com.jb.ElvinaFinalSpringProject.scheduler;

import com.jb.ElvinaFinalSpringProject.Repositories.CouponRepository;
import com.jb.ElvinaFinalSpringProject.Repositories.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ScheduledTaskManagerImpl implements ScheduledTaskManager {
    private final CouponRepository couponRepository;
    private boolean cleanExpiredCoupons;

    public ScheduledTaskManagerImpl(SessionRepository sessionRepository, CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
        this.cleanExpiredCoupons = true;
        Thread cleanExpiredCouponsThread = new Thread(this::clearExpiredCoupons);
        cleanExpiredCouponsThread.start();
    }

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

    @Override
    public void startExpiredCouponDailyClean() {
        log.info("Starting clean expired coupons daily job");
        this.cleanExpiredCoupons = true;
    }

    @Override
    public void stopExpiredCouponDailyClean() {
        log.info("Stopping clean expired coupons daily job");
        this.cleanExpiredCoupons = false;
    }
}
