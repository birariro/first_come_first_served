package com.birariro.first_come_first_served.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.birariro.first_come_first_served.service.EventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EventController {

  private final EventService eventService;

  @GetMapping("/event/v1/{code}/coupon")
  public ResponseEntity getCouponV1(@PathVariable Long code){
    return ResponseEntity.ok().body(eventService.couponV1(code));
  }
  @GetMapping("/event/v2/{code}/coupon")
  public ResponseEntity getCouponV2(@PathVariable Long code){
    return ResponseEntity.ok().body(eventService.couponV2(code));
  }
  @GetMapping("/event/v3/{code}/coupon")
  public ResponseEntity getCouponV3(@PathVariable Long code){
    return ResponseEntity.ok().body(eventService.couponV3(code));
  }
  @GetMapping("/event/v4/{code}/coupon")
  public ResponseEntity getCouponV4(@PathVariable Long code){
    return ResponseEntity.ok().body(eventService.couponV4(code));
  }
  @GetMapping("/event/v5/{code}/coupon")
  public ResponseEntity getCouponV5(@PathVariable Long code){
    return ResponseEntity.ok().body(eventService.couponV5(code));
  }

  @PostMapping("/event")
  public ResponseEntity createEvent(@RequestBody EventCreateRequest eventCreateRequest){
    eventService.event(eventCreateRequest.getCode(),eventCreateRequest.getName(),eventCreateRequest.getCount());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
  @PostMapping("/event/v5")
  public ResponseEntity createEventV5(@RequestBody EventCreateRequest eventCreateRequest){
    eventService.eventV5(eventCreateRequest.getCode(),eventCreateRequest.getName(),eventCreateRequest.getCount());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
