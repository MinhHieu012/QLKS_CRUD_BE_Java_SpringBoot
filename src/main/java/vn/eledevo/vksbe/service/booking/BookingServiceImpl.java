package vn.eledevo.vksbe.service.booking;

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
import vn.eledevo.vksbe.entity.Booking;
import vn.eledevo.vksbe.entity.Room;
import vn.eledevo.vksbe.entity.User;
import vn.eledevo.vksbe.exception.ValidationException;
import vn.eledevo.vksbe.mapper.BookingMapper;
import vn.eledevo.vksbe.repository.BookingRepository;
import vn.eledevo.vksbe.repository.RoomRepository;
import vn.eledevo.vksbe.repository.UserRepository;
import vn.eledevo.vksbe.utils.SecurityUtils;

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
    public BookingResponse addBooking(BookingRequest bookingRequest) {

        User userUUID = new User();
        userUUID.setId(SecurityUtils.getUserId());

        Optional<Room> roomDataFromDB = roomRepository.findById(bookingRequest.getRoomId());
        Room roomDataSaveToEntityBooking = new Room();
        roomDataSaveToEntityBooking.setId(roomDataFromDB.get().getId());
        roomDataSaveToEntityBooking.setName(roomDataFromDB.get().getName());
        roomDataSaveToEntityBooking.setRoomNumber(roomDataFromDB.get().getRoomNumber());
        roomDataSaveToEntityBooking.setFloor(roomDataFromDB.get().getFloor());

        Optional<User> userDataFromDB = userRepository.findById(bookingRequest.getUserId());
        User userDataSaveToEntityBooking = new User();
        userDataSaveToEntityBooking.setId(userDataFromDB.get().getId());
        userDataSaveToEntityBooking.setUsername(userDataFromDB.get().getUsername());
        userDataSaveToEntityBooking.setPhone(userDataFromDB.get().getPhone());
        userDataSaveToEntityBooking.setRole(userDataFromDB.get().getRole());

        Booking booking = mapper.toEntity(bookingRequest);

        booking.setRoom(roomDataSaveToEntityBooking);
        booking.setUser(userDataSaveToEntityBooking);

        booking.setStatus(BookingStatus.PENDING);

        Booking bookingAddDataToDB = bookingRepository.save(booking);

        BookingResponse bookingResponseToFE = mapper.toResponse(bookingAddDataToDB);

        bookingResponseToFE.setRoom(roomDataSaveToEntityBooking);
        bookingResponseToFE.setUser(userDataSaveToEntityBooking);

        return bookingResponseToFE;
    }

    @Override
    public BookingResponse updateBooking(Long id, BookingUpdateRequest bookingUpdateRequest)
            throws ValidationException {
        Booking booking = bookingRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("Booking", "Booking not found!"));

        User userUUID = new User();
        userUUID.setId(SecurityUtils.getUserId());

        Optional<Room> roomDataFromDB = roomRepository.findById(bookingUpdateRequest.getRoomId());
        Room roomDataSaveToEntityBooking = new Room();
        roomDataSaveToEntityBooking.setId(roomDataFromDB.get().getId());
        roomDataSaveToEntityBooking.setName(roomDataFromDB.get().getName());
        roomDataSaveToEntityBooking.setRoomNumber(roomDataFromDB.get().getRoomNumber());
        roomDataSaveToEntityBooking.setFloor(roomDataFromDB.get().getFloor());

        Optional<User> userDataFromDB = userRepository.findById(bookingUpdateRequest.getUserId());
        User userDataSaveToEntityBooking = new User();
        userDataSaveToEntityBooking.setId(userDataFromDB.get().getId());
        userDataSaveToEntityBooking.setUsername(userDataFromDB.get().getUsername());
        userDataSaveToEntityBooking.setPhone(userDataFromDB.get().getPhone());
        userDataSaveToEntityBooking.setRole(userDataFromDB.get().getRole());

        booking.setUser(userDataSaveToEntityBooking);
        booking.setRoom(roomDataSaveToEntityBooking);
        booking.setCheckInDate(bookingUpdateRequest.getCheckInDate());
        booking.setCheckoutDate(bookingUpdateRequest.getCheckoutDate());
        booking.setAmount(bookingUpdateRequest.getAmount());
        booking.setDeposit(bookingUpdateRequest.getDeposit());
        booking.setStatus(bookingUpdateRequest.getStatus());

        Booking bookingUpdateData = bookingRepository.save(booking);

        BookingResponse bookingResponseToFE = mapper.toResponse(bookingUpdateData);
        bookingResponseToFE.setUser(userDataSaveToEntityBooking);
        bookingResponseToFE.setRoom(roomDataSaveToEntityBooking);

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
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkOutDate)
    {
        Pageable bookingPageable =
                PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.valueOf(orderBy.toUpperCase()), orderedColumn));

        List<Booking> bookingList = bookingRepository.listBookingSearchedAndPagingFromDB(
                bookingId, userName, roomName, checkInDate, checkOutDate, bookingPageable
        );

        List<BookingResponse> listSortAndPagingAndSearch = bookingList
                .stream()
                .map(mapper::toResponse)
                .toList();

        return listSortAndPagingAndSearch;
    }
}
