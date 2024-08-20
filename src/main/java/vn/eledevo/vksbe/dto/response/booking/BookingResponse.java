package vn.eledevo.vksbe.dto.response.booking;

import java.time.LocalDateTime;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.constant.BookingStatus;
import vn.eledevo.vksbe.entity.Room;
import vn.eledevo.vksbe.entity.User;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BookingResponse {
    Long id;
    Room room;
    User user;
    LocalDateTime checkInDate;
    LocalDateTime checkoutDate;

    @Enumerated(EnumType.STRING)
    BookingStatus status;
}
