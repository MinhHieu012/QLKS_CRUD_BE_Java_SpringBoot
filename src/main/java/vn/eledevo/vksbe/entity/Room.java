package vn.eledevo.vksbe.entity;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.constant.RoomStatus;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "room")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;

    @Pattern(regexp = "[0-9]*$", message = "Trường này chỉ nhận giá trị số!")
    @Column(name = "room_number")
    String roomNumber;

    @Pattern(regexp = "[0-9]*$", message = "Trường này chỉ nhận giá trị số!")
    String floor;

    @Pattern(regexp = "[0-9]*$", message = "Trường này chỉ nhận giá trị số!")
    String price;

    String description;

    @Enumerated(EnumType.STRING)
    RoomStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id")
    RoomType roomType;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Booking> Booking;
}
