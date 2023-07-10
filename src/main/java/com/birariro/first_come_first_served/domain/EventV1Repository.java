package com.birariro.first_come_first_served.domain;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface EventV1Repository extends JpaRepository<Event,Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Event> findFirstByCode(Long code);
}
