package vn.eledevo.vksbe.mapper;

import org.mapstruct.Mapper;

import vn.eledevo.vksbe.dto.request.DeviceInfoRequest;
import vn.eledevo.vksbe.dto.response.DeviceInfoResponse;
import vn.eledevo.vksbe.entity.DeviceInfo;

@Mapper(componentModel = "spring")
public abstract class DeviceInfoMapper extends BaseMapper<DeviceInfoRequest, DeviceInfoResponse, DeviceInfo> {}
