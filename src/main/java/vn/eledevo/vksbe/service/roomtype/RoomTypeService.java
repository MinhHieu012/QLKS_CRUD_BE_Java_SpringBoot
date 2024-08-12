package vn.eledevo.vksbe.service.roomtype;

import java.util.List;

import vn.eledevo.vksbe.dto.request.roomtype.RoomTypeRequest;
import vn.eledevo.vksbe.dto.response.roomtype.RoomTypeResponse;
import vn.eledevo.vksbe.exception.ValidationException;

public interface RoomTypeService {
    List<RoomTypeResponse> getAllRoomType();

    RoomTypeResponse addRoomType(RoomTypeRequest roomTypeRequest) throws ValidationException;

    RoomTypeResponse updateRoomType(Long id, RoomTypeRequest roomTypeRequest) throws ValidationException;

    RoomTypeResponse deleteRoomType(Long id) throws ValidationException;

    List<RoomTypeResponse> filterRoomType(
            String orderBy, int page, int limit, String orderedColumn, String name, String maxPeople);
}
