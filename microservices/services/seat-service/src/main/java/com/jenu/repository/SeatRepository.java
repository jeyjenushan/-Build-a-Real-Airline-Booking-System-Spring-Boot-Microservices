package com.jenu.repository;

import com.jenu.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SeatRepository extends JpaRepository<Seat, Long> {

    boolean existsBySeatMapId(Long seatMapId);


}
