package es.spb.englishmaster.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnglishLevelRequest {

    private Long id;

    @NotBlank(message = "El código del nivel es obligatorio")
    @Size(max = 10, message = "El código no puede tener más de 10 caracteres")
    private String code;

    @NotBlank(message = "El nombre del nivel es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    private String description;

    private Integer orderIndex;
}