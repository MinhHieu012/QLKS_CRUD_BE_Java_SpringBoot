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
public class RoomTypeResponseDTO {
    Long id;
    String name;
    String maxPeople;
    String description;
}
