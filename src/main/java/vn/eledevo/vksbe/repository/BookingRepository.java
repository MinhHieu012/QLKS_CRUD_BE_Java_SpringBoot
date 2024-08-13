package vn.eledevo.vksbe.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.eledevo.vksbe.entity.Booking;
import vn.eledevo.vksbe.entity.Room;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {}
