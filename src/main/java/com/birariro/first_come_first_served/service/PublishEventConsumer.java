package com.birariro.first_come_first_served.service;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.birariro.first_come_first_served.domain.CouponV5;
import com.birariro.first_come_first_served.domain.CouponV5Repository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublishEventConsumer {
  private final CouponV5Repository couponV5Repository;

  @Async
  @EventListener(PublishEvent.class)
  public void handel(PublishEvent event){

    CouponV5 coupon = couponV5Repository.findByCodeAndEvent(event.getCoupon(), event.getCode())
        .orElseThrow(() -> new IllegalArgumentException("not exist event coupon"));

    coupon.active();
    couponV5Repository.save(coupon);
  }
}
