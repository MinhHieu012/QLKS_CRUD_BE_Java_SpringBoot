package vn.eledevo.vksbe.service.device_info;

import vn.eledevo.vksbe.dto.request.DeviceInfoRequest;
import vn.eledevo.vksbe.dto.response.DeviceInfoResponse;
import vn.eledevo.vksbe.exception.ValidationException;

public interface DeviceInfoService {
    DeviceInfoResponse createDevice(DeviceInfoRequest deviceInfoRequest) throws ValidationException;
}
