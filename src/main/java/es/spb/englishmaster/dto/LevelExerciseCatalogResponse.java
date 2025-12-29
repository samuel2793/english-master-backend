package es.spb.englishmaster.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LevelExerciseCatalogResponse {
    private Long levelId;
    private String levelCode;
    private List<CategoryWithTypes> categories;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryWithTypes {
        private String code;
        private String name;
        private List<TypeItem> types; // puede ser []
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TypeItem {
        private String code;
        private String name;
    }
}
