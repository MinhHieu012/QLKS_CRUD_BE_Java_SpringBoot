package vn.eledevo.vksbe.service.user;

import java.util.List;
import java.util.UUID;

import vn.eledevo.vksbe.dto.request.user.UserAddRequest;
import vn.eledevo.vksbe.dto.request.user.UserUpdateRequest;
import vn.eledevo.vksbe.dto.response.user.UserResponse;
import vn.eledevo.vksbe.exception.ValidationException;

public interface UserService {
    List<UserResponse> getAllUser();

    UserResponse createUser(UserAddRequest userRequest) throws ValidationException;

    UserResponse updateUser(UUID uuid, UserUpdateRequest userRequest) throws ValidationException;

    UserResponse lockUser(UUID uuid) throws ValidationException;

    UserResponse unLockUser(UUID uuid) throws ValidationException;

    List<UserResponse> searchUser(String name, String phone, String identificationNumber);

    List<UserResponse> sortAndPagingAndSearch(
            String orderBy,
            int page,
            int limit,
            String orderedColumn,
            String name,
            String phone,
            String identificationNumber);
}
