package vn.eledevo.vksbe.repository;

import vn.eledevo.vksbe.entity.DeviceInfo;

public interface DeviceInfoRepository extends BaseRepository<DeviceInfo, Long> {
    Boolean existsByName(String name);
}
