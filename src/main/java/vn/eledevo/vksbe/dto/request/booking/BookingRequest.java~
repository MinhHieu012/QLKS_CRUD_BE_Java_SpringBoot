package vn.eledevo.vksbe.dto.request.booking;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
