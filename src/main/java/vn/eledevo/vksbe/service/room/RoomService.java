package vn.eledevo.vksbe.service.room;

import java.util.List;

import vn.eledevo.vksbe.dto.request.room.RoomRequest;
import vn.eledevo.vksbe.dto.response.room.RoomResponse;
import vn.eledevo.vksbe.exception.ValidationException;

public interface RoomService {
    List<RoomResponse> getAllRoom();

    RoomResponse addRoom(RoomRequest roomRequest) throws ValidationException;

    RoomResponse updateRoom(Integer id, RoomRequest roomRequest) throws ValidationException;

    RoomResponse updateRoomStatus(Integer id, String status) throws ValidationException;

    List<RoomResponse> sortAndPagingAndSearch(
            String orderBy,
            int page,
            int limit,
            String orderedColumn,
            String name,
            String roomNumber,
            String floor,
            Long roomTypeId,
            String status)
            throws ValidationException;
}
