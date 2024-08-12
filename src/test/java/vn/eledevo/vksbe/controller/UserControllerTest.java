package vn.eledevo.vksbe.controller;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.eledevo.vksbe.constant.ResponseMessage;
import vn.eledevo.vksbe.constant.Role;
import vn.eledevo.vksbe.dto.request.user.UserAddRequest;
import vn.eledevo.vksbe.dto.response.user.UserResponse;
import vn.eledevo.vksbe.entity.User;
import vn.eledevo.vksbe.service.user.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserAddRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void initData() {
        userRequest = UserAddRequest.builder()
                .username("long")
                .email("long@gmail.com")
                .password("123456")
                .build();

        userResponse = UserResponse.builder()
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
    //    // Kiểm tra mã code trả về khi tạo tài khoản thành công
    //    void createUser_validRequest_success() throws Exception {
    //        String content = objectMapper.writeValueAsString(userRequest);
    //
    //        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);
    //        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/private/users")
    //                        .contentType(MediaType.APPLICATION_JSON_VALUE)
    //                        .content(content))
    //                .andExpect(MockMvcResultMatchers.status().isOk())
    //                .andExpect(MockMvcResultMatchers.jsonPath("code").value(200));
    //    }

    @Test
    @WithUserDetails(value = "john", userDetailsServiceBeanName = "testUserDetailsService")
    // Kiểm tra mã code, message, result.username trả về trong trường hợp nhập thiếu ký tự tên đăng nhập
    void createUser_usernameInvalid_fail() throws Exception {
        userRequest.setUsername("ts");
        String content = objectMapper.writeValueAsString(userRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/private/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(422))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Unprocessable Entity"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.username").value(ResponseMessage.USER_SIZE));
    }
}
