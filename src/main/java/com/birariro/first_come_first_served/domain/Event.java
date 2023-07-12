package com.birariro.first_come_first_served.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_event")
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Event {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  private Long code;
  private String name;
  private Long count;


  @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
  private List<Coupon> coupons = new ArrayList<>();

  public Event(Long code, String name, Long count) {
    this.code = code;
    this.name = name;
    this.count = count;
  }

  public void publishCoupon(Coupon coupon){
    this.isPublishCoupon();
    this.count = this.count - 1;
    this.coupons.add(coupon);
    coupon.publish(this);
  }

  public void test(){
    this.isPublishCoupon();
    this.count = this.count - 1;
  }
  public void isPublishCoupon(){
    if(this.count <= 0)  throw new IllegalStateException("not coupon count");
  }
}
