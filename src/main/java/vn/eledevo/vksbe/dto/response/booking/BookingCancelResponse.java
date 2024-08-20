package vn.eledevo.vksbe.dto.response.booking;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.constant.BookingStatus;
import vn.eledevo.vksbe.entity.Room;
import vn.eledevo.vksbe.entity.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BookingCancelResponse {
    Long id;
    @Enumerated(EnumType.STRING)
    BookingStatus status;
}
