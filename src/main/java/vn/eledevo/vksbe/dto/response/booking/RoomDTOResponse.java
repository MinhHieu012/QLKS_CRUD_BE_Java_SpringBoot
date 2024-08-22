package vn.eledevo.vksbe.dto.response.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoomDTOResponse {
    Integer id;
    String name;
    String roomNumber;
    String floor;
    String price;
}
