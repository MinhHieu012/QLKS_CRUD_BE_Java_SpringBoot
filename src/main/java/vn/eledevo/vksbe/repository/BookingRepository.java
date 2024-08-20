package vn.eledevo.vksbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.eledevo.vksbe.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {}
