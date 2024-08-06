package vn.eledevo.vksbe.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_device_info_key")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDeviceInfoKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String keyUsb;

    LocalDateTime createdAt;
    UUID createdBy;
    LocalDateTime updatedAt;
    UUID updatedBy;
    Boolean isDeleted;

    /**
     * fetch = FetchType.LAZY: Hoãn việc tải dữ liệu liên quan cho đến khi cần thiết -> Tối ưu hóa hiệu suất
     * @JoinColumn(name = "device_info_id") : Chỉ định khóa ngoại là "device_info_id" trong bảng DeviceInfo
     * insertable = false & updatable = false : Chặn ko cho thay đổi giá trị khóa ngoại
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_info_id", insertable = false, updatable = false)
    // Thuộc tính deviceInfo quản lý khóa ngoại FK
    DeviceInfo deviceInfo;

    /**
     * @JoinColumn(name = "user_id") : Chỉ định khóa ngoại là "user_id" trong bảng user_device_info_key
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    // Thuộc tính user quản lý khóa ngoại FK
    User user;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
