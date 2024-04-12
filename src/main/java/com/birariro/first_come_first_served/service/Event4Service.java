package com.birariro.first_come_first_served.service;


import com.birariro.first_come_first_served.domain.CouponRepository;
import com.birariro.first_come_first_served.domain.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Event4Service {
  private final LockService lockService;
  private final EventService eventService;

  /**
   * 스핀락으로 처리
   */
  public String couponV4(Long code) throws InterruptedException {

    while (!lockService.lock(code)){
      Thread.sleep(100);
    }
    System.out.println("lock get");
    try {
      return eventService.couponV1(code);
    } finally {
      System.out.println("lock delete");
      lockService.unlock(code);
    }

  }

}


