package vn.eledevo.vksbe.service.user;

import static vn.eledevo.vksbe.constant.ResponseMessage.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.constant.UserStatus;
import vn.eledevo.vksbe.dto.request.user.UserAddRequest;
import vn.eledevo.vksbe.dto.request.user.UserUpdateRequest;
import vn.eledevo.vksbe.dto.response.user.UserResponse;
import vn.eledevo.vksbe.entity.User;
import vn.eledevo.vksbe.exception.ValidationException;
import vn.eledevo.vksbe.mapper.UserMapper;
import vn.eledevo.vksbe.repository.UserRepository;
import vn.eledevo.vksbe.utils.SecurityUtils;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    UserMapper mapper;

    @Override
    public List<UserResponse> getAllUser() {
        List<User> userList = userRepository.findAll();
        List<UserResponse> userResponse =
                userList.stream().map(mapper::toResponse).collect(Collectors.toList());
        return userResponse;
    }

    public UserResponse createUser(UserAddRequest userRequest) throws ValidationException {

        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new ValidationException("Username", USER_EXIST);
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new ValidationException("Email", EMAIL_EXIST);
        }
        if (userRepository.existsByIdentificationNumber(userRequest.getIdentificationNumber())) {
            throw new ValidationException("Số CMND/CCCD", USER_IDENTIFICATION_NUMBER_INVALID);
        }

        User user = mapper.toEntity(userRequest);

        user.setCreatedBy(SecurityUtils.getUserId());
        user.setUpdatedBy(SecurityUtils.getUserId());

        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setPhone(userRequest.getPhone());
        user.setIdentificationNumber(userRequest.getIdentificationNumber());
        user.setDateOfBirth(userRequest.getDateOfBirth());
        user.setStatus(UserStatus.ACTIVE);

        User userAddData = userRepository.save(user);

        return mapper.toResponse(userAddData);
    }

    @Override
    public UserResponse updateUser(UUID uuid, UserUpdateRequest userRequest) throws ValidationException {

        User user =
                userRepository.findById(uuid).orElseThrow(() -> new ValidationException("Error", "User not found!"));

        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new ValidationException("Username", USER_EXIST);
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new ValidationException("Email", EMAIL_EXIST);
        }
        if (userRepository.existsByIdentificationNumber(userRequest.getIdentificationNumber())) {
            throw new ValidationException("Số CMND/CCCD", USER_IDENTIFICATION_NUMBER_INVALID);
        }

        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setIdentificationNumber(userRequest.getIdentificationNumber());
        user.setDateOfBirth(userRequest.getDateOfBirth());

        user.setUpdatedBy(SecurityUtils.getUserId());

        User userUpdateResult = userRepository.save(user);

        return mapper.toResponse(userUpdateResult);
    }

    @Override
    public UserResponse lockUser(UUID uuid) throws ValidationException {
        User user =
                userRepository.findById(uuid).orElseThrow(() -> new ValidationException("Error", "User not found!"));
        user.setStatus(UserStatus.LOCKED);
        User userLockResult = userRepository.save(user);
        return mapper.toResponse(userLockResult);
    }

    @Override
    public UserResponse unLockUser(UUID uuid) throws ValidationException {
        User user =
                userRepository.findById(uuid).orElseThrow(() -> new ValidationException("Error", "User not found!"));
        user.setStatus(UserStatus.ACTIVE);
        User userUnlockResult = userRepository.save(user);
        return mapper.toResponse(userUnlockResult);
    }

    @Override
    public List<UserResponse> searchUser(String username, String phone, String identificationNumber) {
        List<User> listUserFromDB = userRepository.searchUsers(username, phone, identificationNumber);
        List<UserResponse> listUserSearched =
                listUserFromDB.stream().map(mapper::toResponse).collect(Collectors.toList());
        return listUserSearched;
    }

    @Override
    public List<UserResponse> sortAndPagingAndSearch(
            String orderBy,
            int page,
            int limit,
            String orderedColumn,
            String name,
            String phone,
            String identificationNumber)
            throws ValidationException {
        Pageable userPageable =
                PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.valueOf(orderBy.toUpperCase()), orderedColumn));

        List<User> userList =
                userRepository.listUserSearchedAndPagingFromDB(name, phone, identificationNumber, userPageable);

        if (userList.isEmpty()) {
            throw new ValidationException("Trống", "Không tìm thấy user tương ứng!");
        }

        List<UserResponse> listSortAndPagingAndSearch =
                userList.stream().map(mapper::toResponse).toList();
        return listSortAndPagingAndSearch;
    }
}
