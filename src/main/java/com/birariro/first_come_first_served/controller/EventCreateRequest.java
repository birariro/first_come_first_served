package com.birariro.first_come_first_served.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateRequest {
  private Long code;
  private String name;
  private Long count;
}
