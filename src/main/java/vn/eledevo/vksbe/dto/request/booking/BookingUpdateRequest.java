package vn.eledevo.vksbe.dto.request.booking;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.constant.BookingStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BookingUpdateRequest {
    UUID userId;
    Integer roomId;
    LocalDateTime checkInDate;
    LocalDateTime checkoutDate;
    String amount;
    String deposit;
    @Enumerated(EnumType.STRING)
    BookingStatus status;
}
