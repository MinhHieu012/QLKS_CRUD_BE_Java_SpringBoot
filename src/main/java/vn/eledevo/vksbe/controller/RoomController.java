package vn.eledevo.vksbe.controller;

import jakarta.annotation.Nullable;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse getAllRoom() {
        return new ApiResponse<>(200, "Get all room success!", service.getAllRoom());
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse addRoom(@RequestBody RoomRequest roomRequest) throws ValidationException {
        return new ApiResponse<>(201, "Add room success!", service.addRoom(roomRequest));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse updateRoom(@PathVariable Integer id, @RequestBody @Valid RoomRequest roomRequest)
            throws ValidationException {
        return new ApiResponse<>(204, "Update room success!", service.updateRoom(id, roomRequest));
    }

    @PutMapping("/roomstatus/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse updateRoomStatus(@PathVariable Integer id, @RequestParam String status)
            throws ValidationException {
        return new ApiResponse<>(204, "Update room status success!", service.updateRoomStatus(id, status));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse sortAndPagingAndSearch(
            @RequestParam(value = "orderBy", defaultValue = "DESC") String orderBy,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            @RequestParam(value = "orderedColumn", defaultValue = "createdAt") String orderedColumn,
            @Nullable @RequestParam("name") String name,
            @Nullable @RequestParam("roomNumber") String roomNumber,
            @Nullable @RequestParam("floor") String floor,
            @Nullable @RequestParam("roomTypeId") Long roomTypeId,
            @Nullable @RequestParam("status") String status)
            throws ValidationException {
        return new ApiResponse<>(
                200,
                "Filter room success!",
                service.sortAndPagingAndSearch(
                        orderBy, page, limit, orderedColumn, name, roomNumber, floor, roomTypeId, status));
    }
}
