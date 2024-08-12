package vn.eledevo.vksbe.dto.response.roomtype;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoomTypeResponse {
    Long id;
    String name;
    int maxPeople;
    String description;
}
