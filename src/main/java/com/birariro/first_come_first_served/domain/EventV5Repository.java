package com.birariro.first_come_first_served.domain;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventV5Repository extends JpaRepository<EventV5,Long> {

}
