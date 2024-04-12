package com.birariro.first_come_first_served.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.birariro.first_come_first_served.common.Random;
import com.birariro.first_come_first_served.domain.Coupon;
import com.birariro.first_come_first_served.domain.CouponRepository;
import com.birariro.first_come_first_served.domain.Event;
import com.birariro.first_come_first_served.domain.EventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

  private final CouponRepository couponRepository;
  private final EventRepository eventRepository;

  @Transactional
  public void event(Long code, String name, Long count){
    Event event = new Event(code, name, count);
    eventRepository.save(event);
  }

  /**
   * FK 공유 락으로 인한 데드락
   */
  @Transactional
  public String couponV1(Long code){
    Event event = eventRepository.findFirstByCode(code)
        .orElseThrow(() -> new IllegalArgumentException("not exist event code"));

    event.isPublishCoupon();

    String couponCode = Random.code();
    Coupon coupon = new Coupon(couponCode);
    event.publishCoupon(coupon);
    couponRepository.save(coupon);

    return couponCode;
  }


  /**
   * 정합성이 깨짐
   */
  @Transactional
  public synchronized String couponV2(Long code){
    Event event = eventRepository.findFirstByCode(code)
        .orElseThrow(() -> new IllegalArgumentException("not exist event code"));

    event.isPublishCoupon();

    String couponCode = Random.code();
    Coupon coupon = new Coupon(couponCode);
    event.publishCoupon(coupon);
    couponRepository.save(coupon);

    return couponCode;
  }

  /**
   * 비관적 락을 통한
   * 정상 동작
   */
  @Transactional
  public String couponV3(Long code) {

    Event event = eventRepository.findLockFirstByCode(code)
        .orElseThrow(() -> new IllegalArgumentException("not exist event code"));

    event.isPublishCoupon();

    String couponCode = Random.code();
    Coupon coupon = new Coupon(couponCode);
    event.publishCoupon(coupon);
    couponRepository.save(coupon);
    return couponCode;
  }

}


