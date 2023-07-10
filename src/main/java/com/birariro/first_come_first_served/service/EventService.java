package com.birariro.first_come_first_served.service;


import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.birariro.first_come_first_served.common.Random;
import com.birariro.first_come_first_served.config.Events;
import com.birariro.first_come_first_served.domain.Coupon;
import com.birariro.first_come_first_served.domain.CouponRepository;
import com.birariro.first_come_first_served.domain.CouponV5;
import com.birariro.first_come_first_served.domain.CouponV5Repository;
import com.birariro.first_come_first_served.domain.Event;
import com.birariro.first_come_first_served.domain.EventRepository;
import com.birariro.first_come_first_served.domain.EventV1Repository;
import com.birariro.first_come_first_served.domain.EventV5;
import com.birariro.first_come_first_served.domain.EventV5Repository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

  private final CouponRepository couponRepository;
  private final CouponV5Repository couponV5Repository;
  private final EventRepository eventRepository;
  private final EventV1Repository eventV1Repository;
  private final EventV5Repository eventV5Repository;
  private final RedisTemplate<Long,String> redisTemplate;

  public void createEvent(Long code, String name, Long count){
    Event event = new Event(code, name, count);
    eventRepository.save(event);
  }

  /**
   * 2개 이상 스레드 부터 데드락.
   */
  public String couponV1(Long code){
    Event event = eventV1Repository.findFirstByCode(code)
        .orElseThrow(() -> new IllegalArgumentException("not exist event code"));

    if(!event.isPublishCoupon()) throw new IllegalStateException("expiration coupon");

    String couponCode = Random.code();
    Coupon coupon = new Coupon(couponCode);
    event.publishCoupon(coupon);
    couponRepository.save(coupon);

    return couponCode;
  }

  /**
   * 정합성이 깨짐
   */
  public synchronized String couponV2(Long code){
    Event event = eventV1Repository.findFirstByCode(code)
        .orElseThrow(() -> new IllegalArgumentException("not exist event code"));

    if(!event.isPublishCoupon()) throw new IllegalStateException("expiration coupon");

    String couponCode = Random.code();
    Coupon coupon = new Coupon(couponCode);
    event.publishCoupon(coupon);
    couponRepository.save(coupon);

    return couponCode;
  }

  /**
   * 정합성 보장.
   * hikari connection 이상 요청시 time out
   * @param code
   * @return
   */
  public String couponV3(Long code) {

    Event event = eventRepository.findFirstByCode(code)
        .orElseThrow(() -> new IllegalArgumentException("not exist event code"));

    if(!event.isPublishCoupon()) throw new IllegalStateException("expiration coupon");

    String couponCode = Random.code();
    Coupon coupon = new Coupon(couponCode);
    event.publishCoupon(coupon);
    couponRepository.save(coupon);

    return couponCode;
  }

  /**
   * redis 를 사용하여 숫자값을 증가하면서 넘겨줌
   * 정합성, 500개 동시 요청 완료
   * 하지만 이값은 대기표와 같은 값이라 쿠폰번호로 사용할수없음
   */
  public String couponV4(Long code) {

    ZSetOperations<Long, String> stringIntegerZSetOperations = redisTemplate.opsForZSet();
    Double waiting = stringIntegerZSetOperations.incrementScore(code, "waiting", 1);
    return waiting.toString();
  }
  /**
   *
   */

  public String couponV5(Long code) {
    ListOperations<Long, String> longStringListOperations = redisTemplate.opsForList();
    String coupon = longStringListOperations.leftPop(code);

    if(coupon == null) {
      throw new IllegalStateException("expiration coupon");
    }
    Events.raise(new PublishEvent(code,coupon));
    return coupon;
  }

  /**
   * 이벤트 생성시 쿠폰을 미리 만들어두고
   * 만들어진 이벤트 코드와 쿠폰 번호를 redis 큐 에 저장한다.
   */
  public void createEventV5(Long code, String name, Long count){
    EventV5 event = new EventV5(code, name, count);
    eventV5Repository.save(event);

    ListOperations<Long, String> longStringListOperations = redisTemplate.opsForList();

    for (int i = 0; i < count; i++) {
      String randomCode = Random.code();
      CouponV5 couponV5 = new CouponV5(randomCode);
      event.publishCoupon(couponV5);
      couponV5Repository.save(couponV5);
      longStringListOperations.leftPush(code,randomCode);
    }
  }

}
