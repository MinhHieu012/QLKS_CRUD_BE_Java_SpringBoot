package vn.eledevo.vksbe.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.constant.Role;
import vn.eledevo.vksbe.constant.UserStatus;

import java.util.Date;
import java.util.UUID;

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
