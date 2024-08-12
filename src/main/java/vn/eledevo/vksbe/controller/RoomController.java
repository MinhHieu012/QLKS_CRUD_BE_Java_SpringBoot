package vn.eledevo.vksbe.controller;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import vn.eledevo.vksbe.dto.request.room.RoomRequest;
import vn.eledevo.vksbe.dto.response.ApiResponse;
import vn.eledevo.vksbe.exception.ValidationException;
import vn.eledevo.vksbe.service.room.RoomService;

@RestController
@RequestMapping("/admin/quanlyphong")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService service;

    @GetMapping
    public ApiResponse getAllRoom() {
        return new ApiResponse<>(200, "Get all room success!", service.getAllRoom());
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse addRoom(@RequestBody RoomRequest roomRequest) throws ValidationException {
        return new ApiResponse<>(201, "Add room success!", service.addRoom(roomRequest));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse updateRoom(@PathVariable Integer id, @RequestBody @Valid RoomRequest roomRequest)
            throws ValidationException {
        return new ApiResponse<>(204, "Update room success!", service.updateRoom(id, roomRequest));
    }

    @PutMapping("/roomstatus/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse updateRoomStatus(@PathVariable Integer id, @RequestParam String status) throws ValidationException {
        return new ApiResponse<>(204, "Update room status success!", service.updateRoomStatus(id, status));
    }
}
