package com.uc.ms_security.dto.session;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseSessionDTO {

    @NotBlank(
            message = "El token es obligatorio"
    )
    @Size(
            max = 512,
            message = "El token no puede superar los 512 caracteres"
    )
    private String token;

    @NotNull(
            message = "La fecha de expiración es obligatoria"
    )
    @Future(
            message = "La fecha de expiración debe ser una fecha y hora futura"
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expiration;

    @Size(
            max = 10,
            message = "El código 2FA no puede superar los 10 caracteres"
    )
    private String code2FA;
}
