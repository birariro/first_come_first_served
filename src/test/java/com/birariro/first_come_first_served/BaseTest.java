package com.birariro.first_come_first_served;

import com.birariro.first_come_first_served.controller.EventCreateRequest;
import com.birariro.first_come_first_served.domain.Event;
import com.birariro.first_come_first_served.domain.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class BaseTest {

  @Autowired
  MockMvc mvc;
  @Autowired

  private EventRepository eventRepository;

  private String eventTemplate = "/event";
  long eventCode = 1001L;

  public Event findEvent(){
    return eventRepository.findFirstByCode(eventCode).get();
  }

  public void createEvent(long count) throws Exception {

    EventCreateRequest request = new EventCreateRequest(eventCode, "시작 이벤트", count);
    String json = new ObjectMapper().writeValueAsString(request);
    mvc.perform(post(eventTemplate)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)).andExpect(status().isCreated());
  }


  public boolean multPublish(String version, int count) throws InterruptedException {

    String api = String.format("/event/%s/1001/coupon", version);

    ExecutorService executorService = Executors.newFixedThreadPool(count);
    CountDownLatch countDownLatch = new CountDownLatch(count);
    AtomicBoolean exception = new AtomicBoolean(false);
    for (int i = 0; i < count; i++) {
      executorService.execute(() -> {
        try {
          ResultActions perform = mvc.perform(get(api)
              .contentType(MediaType.APPLICATION_JSON)
          );
          perform.andExpect(status().isOk());

        } catch (Exception e) {
          exception.set(true);
          e.printStackTrace();
        }
        finally {
          countDownLatch.countDown();
        }
      });
    }
    countDownLatch.await();
    return exception.get();

  }


}
