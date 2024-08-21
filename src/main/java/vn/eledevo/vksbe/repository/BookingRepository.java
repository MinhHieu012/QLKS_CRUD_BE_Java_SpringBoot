package vn.eledevo.vksbe.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.eledevo.vksbe.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b "
            + "WHERE (:bookingId IS NULL OR b.id = :bookingId) "
            + "AND (:roomName IS NULL OR b.room.name LIKE %:roomName%) "
            + "AND (:userName IS NULL OR b.user.username LIKE %:userName%) "
            + "AND (:checkInDate IS NULL OR b.checkInDate = :checkInDate) "
            + "AND (:checkOutDate IS NULL OR b.checkoutDate = :checkOutDate) ")
    List<Booking> listBookingSearchedAndPagingFromDB(
            @Param("bookingId") Long bookingId,
            @Param("roomName") String roomName,
            @Param("userName") String userName,
            @Param("checkInDate") LocalDateTime checkInDate,
            @Param("checkOutDate") LocalDateTime checkOutDate,
            Pageable pageable);
}
