package vn.eledevo.vksbe.mapper;

import org.mapstruct.Mapper;
import vn.eledevo.vksbe.dto.request.booking.BookingRequest;
import vn.eledevo.vksbe.dto.request.room.RoomRequest;
import vn.eledevo.vksbe.dto.response.booking.BookingResponse;
import vn.eledevo.vksbe.dto.response.room.RoomResponse;
import vn.eledevo.vksbe.entity.Booking;
import vn.eledevo.vksbe.entity.Room;

@Mapper(componentModel = "spring")
public abstract class BookingMapper extends BaseMapper<BookingRequest, BookingResponse, Booking> {}
