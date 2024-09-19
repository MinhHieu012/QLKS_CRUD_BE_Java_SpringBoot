package vn.eledevo.vksbe.controller;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.log4j.Log4j2;
import vn.eledevo.vksbe.VksBeApplication;
import vn.eledevo.vksbe.constant.Role;
import vn.eledevo.vksbe.constant.UserStatus;
import vn.eledevo.vksbe.dto.request.user.UserAddRequest;
import vn.eledevo.vksbe.dto.request.user.UserUpdateRequest;
import vn.eledevo.vksbe.dto.response.ApiResponse;
import vn.eledevo.vksbe.dto.response.user.UserResponse;
import vn.eledevo.vksbe.entity.User;
import vn.eledevo.vksbe.repository.UserRepository;
import vn.eledevo.vksbe.service.user.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = VksBeApplication.class)
@Log4j2
public class TestUserController {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserAddRequest userRequest;
    private UserUpdateRequest userUpdateRequest;
    private UserResponse userResponse;
    private ApiResponse apiResponse;
    private User user;
    private Date dateOfBirth;
    private List<UserResponse> userListGet;

    @BeforeEach
    void initDataGet() {
        userListGet = Arrays.asList(
                UserResponse.builder()
                        .id(UUID.randomUUID())
                        .username("guestA")
                        .email("guestA@gmail.com")
                        .dateOfBirth(new Date())
                        .phone("0351186849")
                        .identificationNumber(Long.valueOf("035648657"))
                        .role(Role.USER)
                        .status(UserStatus.ACTIVE)
                        .build(),
                UserResponse.builder()
                        .id(UUID.randomUUID())
                        .username("guestB")
                        .email("guestB@gmail.com")
                        .dateOfBirth(new Date())
                        .phone("0351186841")
                        .identificationNumber(Long.valueOf("039648657"))
                        .role(Role.USER)
                        .status(UserStatus.ACTIVE)
                        .build());

        dateOfBirth = java.sql.Date.valueOf("2002-11-09");

        userRequest = UserAddRequest.builder()
                .username("guest")
                .email("guest@gmail.com")
                .password("123456")
                .phone("0967710509")
                .identificationNumber("001202034")
                .dateOfBirth(dateOfBirth)
                .build();

        userResponse = UserResponse.builder()
                .id(UUID.randomUUID())
                .username("guest")
                .email("guest@gmail.com")
                .dateOfBirth(dateOfBirth)
                .phone("0967710509")
                .identificationNumber(Long.valueOf("001202034"))
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .build();

        apiResponse =
                ApiResponse.builder().code(201).message("Create user success!").build();
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() throws Exception {

        Mockito.when(userService.getAllUser()).thenReturn(userListGet);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/quanlyuser").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Get all user success!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.length()").value(2))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.result[0].username").value("guestA"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].email").value("guestA@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].role").value("USER"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.result[1].username").value("guestB"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[1].email").value("guestB@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[1].role").value("USER"));
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void addUser_validRequest_success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(userRequest);
        objectMapper.registerModule(new JavaTimeModule());

        log.info("DOB: " + dateOfBirth);

        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/quanlyuser/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Create user success!"));
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void lockUser_invalidRequest_MissingParams_failed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/admin/quanlyuser/lock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(500))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Internal Server Error"));
    }
}
