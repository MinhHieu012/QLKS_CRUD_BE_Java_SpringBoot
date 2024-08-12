package vn.eledevo.vksbe.dto.request.login_register;

import java.util.Date;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.constant.Role;
import vn.eledevo.vksbe.constant.UserStatus;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    String username;
    String email;
    String password;
    String phone;
    String identificationNumber;
    Date dateOfBirth;
    UserStatus status;
    Role role;
}
