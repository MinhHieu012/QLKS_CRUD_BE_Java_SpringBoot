package vn.eledevo.vksbe.service.booking;

import vn.eledevo.vksbe.dto.request.booking.BookingRequest;
import vn.eledevo.vksbe.dto.request.room.RoomRequest;
import vn.eledevo.vksbe.dto.response.booking.BookingResponse;
import vn.eledevo.vksbe.dto.response.room.RoomResponse;
import vn.eledevo.vksbe.entity.Room;
import vn.eledevo.vksbe.entity.User;
import vn.eledevo.vksbe.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    List<BookingResponse> getAllBookings();
    BookingResponse addBooking(BookingRequest bookingRequest);
    BookingResponse updateBooking(Long id, BookingRequest bookingRequest);
    BookingResponse cancelBooking(Long id);
    List<BookingResponse> sortAndPagingAndSearch (
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