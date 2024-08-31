package vn.eledevo.vksbe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.eledevo.vksbe.constant.RoomStatus;
import vn.eledevo.vksbe.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    Boolean existsByRoomNumber(String roomNumber);

    Boolean existsByRoomTypeId(Long roomTypeId);

    @Query("SELECT r FROM Room r "
            + "WHERE (:name IS NULL OR r.name LIKE %:name%) "
            + "AND (:roomNumber IS NULL OR r.roomNumber LIKE %:roomNumber%) "
            + "AND (:floor IS NULL OR r.floor LIKE %:floor%) "
            + "AND (:roomTypeId IS NULL OR r.roomType.id = :roomTypeId) "
            + "AND (:status IS NULL OR r.status = :status) ")
    Page<Room> listRoomSearchedAndPagingFromDB(
            @Param("name") String name,
            @Param("roomNumber") String roomNumber,
            @Param("floor") String floor,
            @Param("roomTypeId") Long roomTypeId,
            @Param("status") RoomStatus status,
            Pageable pageable);
}
