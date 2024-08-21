package vn.eledevo.vksbe.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.eledevo.vksbe.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Lịch đặt có room ko bị trùng thời gian check-in, check-out lịch khác - DONE
     * Lịch đặt có room ko đc nằm trong khoảng thời gian check-in, check-out của lịch khác - DOING... (Chưa xong)
     * Lịch đặt có sát thời gian check-in của lịch trước đó ko (tối thiểu cách 1h)
     * Lịch đặt không được chọn phòng đang được sử dụng (LOCK), (USING), (CLEANNING) và (APPROVED) - DONE
     */
    @Query(
            """
				SELECT COUNT(b) >= 1 FROM Booking b
				WHERE :roomId = b.room.id
				AND :checkInDate = b.checkInDate AND :checkoutDate = b.checkoutDate
				AND b.room.status NOT IN ('LOCK', 'CLEANNING', 'USING', 'APPROVED')
			""")
    Boolean validateSameBooking(Integer roomId, LocalDateTime checkInDate, LocalDateTime checkoutDate);

    @Query(
            """
				SELECT COUNT(b) > 0 FROM Booking b
				WHERE b.room.id = :roomId
				AND (:checkInDate < b.checkoutDate AND :checkoutDate > :checkInDate)
			""")
    Boolean validateOnRangeBooking(Integer roomId, LocalDateTime checkInDate, LocalDateTime checkoutDate);

	@Query(
			"""
               	SELECT COUNT(b) > 0 FROM Booking b
                WHERE b.room.id = :roomId
                AND (:checkInDate <= b.checkoutDate)
            """)
	Boolean validateRange1HourBooking(Integer roomId, LocalDateTime checkInDate);

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
