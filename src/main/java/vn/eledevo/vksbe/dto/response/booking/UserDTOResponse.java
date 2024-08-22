package vn.eledevo.vksbe.dto.response.booking;

import java.util.UUID;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserDTOResponse {
    UUID id;
    String username;
    String phone;
}
