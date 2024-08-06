package vn.eledevo.vksbe.dto.request;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MassActionRequest<ID> {

    @NotEmpty
    private Set<ID> ids;
}
