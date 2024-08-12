package vn.eledevo.vksbe.dto.response.user;

import java.util.Date;
import java.util.UUID;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.constant.Role;
import vn.eledevo.vksbe.constant.UserStatus;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserResponse {
    UUID id;
    String username;
    String email;
    Date dateOfBirth;
    String phone;
    Long identificationNumber;
    Role role;
    UserStatus status;
}
