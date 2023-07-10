package com.birariro.first_come_first_served.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_coupon_v5")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponV5 implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  private String code;

  @JoinColumn(name = "event_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private EventV5 event;
  private String active;

  public CouponV5(String code) {
    this.code = code;
    this.active = "WAIT";
  }
  public void active(){
    this.active = "PUBLISHED";
  }

  public void publish(EventV5 event){
    this.event = event;
  }
}
