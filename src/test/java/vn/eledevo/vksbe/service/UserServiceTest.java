package vn.eledevo.vksbe.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

import vn.eledevo.vksbe.constant.ResponseMessage;
import vn.eledevo.vksbe.constant.Role;
import vn.eledevo.vksbe.dto.request.user.UserAddRequest;
import vn.eledevo.vksbe.entity.User;
import vn.eledevo.vksbe.exception.ValidationException;
import vn.eledevo.vksbe.repository.UserRepository;
import vn.eledevo.vksbe.service.user.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserAddRequest userRequest;
    private User user;

    @BeforeEach
    void initData() {
        userRequest = UserAddRequest.builder()
                .username("long")
                .email("long@gmail.com")
                .password("123456")
                .build();

        user = User.builder()
                .id(UUID.fromString("88100e6d-99fe-44cd-aa44-1d2aa5c04d52"))
                .username("long")
                .email("long@gmail.com")
                .build();
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserDetailsService testUserDetailsService() {
            User userMock = new User();
            userMock.setRole(Role.ADMIN);
            return username -> userMock;
        }
    }

    //    @Test
    //    @WithUserDetails(value = "john", userDetailsServiceBeanName = "testUserDetailsService")
    //    // Kiểm tra Id, Username trả về khi tạo tài khoản
    //    void createUser_validRequest_success() throws Exception {
    //        when(userRepository.existsByUsername(anyString())).thenReturn(false);
    //        when(userRepository.save(any())).thenReturn(user);
    //        var response = userService.createUser(userRequest);
    //
    // Assertions.assertThat(response.getId()).isEqualTo(UUID.fromString("88100e6d-99fe-44cd-aa44-1d2aa5c04d52"));
    //        Assertions.assertThat(response.getUsername()).isEqualTo("long");
    //    }

    @Test
    // Kiểm tra username có tồn tại khi tạo tài khoản
    void createUser_userExisted_fail() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        var exception = assertThrows(ValidationException.class, () -> userService.createUser(userRequest));
        Assertions.assertThat(exception.getErrors().get("username")).isEqualTo(ResponseMessage.USER_EXIST);
    }
}
