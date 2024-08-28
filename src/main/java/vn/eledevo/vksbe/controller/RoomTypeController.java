package vn.eledevo.vksbe.controller;

import jakarta.annotation.Nullable;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import vn.eledevo.vksbe.dto.request.roomtype.RoomTypeRequest;
import vn.eledevo.vksbe.dto.response.ApiResponse;
import vn.eledevo.vksbe.exception.ValidationException;
import vn.eledevo.vksbe.service.roomtype.RoomTypeService;

@RestController
@RequestMapping("/admin/quanlykieuphong")
@RequiredArgsConstructor
@CrossOrigin
public class RoomTypeController {
    private final RoomTypeService service;

    @GetMapping
    public ApiResponse getAllRoomType() {
        return new ApiResponse<>(200, "Get all room type success!", service.getAllRoomType());
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse addRoomType(@RequestBody RoomTypeRequest roomTypeRequest) throws ValidationException {
        return new ApiResponse<>(200, "Add room type success!", service.addRoomType(roomTypeRequest));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse updateRoomType(@PathVariable Long id, @RequestBody RoomTypeRequest roomTypeRequest)
            throws ValidationException {
        return new ApiResponse<>(200, "Update room type success!", service.updateRoomType(id, roomTypeRequest));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse deleteRoomType(@PathVariable Long id) throws ValidationException {
        return new ApiResponse<>(200, "Delete room type success!", service.deleteRoomType(id));
    }

    @GetMapping("/filter")
    public ApiResponse filterRoomType(
            @RequestParam(value = "orderBy", defaultValue = "ASC") String orderBy,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "2") int limit,
            @RequestParam(value = "orderedColumn", defaultValue = "name") String orderedColumn,
            @Nullable @RequestParam(value = "name") String name,
            @Nullable @RequestParam(value = "maxPeople") String maxPeople)
            throws ValidationException {
        return new ApiResponse<>(
                200,
                "Filter room type success!",
                service.filterRoomType(orderBy, page, limit, orderedColumn, name, maxPeople));
    }
}
