package vn.eledevo.vksbe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.eledevo.vksbe.entity.Room;
import vn.eledevo.vksbe.entity.User;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    Boolean existsByRoomNumber(String roomNumber);
    Optional<Room> findRoomByName(String roomName);

    @Query("SELECT r FROM Room r "
            + "WHERE (:name IS NULL OR r.name LIKE %:name%) "
            + "AND (:roomNumber IS NULL OR r.roomNumber LIKE %:roomNumber%) "
            + "AND (:floor IS NULL OR r.floor LIKE %:floor%) "
            + "AND (:roomTypeId IS NULL OR r.roomType.id = :roomTypeId) "
            + "AND (:status IS NULL OR r.status LIKE %:status%) ")
    List<Room> listRoomSearchedAndPagingFromDB(
            @Param("name") String name,
            @Param("roomNumber") String roomNumber,
            @Param("floor") String floor,
            @Param("roomTypeId") Long roomTypeId,
            @Param("status") String status,
            Pageable pageable);
}
