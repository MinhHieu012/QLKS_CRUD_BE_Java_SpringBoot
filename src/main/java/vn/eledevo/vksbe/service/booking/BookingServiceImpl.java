package vn.eledevo.vksbe.service.booking;

import static vn.eledevo.vksbe.constant.ResponseMessage.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.constant.BookingStatus;
import vn.eledevo.vksbe.dto.request.booking.BookingRequest;
import vn.eledevo.vksbe.dto.request.booking.BookingUpdateRequest;
import vn.eledevo.vksbe.dto.response.booking.BookingResponse;
import vn.eledevo.vksbe.dto.response.booking.RoomDTOResponse;
import vn.eledevo.vksbe.dto.response.booking.UserDTOResponse;
import vn.eledevo.vksbe.entity.Booking;
import vn.eledevo.vksbe.entity.Room;
import vn.eledevo.vksbe.entity.User;
import vn.eledevo.vksbe.exception.ValidationException;
import vn.eledevo.vksbe.mapper.BookingMapper;
import vn.eledevo.vksbe.repository.BookingRepository;
import vn.eledevo.vksbe.repository.RoomRepository;
import vn.eledevo.vksbe.repository.UserRepository;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    BookingRepository bookingRepository;
    RoomRepository roomRepository;
    UserRepository userRepository;

    BookingMapper mapper;

    @Override
    public List<BookingResponse> getAllBookings() {
        List<Booking> bookingListFromDB = bookingRepository.findAll();
        List<BookingResponse> bookingListToFE =
                bookingListFromDB.stream().map(mapper::toResponse).toList();
        return bookingListToFE;
    }

    @Override
    public BookingResponse addBooking(BookingRequest bookingRequest) throws ValidationException {

        if (bookingRepository.validateSameBooking(
                bookingRequest.getRoomId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckoutDate())) {
            throw new ValidationException("Lỗi", BOOKING_EXISTS);
        }

        if (bookingRepository.validateOnRangeBooking(
                bookingRequest.getRoomId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckoutDate())) {
            throw new ValidationException("Lỗi", BOOKING_EXISTS_IN_RANGE);
        }

        if (bookingRepository.validateRange1HourBooking(
                bookingRequest.getRoomId(), bookingRequest.getCheckInDate().minusHours(1))) {
            throw new ValidationException("Lỗi", BOOKING_EXISTS_IN_RANGE_1H);
        }

        Optional<Room> roomDataFromDB = roomRepository.findById(bookingRequest.getRoomId());
        if (roomDataFromDB.isEmpty()) {
            throw new ValidationException("Lỗi", "Phòng bạn chọn không tồn tại");
        }
        Room room = roomDataFromDB.get();

        Optional<User> userDataFromDB = userRepository.findById(bookingRequest.getUserId());
        if (userDataFromDB.isEmpty()) {
            throw new ValidationException("Lỗi", "Người dùng không tồn tại");
        }
        User user = userDataFromDB.get();

        Booking booking = mapper.toEntity(bookingRequest);
        booking.setRoom(room);
        booking.setUser(user);
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(booking);

        BookingResponse bookingResponseToFE = mapper.toResponse(savedBooking);

        return bookingResponseToFE;
    }

    @Override
    public BookingResponse updateBooking(Long id, BookingUpdateRequest bookingUpdateRequest)
            throws ValidationException {
        Booking booking = bookingRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("Booking", "Booking not found!"));

        if (bookingRepository.validateSameBooking(
                bookingUpdateRequest.getRoomId(),
                bookingUpdateRequest.getCheckInDate(),
                bookingUpdateRequest.getCheckoutDate())) {
            throw new ValidationException("Lỗi", BOOKING_EXISTS);
        }

        if (bookingRepository.validateOnRangeBooking(
                bookingUpdateRequest.getRoomId(),
                bookingUpdateRequest.getCheckInDate(),
                bookingUpdateRequest.getCheckoutDate())) {
            throw new ValidationException("Lỗi", BOOKING_EXISTS_IN_RANGE);
        }

        if (bookingRepository.validateRange1HourBooking(
                bookingUpdateRequest.getRoomId(),
                bookingUpdateRequest.getCheckInDate().minusHours(1))) {
            throw new ValidationException("Lỗi", BOOKING_EXISTS_IN_RANGE_1H);
        }

        Optional<Room> roomDataFromDB = roomRepository.findById(bookingUpdateRequest.getRoomId());
        if (roomDataFromDB.isEmpty()) {
            throw new ValidationException("Lỗi", "Phòng bạn chọn không tồn tại");
        }
        Room roomUpdateSaveToEntity = roomDataFromDB.get();

        Optional<User> userDataFromDB = userRepository.findById(bookingUpdateRequest.getUserId());
        if (userDataFromDB.isEmpty()) {
            throw new ValidationException("Lỗi", "Người dùng không tồn tại");
        }
        User userUpdateDataSaveToEntity = userDataFromDB.get();

        booking.setRoom(roomUpdateSaveToEntity);
        booking.setUser(userUpdateDataSaveToEntity);
        booking.setStatus(bookingUpdateRequest.getStatus());

        Booking bookingUpdateData = bookingRepository.save(booking);

        BookingResponse bookingResponseToFE = mapper.toResponse(bookingUpdateData);

        return bookingResponseToFE;
    }

    @Override
    public BookingResponse cancelBooking(Long id) throws ValidationException {
        Booking booking = bookingRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("Booking", "Booking not found!"));
        booking.setStatus(BookingStatus.CANCEL);
        Booking bookingCancelData = bookingRepository.save(booking);
        return null;
    }

    @Override
    public List<BookingResponse> sortAndPagingAndSearch(
            String orderBy,
            int page,
            int limit,
            String orderedColumn,
            Long bookingId,
            String roomName,
            String userName,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkInDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkOutDate) {
        Pageable bookingPageable =
                PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.valueOf(orderBy.toUpperCase()), orderedColumn));

        List<Booking> bookingList = bookingRepository.listBookingSearchedAndPagingFromDB(
                bookingId, userName, roomName, checkInDate, checkOutDate, bookingPageable);

        List<BookingResponse> listSortAndPagingAndSearch = bookingList.stream()
                .map(booking -> {
                    RoomDTOResponse roomDTO = new RoomDTOResponse(
                            booking.getRoom().getId(),
                            booking.getRoom().getName(),
                            booking.getRoom().getRoomNumber(),
                            booking.getRoom().getFloor(),
                            booking.getRoom().getPrice());
                    UserDTOResponse userDTO = new UserDTOResponse(
                            booking.getUser().getId(),
                            booking.getUser().getUsername(),
                            booking.getUser().getPhone());
                    return BookingResponse.builder()
                            .id(booking.getId())
                            .room(roomDTO)
                            .user(userDTO)
                            .checkInDate(booking.getCheckInDate())
                            .checkoutDate(booking.getCheckoutDate())
                            .status(booking.getStatus())
                            .build();
                })
                .collect(Collectors.toList());

        return listSortAndPagingAndSearch;
    }
}
