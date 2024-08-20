package vn.eledevo.vksbe.service.booking;

import java.time.LocalDateTime;
import java.util.List;

import vn.eledevo.vksbe.dto.request.booking.BookingRequest;
import vn.eledevo.vksbe.dto.request.booking.BookingUpdateRequest;
import vn.eledevo.vksbe.dto.response.booking.BookingResponse;
import vn.eledevo.vksbe.entity.Room;
import vn.eledevo.vksbe.entity.User;
import vn.eledevo.vksbe.exception.ValidationException;

public interface BookingService {
    List<BookingResponse> getAllBookings();

    BookingResponse addBooking(BookingRequest bookingRequest);

    BookingResponse updateBooking(Long id, BookingUpdateRequest bookingUpdateRequest) throws ValidationException;

    BookingResponse cancelBooking(Long id) throws ValidationException;

    List<BookingResponse> sortAndPagingAndSearch(
            String orderBy,
            int page,
            int limit,
            String orderedColumn,
            Long id,
            Room roomName,
            User userName,
            LocalDateTime checkInDate,
            LocalDateTime checkOutData
    );
}