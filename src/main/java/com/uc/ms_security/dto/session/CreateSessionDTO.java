package com.uc.ms_security.dto.session;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSessionDTO extends BaseSessionDTO {

    @NotNull(
            message = "El ID de usuario es obligatorio"
    )
    private Long userId;
}
