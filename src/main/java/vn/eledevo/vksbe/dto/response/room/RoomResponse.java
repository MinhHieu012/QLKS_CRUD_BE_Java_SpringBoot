package vn.eledevo.vksbe.dto.response.room;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoomResponse {
    Integer id;
    String name;
    String roomNumber;
    String floor;
    Long roomTypeId;
    String description;
    String price;
    String roomStatus;
}
