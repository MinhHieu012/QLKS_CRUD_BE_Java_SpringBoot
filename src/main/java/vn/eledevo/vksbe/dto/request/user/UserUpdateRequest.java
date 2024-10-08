package vn.eledevo.vksbe.dto.request.user;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.constant.ResponseMessage;

@Setter
@Getter
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserUpdateRequest {
    @NotBlank(message = ResponseMessage.USER_BLANK)
    @Size(min = 3, max = 50, message = ResponseMessage.USER_SIZE)
    String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    String email;

    String phone;

    String identificationNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date dateOfBirth;
}
