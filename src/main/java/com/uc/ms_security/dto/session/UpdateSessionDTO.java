package com.uc.ms_security.dto.session;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSessionDTO extends BaseSessionDTO {
    // Hereda token, expiration y code2FA de BaseSessionDTO.
    // El userId no es modificable para mantener la sesión vinculada a su usuario original.
}
