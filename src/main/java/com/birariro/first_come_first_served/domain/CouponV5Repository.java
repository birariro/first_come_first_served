package com.birariro.first_come_first_served.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponV5Repository extends JpaRepository<CouponV5, Long> {
  @Query("select c from CouponV5 c join fetch c.event e where e.code =:event and c.code =:code")
  Optional<CouponV5> findByCodeAndEvent(@Param("code") String code, @Param("event") Long event);


  @Query("update CouponV5 c SET c.active = 'PUBLISHED'  where c.id in (select c from CouponV5 c join c.event e where e.code =:event and c.code =:code)")
  int updateByCodeAndEvent(@Param("code") String code, @Param("event") Long event);
}
