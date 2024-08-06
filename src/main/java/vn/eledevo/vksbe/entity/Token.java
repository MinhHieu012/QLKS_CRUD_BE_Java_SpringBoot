package vn.eledevo.vksbe.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.constant.TokenType;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "token")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String accessToken;

    @Enumerated(EnumType.STRING)
    TokenType tokenType = TokenType.BEARER;

    Boolean isExpired;
    Boolean isRevoked;

    /**
     * fetch = FetchType.LAZY: Hoãn việc tải dữ liệu liên quan cho đến khi cần thiết -> Tối ưu hóa hiệu suất
     * @JoinColumn(name = "user_id") : Chỉ định khóa ngoại là "user_id" trong bảng Token
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    User user;
}
