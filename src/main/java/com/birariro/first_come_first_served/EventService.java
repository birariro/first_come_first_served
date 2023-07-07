package com.birariro.first_come_first_served;

import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.birariro.first_come_first_served.domain.Coupon;
import com.birariro.first_come_first_served.domain.CouponRepository;
import com.birariro.first_come_first_served.domain.Event;
import com.birariro.first_come_first_served.domain.EventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

  private final CouponRepository couponRepository;
  private final EventRepository eventRepository;
  public String coupon(Long code) {

    Event event = eventRepository.findFirstByCode(code)
        .orElseThrow(() -> new IllegalArgumentException("not exist event code"));

    if(!event.isPublishCoupon()) throw new IllegalStateException("expiration coupon");

    String couponCode = randomCode();
    Coupon coupon = new Coupon(couponCode);
    event.publishCoupon(coupon);
    couponRepository.save(coupon);

    return couponCode;
  }

  public void createEvent(Long code, String name, Long count){
    Event event = new Event(code, name, count);
    eventRepository.save(event);
  }

  private String randomCode(){

    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    int startChar = 48; // '0'
    int encChar = 122; // 'z'
    int targetStringLength = 10;
    Random random = new Random();
    String generatedString = random.ints(startChar, encChar + 1)
        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();

   return generatedString;
  }

}
