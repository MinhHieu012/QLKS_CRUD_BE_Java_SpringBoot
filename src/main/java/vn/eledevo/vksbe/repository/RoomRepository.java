package vn.eledevo.vksbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.eledevo.vksbe.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    Boolean existsByRoomNumber(String roomNumber);
}
