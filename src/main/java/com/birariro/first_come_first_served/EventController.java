package com.birariro.first_come_first_served;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EventController {

  private final EventService eventService;

  @GetMapping("/event/{code}/coupon")
  public ResponseEntity getCoupon(@PathVariable Long code){
    String coupon = eventService.coupon(code);
    return ResponseEntity.ok().body(coupon);
  }

  @PostMapping("/event")
  public ResponseEntity createEvent(@RequestBody EventCreateRequest eventCreateRequest){
    eventService.createEvent(eventCreateRequest.getCode(),eventCreateRequest.getName(),eventCreateRequest.getCount());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
