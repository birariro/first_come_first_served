package com.birariro.first_come_first_served.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
  Optional<Event> findFirstByCode(Long code);
}
