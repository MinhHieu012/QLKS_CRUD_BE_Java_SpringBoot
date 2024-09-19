package vn.eledevo.vksbe.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import lombok.extern.log4j.Log4j2;
import vn.eledevo.vksbe.dto.request.room.RoomRequest;
import vn.eledevo.vksbe.dto.response.ApiResponse;
import vn.eledevo.vksbe.dto.response.room.RoomResponse;
import vn.eledevo.vksbe.dto.response.roomtype.RoomTypeResponseDTO;
import vn.eledevo.vksbe.repository.RoomRepository;
import vn.eledevo.vksbe.service.room.RoomService;

@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
public class TestRoomController {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @MockBean
    private RoomRepository roomRepository;

    private RoomRequest roomRequest;
    private RoomResponse roomResponse;
    private ApiResponse apiResponse;
    private RoomTypeResponseDTO roomTypeResponseDTO;
    private List<RoomResponse> roomList;

    @BeforeEach
    void initData() {
        roomTypeResponseDTO = RoomTypeResponseDTO.builder()
                .id(1L)
                .name("Deluxe")
                .maxPeople("5")
                .description("Deluxe Room")
                .build();

        roomList = Arrays.asList(
                RoomResponse.builder()
                        .id(1)
                        .name("Phòng A")
                        .roomNumber("101")
                        .floor("1")
                        .roomType(roomTypeResponseDTO)
                        .description("Mô tả A1")
                        .price("100000")
                        .build(),
                RoomResponse.builder()
                        .id(2)
                        .name("Phòng B")
                        .roomNumber("102")
                        .floor("1")
                        .roomType(roomTypeResponseDTO)
                        .description("Mô tả B")
                        .price("100000")
                        .build());
    }

    @Test
    void GetAllUsers_ReturnsListOfUsers_Success() throws Exception {

        Mockito.when(roomService.getAllRoom()).thenReturn(roomList);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/quanlyphong").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
