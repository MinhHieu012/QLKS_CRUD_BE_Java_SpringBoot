package vn.eledevo.vksbe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;

import java.util.UUID;

import static vn.eledevo.vksbe.constant.ResponseMessage.USER_IDENTIFICATION_NUMBER_INVALID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roomtype")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_id")
    Long id;

    @Column(unique = true)
    String name;

    @Pattern(regexp = "[0-9]*$", message = "Trường này chỉ nhận giá trị số!")
    @Column(name = "max_people")
    String maxPeople;

    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = true, updatable = true)
    User user;
}