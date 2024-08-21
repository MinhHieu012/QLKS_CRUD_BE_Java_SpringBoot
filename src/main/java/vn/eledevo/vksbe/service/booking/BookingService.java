package vn.eledevo.vksbe.service.booking;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import vn.eledevo.vksbe.dto.request.booking.BookingRequest;
import vn.eledevo.vksbe.dto.request.booking.BookingUpdateRequest;
import vn.eledevo.vksbe.dto.response.booking.BookingResponse;
import vn.eledevo.vksbe.exception.ValidationException;

public interface BookingService {
    List<BookingResponse> getAllBookings();

    BookingResponse addBooking(BookingRequest bookingRequest) throws ValidationException;

    BookingResponse updateBooking(Long id, BookingUpdateRequest bookingUpdateRequest) throws ValidationException;

    BookingResponse cancelBooking(Long id) throws ValidationException;

    List<BookingResponse> sortAndPagingAndSearch(
            String orderBy,
            int page,
            int limit,
            String orderedColumn,
            Long bookingId,
            String roomName,
            String userName,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkInDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkOutDate);
}
