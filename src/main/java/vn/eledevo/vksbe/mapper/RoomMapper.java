package vn.eledevo.vksbe.mapper;

import org.mapstruct.Mapper;

import vn.eledevo.vksbe.dto.request.room.RoomRequest;
import vn.eledevo.vksbe.dto.response.room.RoomResponse;
import vn.eledevo.vksbe.entity.Room;

@Mapper(componentModel = "spring")
public abstract class RoomMapper extends BaseMapper<RoomRequest, RoomResponse, Room> {}
