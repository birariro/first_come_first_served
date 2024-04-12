package com.birariro.first_come_first_served;

import com.birariro.first_come_first_served.domain.Event;
import com.birariro.first_come_first_served.domain.EventRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class V2ControllerTest extends BaseTest {

  @Test
  @DisplayName("멀티스레드 쿠폰 10개중 10개 발행시 적합성 깨짐")
  public void multPublish10Test() throws Exception {

    createEvent(10);
    boolean result = multPublish("v2", 10);
    Assertions.assertThat(result).isFalse();

    Event event = findEvent();
    Long count = event.getCount();

    //적합성이 깨져서 0 이 아니다
    Assertions.assertThat(count).isNotEqualTo(0);
  }

}
