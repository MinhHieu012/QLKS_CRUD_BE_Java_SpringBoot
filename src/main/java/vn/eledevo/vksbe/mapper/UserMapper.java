package vn.eledevo.vksbe.mapper;

import org.mapstruct.Mapper;

import vn.eledevo.vksbe.dto.request.user.UserAddRequest;
import vn.eledevo.vksbe.dto.response.user.UserResponse;
import vn.eledevo.vksbe.entity.User;

@Mapper(componentModel = "spring")
public abstract class UserMapper extends BaseMapper<UserAddRequest, UserResponse, User> {}
