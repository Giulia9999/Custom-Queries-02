package com.example.customqueries02;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepo extends JpaRepository<Flight, Long> {
    Page<Flight> findByStatus(FlightStatusEnum status, Pageable pageable);

    @Query("SELECT f FROM Flight f WHERE f.status = :status1 OR f.status = :status2")
    Page<Flight> findByStatus(@Param("status1") FlightStatusEnum status1,
                              @Param("status2") FlightStatusEnum status2, Pageable pageable);
}
