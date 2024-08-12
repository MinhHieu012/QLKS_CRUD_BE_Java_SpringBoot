package vn.eledevo.vksbe.dto.request.roomtype;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoomTypeRequest {
    String name;
    @Pattern(regexp = "[0-9]*$", message = "Trường này chỉ nhận giá trị số!")
    String maxPeople;
    String description;
}
