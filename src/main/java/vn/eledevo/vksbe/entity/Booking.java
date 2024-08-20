package vn.eledevo.vksbe.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.constant.BookingStatus;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booking")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "checkin_date")
    LocalDateTime checkInDate;

    @Column(name = "checkout_date")
    LocalDateTime checkoutDate;

    @Pattern(regexp = "[0-9]*$", message = "Trường này chỉ nhận giá trị số!")
    String amount;

    @Pattern(regexp = "[0-9]*$", message = "Trường này chỉ nhận giá trị số!")
    String deposit;

    @Enumerated(EnumType.STRING)
    BookingStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    Room room;
}
