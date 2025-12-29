package es.spb.englishmaster.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpsertLevelExerciseCatalogRequest {

    // ejemplo:
    // [
    //   { "categoryCode":"reading", "typeCodes":["long_text","missing_sentences"] },
    //   { "categoryCode":"writing", "typeCodes":[] }
    // ]
    private List<CategoryTypes> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryTypes {
        @NotBlank
        private String categoryCode;
        private List<String> typeCodes; // null => lo tratamos como []
    }
}
