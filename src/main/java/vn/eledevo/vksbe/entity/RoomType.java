package vn.eledevo.vksbe.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import lombok.*;
import lombok.experimental.FieldDefaults;

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

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @OneToOne(mappedBy = "roomType", cascade = CascadeType.ALL)
    Room room;
}
