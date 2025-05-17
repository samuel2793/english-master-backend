package es.spb.englishmaster.dto;

import es.spb.englishmaster.constants.ErrorMessages;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    @NotBlank(message = ErrorMessages.FIELD_CANNOT_BE_BLANK)
    private String nif;

    private String nombre;
    private String apellidos;
    private String razonSocial;

    @AssertTrue(message = ErrorMessages.INVALID_NAME)
    public boolean isNameValid() {
        return (nombre != null && apellidos != null) || razonSocial != null;
    }

    @NotBlank(message = ErrorMessages.FIELD_CANNOT_BE_BLANK)
    @Email(message = ErrorMessages.INVALID_EMAIL)
    private String email;

    @NotBlank(message = ErrorMessages.FIELD_CANNOT_BE_BLANK)
    private String password;

    @NotNull(message = ErrorMessages.FIELD_CANNOT_BE_BLANK)
    @Valid
    private DireccionDTO direccion;
}
