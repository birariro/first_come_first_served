package com.birariro.first_come_first_served;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.birariro.first_come_first_served.controller.EventCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class EventControllerTest {

  @Autowired
  MockMvc mvc;


  private String couponTemplate = "/event/v31/1001/coupon";
  private String eventTemplate = "/event";

  public void createEvent(long count) throws Exception {

    EventCreateRequest request = new EventCreateRequest(1001L, "시작 이벤트", count);
    String json = new ObjectMapper().writeValueAsString(request);
    mvc.perform(post(eventTemplate)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)).andExpect(status().isCreated());
  }


  @Test
  @DisplayName("쿠폰 1개 발행")
  public void publishTest() throws Exception {
    createEvent(1);

    mvc.perform(get(couponTemplate)
        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
  }

  public void multPublish(int count) throws InterruptedException {

    List<String> results = new ArrayList<>();
    ExecutorService executorService = Executors.newFixedThreadPool(count);
    CountDownLatch countDownLatch = new CountDownLatch(count);
    for (int i = 0; i < count; i++) {
      executorService.execute(() -> {
        try {
          ResultActions perform = mvc.perform(get(couponTemplate)
              .contentType(MediaType.APPLICATION_JSON)
          );
          perform.andExpect(status().isOk());

          countDownLatch.countDown();
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    }
    countDownLatch.await();
//
//    List<String> collect = results.stream().distinct().collect(Collectors.toList());
//    if(results.size() != collect.size()){
//      System.out.println("멀티스레드 쿠폰 중복 발생");
//      Assertions.fail();
//    }

    System.out.println("멀티스레드 쿠폰 발행 종료");

  }
  public void multPublishNotWait(int count) throws InterruptedException {

    ExecutorService executorService = Executors.newFixedThreadPool(count);
    for (int i = 0; i < count; i++) {
      executorService.execute(() -> {
        try {
          ResultActions perform = mvc.perform(get(couponTemplate)
              .contentType(MediaType.APPLICATION_JSON)
          );
          perform.andExpect(status().isOk());

        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    }

  }

  @Test
  @DisplayName("멀티스레드 쿠폰 2개중 2개 발행")
  public void multPublish2Test() throws Exception {

    createEvent(2);
    multPublish(2);
  }

  @Test
  @DisplayName("멀티스레드 쿠폰 10개중 10개 발행")
  public void multPublish10Test() throws Exception {

    createEvent(10);
    multPublish(10);
  }
  @Test
  @DisplayName("멀티스레드 쿠폰 100개중 100개 발행")
  public void multPublish100Test() throws Exception {

    createEvent(100);
    multPublish(100);
  }

  @Test
  @DisplayName("멀티스레드 쿠폰 150개중 150개 발행")
  public void multPublish150Test() throws Exception {

    int count = 150;
    createEvent(count);
    multPublish(count);
  }
  @Test
  @DisplayName("멀티스레드 쿠폰 500개중 500개 발행")
  public void multPublish500Test() throws Exception {
    int count = 500;
    createEvent(count);
    multPublish(count);
  }
  @Test
  @DisplayName("멀티스레드 쿠폰 500개중 100회 5번 발행")
  public void multPublish500to5Test() throws Exception {
    int count = 500;
    createEvent(count);
    for (int i = 0; i < 5; i++) {
      multPublish(100);
    }
  }

  @Test
  @DisplayName("멀티스레드 쿠폰 1000개중 1000개 발행")
  public void multPublish1000Test() throws Exception {
    int count = 1000;
    createEvent(count);
    multPublish(count);
  }

  @Test
  @DisplayName("멀티스레드 쿠폰 2000개중 2000개 발행")
  public void multPublish2000Test() throws Exception {
    int count = 2000;
    createEvent(count);
    multPublish(count);
  }
  @Test
  @DisplayName("멀티스레드 쿠폰 5000개중 1000개 5회 발행")
  public void multPublish5000Test() throws Exception {
    int count = 5000;
    createEvent(count);

    for (int i = 0; i < 5; i++) {
      multPublishNotWait(1000);
    }
  }

  @Test
  @DisplayName("멀티스레드 쿠폰 10000개중 10000개 발행")
  public void multPublish10000Test() throws Exception {
    int count = 10000;
    createEvent(count);
    multPublish(count);
  }


}
