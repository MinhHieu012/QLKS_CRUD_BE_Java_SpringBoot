package vn.eledevo.vksbe.dto.request.room;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoomRequest {
    String name;
    String roomNumber;
    String floor;
    Long roomTypeId;
    String description;
    String price;
    String status;
}
