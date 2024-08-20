package vn.eledevo.vksbe.controller;

import java.time.LocalDateTime;

import jakarta.annotation.Nullable;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import vn.eledevo.vksbe.dto.request.booking.BookingRequest;
import vn.eledevo.vksbe.dto.request.booking.BookingUpdateRequest;
import vn.eledevo.vksbe.dto.response.ApiResponse;
import vn.eledevo.vksbe.exception.ValidationException;
import vn.eledevo.vksbe.service.booking.BookingService;

@RestController
@RequestMapping("/admin/quanlydatphong")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ApiResponse getAllBookings() {
        return new ApiResponse<>(200, "Get all booking success!", bookingService.getAllBookings());
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse addBooking(@RequestBody BookingRequest bookingRequest) {
        return new ApiResponse<>(201, "Create booking success!", bookingService.addBooking(bookingRequest));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse updateBooking(@PathVariable Long id, @RequestBody BookingUpdateRequest bookingUpdateRequest)
            throws ValidationException {
        return new ApiResponse<>(
                201, "Update booking success!", bookingService.updateBooking(id, bookingUpdateRequest));
    }

    @PatchMapping("/cancel/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse cancelBooking(@PathVariable Long id) throws ValidationException {
        return new ApiResponse<>(204, "Cancel booking success!", bookingService.cancelBooking(id));
    }

    @GetMapping("/filter")
    public ApiResponse sortAndPagingAndSearch(
            @RequestParam(value = "orderBy", defaultValue = "ASC") String orderBy,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "2") int limit,
            @RequestParam(value = "orderedColumn", defaultValue = "name") String orderedColumn,
            @Nullable @RequestParam("id") Long id,
            @Nullable @RequestParam("roomName") String roomName,
            @Nullable @RequestParam("userName") String userName,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkInDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkOutDate) {
        return new ApiResponse<>(
                200,
                "Filter booking success!",
                bookingService.sortAndPagingAndSearch(
                        orderBy,
                        page,
                        limit,
                        orderedColumn,
                        String.valueOf(id),
                        roomName,
                        userName,
                        checkInDate,
                        checkOutDate)
        );
    }
}
