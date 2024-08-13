package vn.eledevo.vksbe.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.eledevo.vksbe.dto.request.room.RoomRequest;
import vn.eledevo.vksbe.dto.response.ApiResponse;
import vn.eledevo.vksbe.exception.ValidationException;
import vn.eledevo.vksbe.service.booking.BookingService;
import vn.eledevo.vksbe.service.room.RoomService;

@RestController
@RequestMapping("/admin/quanlydatphong")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    @GetMapping
    public ApiResponse getAllBookings() {
        return new ApiResponse<>(200, "Get all booking success!", service.getAllBookings());
    }
}