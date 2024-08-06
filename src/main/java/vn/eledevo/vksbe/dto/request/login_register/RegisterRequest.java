package vn.eledevo.vksbe.dto.request.login_register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.constant.ResponseMessage;
import vn.eledevo.vksbe.constant.Role;
import vn.eledevo.vksbe.constant.UserStatus;

import java.util.Date;

import static vn.eledevo.vksbe.constant.ResponseMessage.USER_IDENTIFICATION_NUMBER_INVALID;

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
