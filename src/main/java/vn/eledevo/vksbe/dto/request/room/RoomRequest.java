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
    Integer roomNumber;
    Integer floor;
    Long roomTypeId;
    String description;
    Integer price;
    String status;
}
