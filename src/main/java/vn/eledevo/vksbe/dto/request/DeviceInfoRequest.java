package vn.eledevo.vksbe.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.constant.ResponseMessage;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DeviceInfoRequest {
    @NotBlank(message = ResponseMessage.DEVICE_INFO_BLANK)
    String name;

    @NotBlank(message = ResponseMessage.DEVICE_INFO_UUID_BLANK)
    String deviceUuid;
}
