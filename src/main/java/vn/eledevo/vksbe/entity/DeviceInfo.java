package vn.eledevo.vksbe.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "device_info")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String deviceUuid;
    String status;

    LocalDateTime createdAt;
    UUID createdBy;
    LocalDateTime updatedAt;
    UUID updatedBy;
    Boolean isDeleted;

    /**
     * mappedBy = "deviceInfo" : Liên kết bảng đc quản lý bởi thuộc tính "deviceInfo" bên entity UserDeviceInfoKey
     * cascade = CascadeType.ALL : Theo dõi các hoạt động từ DeviceInfo đến UserDeviceInfoKey
     * orphanRemoval = true : Xóa bản ghi tương ứng của UserDeviceInfoKey khi DeviceInfo bị xóa
     */
    @OneToMany(mappedBy = "deviceInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    // Liên kết với Entity UserDeviceInfoKey
    List<UserDeviceInfoKey> userDeviceInfoKeys;

    @PrePersist
    void prePersist() {
        this.isDeleted = false;
        this.status = "Not Connect";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
