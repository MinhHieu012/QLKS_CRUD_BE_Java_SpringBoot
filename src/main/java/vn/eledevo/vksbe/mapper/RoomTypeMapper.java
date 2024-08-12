package vn.eledevo.vksbe.mapper;

import org.mapstruct.Mapper;
import vn.eledevo.vksbe.dto.request.roomtype.RoomTypeRequest;
import vn.eledevo.vksbe.dto.request.user.UserAddRequest;
import vn.eledevo.vksbe.dto.response.RoomTypeResponse;
import vn.eledevo.vksbe.dto.response.UserResponse;
import vn.eledevo.vksbe.entity.RoomType;
import vn.eledevo.vksbe.entity.User;

@Mapper(componentModel = "spring")
public abstract class RoomTypeMapper extends BaseMapper<RoomTypeRequest, RoomTypeResponse, RoomType> {}