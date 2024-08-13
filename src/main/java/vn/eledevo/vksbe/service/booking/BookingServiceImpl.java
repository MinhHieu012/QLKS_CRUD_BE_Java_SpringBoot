package vn.eledevo.vksbe.service.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.eledevo.vksbe.dto.request.booking.BookingRequest;
import vn.eledevo.vksbe.dto.response.booking.BookingResponse;
import vn.eledevo.vksbe.entity.Booking;
import vn.eledevo.vksbe.entity.Room;
import vn.eledevo.vksbe.entity.User;
import vn.eledevo.vksbe.mapper.BookingMapper;
import vn.eledevo.vksbe.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    BookingRepository repository;

    BookingMapper mapper;

    @Override
    public List<BookingResponse> getAllBookings() {
        List<Booking> bookingListFromDB = repository.findAll();
        List<BookingResponse> bookingListToFE = bookingListFromDB
                .stream()
                .map(mapper::toResponse)
                .toList();
        return bookingListToFE;
    }

    @Override
    public BookingResponse addBooking(BookingRequest bookingRequest) {
        return null;
    }

    @Override
    public BookingResponse updateBooking(Long id, BookingRequest bookingRequest) {
        return null;
    }

    @Override
    public BookingResponse cancelBooking(Long id) {
        return null;
    }

    @Override
    public List<BookingResponse> sortAndPagingAndSearch(
            String orderBy, int page, int limit, String orderedColumn,
            Long id, Room roomName, User userName, LocalDateTime checkInDate, LocalDateTime checkOutData)
    {
        return null;
    }
}
