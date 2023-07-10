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
@Table(name = "tb_event_v5")
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class EventV5 {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  private Long code;
  private String name;
  private Long count;


  @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
  private List<CouponV5> coupons = new ArrayList<>();

  public EventV5(Long code, String name, Long count) {
    this.code = code;
    this.name = name;
    this.count = count;
  }

  public void publishCoupon(CouponV5 coupon){
    if(! this.isPublishCoupon()) {
      throw new IllegalStateException("not coupon count");
    }
    this.count = this.count - 1;
    this.coupons.add(coupon);
    coupon.publish(this);
  }

  public boolean isPublishCoupon(){
    return this.count > 0;
  }
}
