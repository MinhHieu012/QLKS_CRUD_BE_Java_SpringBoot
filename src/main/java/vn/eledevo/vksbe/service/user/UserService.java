package vn.eledevo.vksbe.service.user;

import vn.eledevo.vksbe.dto.request.user.UserAddRequest;
import vn.eledevo.vksbe.dto.request.user.UserUpdateRequest;
import vn.eledevo.vksbe.dto.response.ApiResponse;
import vn.eledevo.vksbe.dto.response.UserResponse;
import vn.eledevo.vksbe.exception.ValidationException;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> getAllUser();
    UserResponse createUser(UserAddRequest userRequest) throws ValidationException;
    UserResponse updateUser(UUID uuid, UserUpdateRequest userRequest) throws ValidationException;
    UserResponse lockUser(UUID uuid) throws ValidationException;
    UserResponse unLockUser(UUID uuid) throws ValidationException;
}
