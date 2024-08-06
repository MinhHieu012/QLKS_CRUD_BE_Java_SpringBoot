package vn.eledevo.vksbe.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.eledevo.vksbe.dto.request.DeviceInfoRequest;
import vn.eledevo.vksbe.dto.response.ApiResponse;
import vn.eledevo.vksbe.dto.response.DeviceInfoResponse;
import vn.eledevo.vksbe.exception.ValidationException;
import vn.eledevo.vksbe.service.device_info.DeviceInfoService;

@RestController
@RequestMapping("/api/v1/public/device-info")
@RequiredArgsConstructor
public class DeviceInfoController {
    private final DeviceInfoService deviceInfoService;

    @PostMapping("")
    public ApiResponse<DeviceInfoResponse> createUser(@RequestBody @Valid DeviceInfoRequest deviceInfoRequest)
            throws ValidationException {
        return ApiResponse.ok(deviceInfoService.createDevice(deviceInfoRequest));
    }
}
