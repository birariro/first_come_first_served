package com.birariro.first_come_first_served;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
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

  private final long couponCount = 10;
  private final int userCount = 10;
  @BeforeEach
  public void createEventTest() throws Exception {

    EventCreateRequest request = new EventCreateRequest(1001L, "시작 이벤트", couponCount);
    String json = new ObjectMapper().writeValueAsString(request);
    mvc.perform(post("/event")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)).andExpect(status().isCreated());
  }

  @Test
  @DisplayName("쿠폰 1개 발행")
  @Order(1)
  public void publishTest() throws Exception {

    mvc.perform(get("/event/1001/coupon")
        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
  }

  @Test
  @Order(2)
  @DisplayName("멀티스레드 쿠폰 10개 발행")
  public void multPublishTest() throws InterruptedException {


    ExecutorService executorService = Executors.newFixedThreadPool(userCount);
    CountDownLatch countDownLatch = new CountDownLatch(userCount);
    for (int i = 0; i < userCount; i++) {
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
}