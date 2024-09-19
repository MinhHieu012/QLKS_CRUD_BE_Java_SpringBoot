package vn.eledevo.vksbe.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

import java.util.Date;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;

import vn.eledevo.vksbe.constant.Role;
import vn.eledevo.vksbe.constant.UserStatus;
import vn.eledevo.vksbe.dto.request.user.UserAddRequest;
import vn.eledevo.vksbe.dto.request.user.UserUpdateRequest;
import vn.eledevo.vksbe.dto.response.user.UserResponse;
import vn.eledevo.vksbe.entity.User;
import vn.eledevo.vksbe.exception.ValidationException;
import vn.eledevo.vksbe.repository.UserRepository;
import vn.eledevo.vksbe.service.user.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
@TestPropertySource("/test.properties")
@WithUserDetails(value = "admin", userDetailsServiceBeanName = "testUserAdmin")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserAddRequest userRequest;
    private UserUpdateRequest userUpdateRequest;
    private UserResponse userResponse;
    private User user;
    private Date dateOfBirth;

    @BeforeEach
    void initData() {
        dateOfBirth = java.sql.Date.valueOf("2002-11-09");

        userRequest = UserAddRequest.builder()
                .username("guest")
                .email("guest@gmail.com")
                .password("123456")
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

        var exceptionUsernameExisted = assertThrows(ValidationException.class, () -> userService.createUser(userRequest));

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
}
