package vn.eledevo.vksbe.mapper;

import org.mapstruct.Mapper;

import vn.eledevo.vksbe.dto.request.roomtype.RoomTypeRequest;
import vn.eledevo.vksbe.dto.response.roomtype.RoomTypeResponse;
import vn.eledevo.vksbe.entity.RoomType;

@Mapper(componentModel = "spring")
public abstract class RoomTypeMapper extends BaseMapper<RoomTypeRequest, RoomTypeResponse, RoomType> {}
