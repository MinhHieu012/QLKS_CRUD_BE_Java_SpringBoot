package vn.eledevo.vksbe.entity;

import static vn.eledevo.vksbe.constant.ResponseMessage.USER_IDENTIFICATION_NUMBER_INVALID;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.constant.Role;
import vn.eledevo.vksbe.constant.UserStatus;
import vn.eledevo.vksbe.exception.ValidationException;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    UUID id;

    @Column(unique = true)
    String username;

    @Column(unique = true)
    String email;

    String password;

    @Column(name = "date_of_birth")
    @JsonFormat(pattern = "yyyy/MM/dd")
    Date dateOfBirth;

    @Column(name = "identification_number", unique = true)
    @Pattern(regexp = "^(0)[0-9]*$", message = USER_IDENTIFICATION_NUMBER_INVALID)
    String identificationNumber;

    @Pattern(regexp = "^(03|04|05|07|08|09)[0-9]{8,9}$", message = "Số điện thoại không hợp lệ")
    String phone;

    @Enumerated(EnumType.STRING)
    Role role;

    @Enumerated(EnumType.STRING)
    UserStatus status;

    LocalDateTime createdAt;
    UUID createdBy;
    LocalDateTime updatedAt;
    UUID updatedBy;
    Boolean isDeleted;

    /**
     * mappedBy = "user" : Liên kết (FK) với bảng UserDeviceInfoKey đc quản lý bởi thuộc tính "user" bên entity UserDeviceInfoKey
     * cascade = CascadeType.ALL : Theo dõi các hoạt động từ User đến UserDeviceInfoKey
     * orphanRemoval = true : Xóa bản ghi tương ứng của UserDeviceInfoKey khi User bị xóa
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // Liên kết với Entity UserDeviceInfoKey
    List<UserDeviceInfoKey> userDeviceInfoKeys;

    /**
     * mappedBy = "user" : Liên kết (FK) bảng UserDeviceInfoKey đc quản lý bởi thuộc tính "user" bên entity Token
     * cascade = CascadeType.ALL : Theo dõi các hoạt động từ User đến Token
     * orphanRemoval = true : Xóa bản ghi tương ứng của Tokens khi User bị xóa
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // Liên kết với Entity Token
    List<Token> Tokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<RoomType> RoomTypes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Room> Room;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Booking> Booking;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

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
