package es.spb.englishmaster.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private String translationKey;

    public static ErrorResponse of(String message, String translationKey) {
        return new ErrorResponse(message, translationKey);
    }
}
