package vn.eledevo.vksbe.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

import java.util.*;

import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import lombok.extern.log4j.Log4j2;
import vn.eledevo.vksbe.config.security.JwtAuthenticationFilter;
import vn.eledevo.vksbe.config.security.JwtService;
import vn.eledevo.vksbe.constant.Role;
import vn.eledevo.vksbe.constant.UserStatus;
import vn.eledevo.vksbe.dto.request.user.UserAddRequest;
import vn.eledevo.vksbe.dto.request.user.UserUpdateRequest;
import vn.eledevo.vksbe.dto.response.user.UserResponse;
import vn.eledevo.vksbe.entity.User;
import vn.eledevo.vksbe.exception.ValidationException;
import vn.eledevo.vksbe.mapper.UserMapper;
import vn.eledevo.vksbe.repository.UserRepository;
import vn.eledevo.vksbe.service.user.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
@TestPropertySource("/test.properties")
@WithUserDetails(value = "admin", userDetailsServiceBeanName = "testUserAdmin")
public class UserServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserAddRequest userRequest;
    private UserUpdateRequest userUpdateRequest;
    private User existingUser;
    private UserResponse userResponse;
    private User user;
    private Date dateOfBirth;
    private List<User> userList;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserMapper mapper;
    @MockBean
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void initData() {
        userList = List.of(
                new User(
                        "6fb9c7ec-a08f-44ca-8be3-25d6d50efef0",
                        "guest",
                        "guest@example.com",
                        "2002-11-09",
                        "0967710509",
                        "1202034",
                        "USER",
                        "ACTIVE"),
                new User(
                        "b31042bf-33be-4310-b38a-6fa3179ad9b9",
                        "guest1",
                        "gues1t@example.com",
                        "2001-11-09",
                        "0967710508",
                        "1202033",
                        "USER",
                        "ACTIVE"));

        dateOfBirth = java.sql.Date.valueOf("2002-11-09");

        userRequest = UserAddRequest.builder()
                .username("guest")
                .email("guest@gmail.com")
                .password("123456")
                .phone("0967710509")
                .identificationNumber("1202034")
                .dateOfBirth(dateOfBirth)
                .build();

        userUpdateRequest = UserUpdateRequest.builder()
                .username("guest")
                .email("guest@gmail.com")
                .phone("0967710509")
                .identificationNumber("1202034")
                .dateOfBirth(dateOfBirth)
                .build();

        userResponse = UserResponse.builder()
                .username("guest")
                .email("guest@gmail.com")
                .dateOfBirth(dateOfBirth)
                .phone("0967710509")
                .identificationNumber(Long.valueOf("1202034"))
                .build();

        existingUser = new User();
        existingUser.setId(UUID.fromString("6fb9c7ec-a08f-44ca-8be3-25d6d50efef0"));
        existingUser.setUsername("guest");
        existingUser.setEmail("guest@gmail.com");
        existingUser.setPhone("0967710509");
        existingUser.setIdentificationNumber("1202034");
        existingUser.setDateOfBirth(new Date(2000 - 11 - 9));
        existingUser.setRole(Role.USER);
        existingUser.setStatus(UserStatus.ACTIVE);

        user = User.builder()
                .username("guest")
                .email("guest@gmail.com")
                .phone("0967710509")
                .identificationNumber("001202034")
                .dateOfBirth(dateOfBirth)
                .build();
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserDetailsService testUserAdmin() {
            User userMock = new User();
            userMock.setRole(Role.ADMIN);
            return username -> userMock;
        }
    }

    @Test
    void GetAllUsers_ValidRequest_Success() throws Exception {
        Mockito.when(userRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")))
                .thenReturn(userList);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/quanlyuser").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Get all user success!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.length()").value(2));
    }

    @Test
    void AddUser_ValidRequest_Success() throws ValidationException {
        Mockito.when(userRepository.existsByUsername(any())).thenReturn(false);
        Mockito.when(userRepository.existsByPhone(any())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(any())).thenReturn(false);
        Mockito.when(userRepository.existsByIdentificationNumber(any())).thenReturn(false);
        Mockito.when(userRepository.save(any())).thenReturn(user);

        var userResponse = userService.createUser(userRequest);
        userResponse.setRole(Role.USER);
        userResponse.setStatus(UserStatus.ACTIVE);

        Assertions.assertThat(userResponse.getUsername()).isEqualTo("guest");
        Assertions.assertThat(userResponse.getEmail()).isEqualTo("guest@gmail.com");
        Assertions.assertThat(userResponse.getDateOfBirth()).isEqualTo("2002-11-09");
        Assertions.assertThat(userResponse.getPhone()).isEqualTo("0967710509");
        Assertions.assertThat(userResponse.getIdentificationNumber().toString()).isEqualTo("1202034");
        Assertions.assertThat(userResponse.getRole().toString()).isEqualTo("USER");
        Assertions.assertThat(userResponse.getStatus().toString()).isEqualTo("ACTIVE");
    }

    @Test
    void AddUser_InvalidRequest_UsernameExisted_Failed() throws ValidationException {
        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(true);

        userRequest.setUsername("khach 1");

        var exceptionUsernameExisted =
                assertThrows(ValidationException.class, () -> userService.createUser(userRequest));

        Assertions.assertThat(exceptionUsernameExisted.getMessage()).isEqualTo("username: " + "Người dùng đã tồn tại!");
    }

    @Test
    void AddUser_InvalidRequest_PhoneExisted_Failed() throws ValidationException {
        Mockito.when(userRepository.existsByPhone(anyString())).thenReturn(true);

        userRequest.setPhone("0967710591");

        var exceptionPhoneExisted = assertThrows(ValidationException.class, () -> userService.createUser(userRequest));

        Assertions.assertThat(exceptionPhoneExisted.getMessage())
                .isEqualTo("phone: " + "Số điện thoại này đã được sử dụng!");
    }

    @Test
    void AddUser_InvalidRequest_EmailExisted_Failed() throws ValidationException {
        Mockito.when(userRepository.existsByEmail(anyString())).thenReturn(true);

        userRequest.setEmail("khach1@gmail.com");

        var exceptionEmailExisted = assertThrows(ValidationException.class, () -> userService.createUser(userRequest));

        Assertions.assertThat(exceptionEmailExisted.getMessage()).isEqualTo("email: " + "Email đã tồn tại!");
    }

    @Test
    void AddUser_InvalidRequest_IdentificationNumberExisted_Failed() throws ValidationException {
        Mockito.when(userRepository.existsByIdentificationNumber(ArgumentMatchers.any()))
                .thenReturn(true);

        userRequest.setIdentificationNumber("04734973");

        var exceptionIdentificationNumberExisted =
                assertThrows(ValidationException.class, () -> userService.createUser(userRequest));

        Assertions.assertThat(exceptionIdentificationNumberExisted.getMessage())
                .isEqualTo("identificationNumber: " + "Số CMND/CCCD không hợp lệ hoặc đã tồn tại!");
    }

    @Test
    void UpdateUser_ValidRequest_Success() throws ValidationException {
        Mockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(existingUser));

        Mockito.when(userRepository.existsByUsername(any())).thenReturn(false);
        Mockito.when(userRepository.existsByPhone(any())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(any())).thenReturn(false);
        Mockito.when(userRepository.existsByIdentificationNumber(any())).thenReturn(false);

        userUpdateRequest.setUsername("guest1");
        userUpdateRequest.setEmail("guest1@example.com");
        userUpdateRequest.setPhone("0967710509");
        userUpdateRequest.setIdentificationNumber("967710509");
        userUpdateRequest.setDateOfBirth(java.sql.Date.valueOf("2002-11-09"));
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(existingUser);

        var updatedUserResponse = userService.updateUser(existingUser.getId(), userUpdateRequest);

        Assertions.assertThat(updatedUserResponse.getUsername()).isEqualTo("guest1");
        Assertions.assertThat(updatedUserResponse.getEmail()).isEqualTo("guest1@example.com");
        Assertions.assertThat(updatedUserResponse.getPhone()).isEqualTo("0967710509");
        Assertions.assertThat(updatedUserResponse.getIdentificationNumber().toString())
                .isEqualTo("967710509");
        Assertions.assertThat(updatedUserResponse.getDateOfBirth()).isEqualTo("2002-11-09");
    }

    @Test
    void UpdateUser_InvalidRequest_UserIdNotExisted_Failed() throws ValidationException {
        Mockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

        var exceptionUserIdNotExisted = assertThrows(
                ValidationException.class,
                () -> userService.updateUser(
                        UUID.fromString("6fb9c7ec-a08f-44ca-8be3-25d6d50efef0"), userUpdateRequest));

        Assertions.assertThat(exceptionUserIdNotExisted.getMessage()).isEqualTo("Error: " + "User not found!");
    }

    @Test
    void UpdateUser_InvalidRequest_UserIsLocked_Failed() throws ValidationException {
        Mockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(existingUser));

        existingUser.setStatus(UserStatus.LOCKED);

        var exceptionUserIsLocked = assertThrows(
                ValidationException.class,
                () -> userService.updateUser(
                        UUID.fromString("6fb9c7ec-a08f-44ca-8be3-25d6d50efef0"), userUpdateRequest));

        Assertions.assertThat(exceptionUserIsLocked.getMessage())
                .isEqualTo("locked: " + "User đang bị khóa! Không thể cập nhật");
    }

    @Test
    void UpdateUser_InvalidRequest_UsernameExisted_Failed() throws ValidationException {
        Mockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(existingUser));

        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(true);

        existingUser.setUsername("guest");

        var exceptionUserIsLocked = assertThrows(
                ValidationException.class,
                () -> userService.updateUser(
                        UUID.fromString("6fb9c7ec-a08f-44ca-8be3-25d6d50efef0"), userUpdateRequest));

        Assertions.assertThat(exceptionUserIsLocked.getMessage()).isEqualTo("username: " + "Người dùng đã tồn tại!");
    }

    @Test
    void UpdateUser_InvalidRequest_PhoneExisted_Failed() throws ValidationException {
        Mockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(existingUser));

        Mockito.when(userRepository.existsByPhone(anyString())).thenReturn(true);

        existingUser.setPhone("0967710509");

        var exceptionUserIsLocked = assertThrows(
                ValidationException.class,
                () -> userService.updateUser(
                        UUID.fromString("6fb9c7ec-a08f-44ca-8be3-25d6d50efef0"), userUpdateRequest));

        Assertions.assertThat(exceptionUserIsLocked.getMessage())
                .isEqualTo("phone: " + "Số điện thoại này đã được sử dụng!");
    }

    @Test
    void UpdateUser_InvalidRequest_EmailExisted_Failed() throws ValidationException {
        Mockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(existingUser));

        Mockito.when(userRepository.existsByEmail(anyString())).thenReturn(true);

        existingUser.setEmail("guest@gmail.com");

        var exceptionUserIsLocked = assertThrows(
                ValidationException.class,
                () -> userService.updateUser(
                        UUID.fromString("6fb9c7ec-a08f-44ca-8be3-25d6d50efef0"), userUpdateRequest));

        Assertions.assertThat(exceptionUserIsLocked.getMessage()).isEqualTo("email: " + "Email đã tồn tại!");
    }

    @Test
    void UpdateUser_InvalidRequest_IdentificationNumberExisted_Failed() throws ValidationException {
        Mockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(existingUser));

        Mockito.when(userRepository.existsByIdentificationNumber(anyString())).thenReturn(true);

        existingUser.setIdentificationNumber("1202034");

        var exceptionUserIsLocked = assertThrows(
                ValidationException.class,
                () -> userService.updateUser(
                        UUID.fromString("6fb9c7ec-a08f-44ca-8be3-25d6d50efef0"), userUpdateRequest));

        Assertions.assertThat(exceptionUserIsLocked.getMessage())
                .isEqualTo("identificationNumber: " + "Số CMND/CCCD không hợp lệ hoặc đã tồn tại!");
    }

    @Test
    void LockUser_ValidRequest_Success() throws ValidationException {
        Mockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(existingUser));

        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(existingUser);

        existingUser.setStatus(UserStatus.LOCKED);

        var resultUserLocked = userService.lockUser(existingUser.getId());

        Assertions.assertThat(resultUserLocked.getId().toString()).isEqualTo("6fb9c7ec-a08f-44ca-8be3-25d6d50efef0");
        Assertions.assertThat(resultUserLocked.getStatus().toString()).isEqualTo("LOCKED");
    }

    @Test
    void LockUser_InvalidRequest_UserIdNotExisted_Failed() throws ValidationException {
        Mockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

        var exceptionUserIdNotExisted = assertThrows(
                ValidationException.class,
                () -> userService.lockUser(UUID.fromString("6fb9c7ec-a08f-44ca-8be3-25d6d50efef0")));

        Assertions.assertThat(exceptionUserIdNotExisted.getMessage()).isEqualTo("error: " + "User not found!");
    }

    @Test
    void UnlockUser_ValidRequest_Success() throws ValidationException {
        Mockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(existingUser));

        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(existingUser);

        existingUser.setStatus(UserStatus.ACTIVE);

        var resultUserUnlocked = userService.unLockUser(existingUser.getId());

        Assertions.assertThat(resultUserUnlocked.getId().toString()).isEqualTo("6fb9c7ec-a08f-44ca-8be3-25d6d50efef0");
        Assertions.assertThat(resultUserUnlocked.getStatus().toString()).isEqualTo("ACTIVE");
    }

    @Test
    void UnlockUser_InvalidRequest_UserIdNotExisted_Failed() throws ValidationException {
        Mockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

        var exceptionUserIdNotExisted = assertThrows(
                ValidationException.class,
                () -> userService.unLockUser(UUID.fromString("6fb9c7ec-a08f-44ca-8be3-25d6d50efef0")));

        Assertions.assertThat(exceptionUserIdNotExisted.getMessage()).isEqualTo("Error: " + "User not found!");
    }

    @Test
    public void testSortAndPagingAndSearch_AdminRole() throws ValidationException {
        // Arrange
        String orderBy = "ASC";
        int page = 1;
        int limit = 5;
        String orderedColumn = "username";
        String name = "John";
        String phone = "123456789";
        String identificationNumber = "123456";
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcyNjYzNDg2MiwiZXhwIjoxNzI2NzIxMjYyfQ.KEgMO_xV8WRaZ9zgv-B7BiBUctqXnaJL5gSsBqfwOII";

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.valueOf(orderBy.toUpperCase()), orderedColumn));
        User mockUser = new User();
        Page<User> userPage = new PageImpl<>(Collections.singletonList(mockUser), pageable, 1);
        UserResponse mockUserResponse = new UserResponse();

        // Mock behaviors
        Mockito.when(jwtAuthenticationFilter.getJwtFromHeader(httpServletRequest)).thenReturn(token);
        Mockito.when(jwtService.extractRole(token)).thenReturn("ADMIN");
        Mockito.when(userRepository.listUserSearchedAndPagingFromDBForRoleAdmin(name, phone, identificationNumber, pageable)).thenReturn(userPage);
        Mockito.when(mapper.toResponse(mockUser)).thenReturn(mockUserResponse);

        // Act
        Page<UserResponse> result = userService.sortAndPagingAndSearch(orderBy, page, limit, orderedColumn, name, phone, identificationNumber, httpServletRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(jwtAuthenticationFilter).getJwtFromHeader(httpServletRequest);
        verify(jwtService).extractRole(token);
        verify(userRepository).listUserSearchedAndPagingFromDBForRoleAdmin(name, phone, identificationNumber, pageable);
    }
}
