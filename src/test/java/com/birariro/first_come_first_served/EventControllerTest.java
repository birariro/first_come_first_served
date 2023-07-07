package com.birariro.first_come_first_served;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class EventControllerTest {

  @Autowired
  MockMvc mvc;



  public void createEvent(long count) throws Exception {

    EventCreateRequest request = new EventCreateRequest(1001L, "시작 이벤트", count);
    String json = new ObjectMapper().writeValueAsString(request);
    mvc.perform(post("/event")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)).andExpect(status().isCreated());
  }

  @Test
  @DisplayName("쿠폰 1개 발행")
  public void publishTest() throws Exception {
    createEvent(10);
    mvc.perform(get("/event/1001/coupon")
        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
  }

  public void multPublishWithUser(int count) throws InterruptedException {

    ExecutorService executorService = Executors.newFixedThreadPool(count);
    CountDownLatch countDownLatch = new CountDownLatch(count);
    for (int i = 0; i < count; i++) {
      executorService.execute(() -> {
        try {
          mvc.perform(get("/event/1001/coupon")
              .contentType(MediaType.APPLICATION_JSON)
          ).andExpect(status().isOk());

          countDownLatch.countDown();
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    }
    countDownLatch.await();
    System.out.println("멀티스레드 쿠폰 발행 종료");

  }

  @Test
  @DisplayName("멀티스레드 쿠폰 10개중 10개 발행")
  public void multPublish10Test() throws Exception {

    createEvent(10);
    multPublishWithUser(10);
    System.out.println("멀티스레드 쿠폰 발행 종료");
  }
  @Test
  @DisplayName("멀티스레드 쿠폰 100개중 100개 발행")
  public void multPublish100Test() throws Exception {

    createEvent(100);
    multPublishWithUser(100);
    System.out.println("멀티스레드 쿠폰 발행 종료");
  }

  @Test
  @DisplayName("멀티스레드 쿠폰 150개중 150개 발행")
  public void multPublish150Test() throws Exception {

    int count = 150;
    createEvent(count);
    multPublishWithUser(count);
    System.out.println("멀티스레드 쿠폰 발행 종료");
  }
  @Test
  @DisplayName("멀티스레드 쿠폰 500개중 500개 발행")
  public void multPublish500Test() throws Exception {
    int count = 500;
    createEvent(count);
    multPublishWithUser(count);
    System.out.println("멀티스레드 쿠폰 발행 종료");
  }

  @Test
  @DisplayName("멀티스레드 쿠폰 500개중 100회 5번 발행")
  public void multPublish500to5Test() throws Exception {
    int count = 500;
    createEvent(count);
    for (int i = 0; i < 5; i++) {
      multPublishWithUser(100);
    }

    System.out.println("멀티스레드 쿠폰 발행 종료");
  }

}