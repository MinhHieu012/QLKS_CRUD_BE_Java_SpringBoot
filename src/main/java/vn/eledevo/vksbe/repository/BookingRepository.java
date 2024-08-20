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
            + "WHERE (:id IS NULL OR b.id LIKE %:id%) "
            + "AND (:roomName IS NULL OR b.room.id LIKE %:roomName%) "
            + "AND (:userName IS NULL OR b.user.id LIKE %:userName%) "
            + "AND (:checkInDate IS NULL OR b.checkInDate LIKE %:checkInDate%) "
            + "AND (:checkOutDate IS NULL OR b.checkoutDate LIKE %:checkOutDate%) ")
    List<Booking> listBookingSearchedAndPagingFromDB(
            @Param("id") String id,
            @Param("roomName") String roomName,
            @Param("userName") String userName,
            @Param("checkInDate") LocalDateTime checkInDate,
            @Param("checkInDate") LocalDateTime checkOutDate,
            Pageable pageable);
}
