package com.birariro.first_come_first_served.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class PublishEvent{
  private Long code;
  private String coupon;
}
