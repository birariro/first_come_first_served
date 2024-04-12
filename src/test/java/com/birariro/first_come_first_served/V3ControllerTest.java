package com.birariro.first_come_first_served;

import com.birariro.first_come_first_served.domain.Event;
import com.birariro.first_come_first_served.domain.EventRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class V3ControllerTest extends BaseTest {


  @Test
  @DisplayName("멀티스레드 쿠폰 500개중 500개 발행시 성공")
  public void multPublish10Test() throws Exception {

    createEvent(500);
    boolean result = multPublish("v3", 500);

    Assertions.assertThat(result).isFalse();

    Event event = findEvent();
    Long count = event.getCount();

    Assertions.assertThat(count).isEqualTo(0);
  }

}
