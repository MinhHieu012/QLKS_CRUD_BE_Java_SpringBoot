package vn.eledevo.vksbe.dto.request.booking;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.constant.RoomStatus;
import vn.eledevo.vksbe.entity.Room;
import vn.eledevo.vksbe.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BookingRequest {
    UUID userId;
    Integer roomId;
    LocalDateTime checkInDate;
    LocalDateTime checkoutDate;
    String amount;
    String deposit;
}
