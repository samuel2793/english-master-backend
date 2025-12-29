package es.spb.englishmaster.service;

import es.spb.englishmaster.dto.LevelExerciseCatalogResponse;
import es.spb.englishmaster.dto.UpsertLevelExerciseCatalogRequest;
import es.spb.englishmaster.entity.EnglishLevelEntity;
import es.spb.englishmaster.entity.ExerciseCategoryEntity;
import es.spb.englishmaster.entity.ExerciseTypeEntity;
import es.spb.englishmaster.repository.EnglishLevelRepository; // si no existe, usa EnglishLevelService + repo
import es.spb.englishmaster.repository.ExerciseCategoryRepository;
import es.spb.englishmaster.repository.ExerciseTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ExerciseCatalogService {

    private final EnglishLevelRepository englishLevelRepository;
    private final ExerciseCategoryRepository categoryRepository;
    private final ExerciseTypeRepository typeRepository;

    @Transactional
    public LevelExerciseCatalogResponse getCatalog(Long levelId) {
        EnglishLevelEntity level = englishLevelRepository.findById(levelId)
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado: " + levelId));

        // Ordena categorías por orderIndex si existe
        List<ExerciseCategoryEntity> categories = new ArrayList<>(Optional.ofNullable(level.getAvailableCategories()).orElse(Set.of()));
        categories.sort(Comparator.comparing(c -> Optional.ofNullable(c.getOrderIndex()).orElse(9999)));

        // Agrupa tipos por categoryCode
        Map<String, List<ExerciseTypeEntity>> typesByCategory = new HashMap<>();
        for (ExerciseTypeEntity t : Optional.ofNullable(level.getAvailableTypes()).orElse(Set.of())) {
            typesByCategory.computeIfAbsent(t.getCategory().getCode(), k -> new ArrayList<>()).add(t);
        }
        for (var entry : typesByCategory.entrySet()) {
            entry.getValue().sort(Comparator.comparing(t -> Optional.ofNullable(t.getOrderIndex()).orElse(9999)));
        }

        var outCats = categories.stream().map(cat -> {
            var types = typesByCategory.getOrDefault(cat.getCode(), List.of()).stream()
                    .map(t -> LevelExerciseCatalogResponse.TypeItem.builder()
                            .code(t.getCode())
                            .name(t.getName())
                            .build())
                    .toList();

            return LevelExerciseCatalogResponse.CategoryWithTypes.builder()
                    .code(cat.getCode())
                    .name(cat.getName())
                    .types(types)
                    .build();
        }).toList();

        return LevelExerciseCatalogResponse.builder()
                .levelId(level.getId())
                .levelCode(level.getCode())
                .categories(outCats)
                .build();
    }

    /**
     * Reemplaza completamente el catálogo del nivel.
     */
    @Transactional
    public LevelExerciseCatalogResponse upsertCatalog(Long levelId, UpsertLevelExerciseCatalogRequest request) {
        EnglishLevelEntity level = englishLevelRepository.findById(levelId)
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado: " + levelId));

        Set<ExerciseCategoryEntity> newCategories = new HashSet<>();
        Set<ExerciseTypeEntity> newTypes = new HashSet<>();

        if (request.getItems() != null) {
            for (var item : request.getItems()) {
                ExerciseCategoryEntity cat = categoryRepository.findByCode(item.getCategoryCode())
                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + item.getCategoryCode()));
                newCategories.add(cat);

                List<String> typeCodes = Optional.ofNullable(item.getTypeCodes()).orElse(List.of());
                for (String typeCode : typeCodes) {
                    ExerciseTypeEntity type = typeRepository.findByCode(typeCode)
                            .orElseThrow(() -> new RuntimeException("Tipo no encontrado: " + typeCode));

                    // Guardrail: el tipo debe pertenecer a la categoría
                    if (!type.getCategory().getCode().equals(cat.getCode())) {
                        throw new RuntimeException("El tipo " + typeCode + " no pertenece a la categoría " + cat.getCode());
                    }
                    newTypes.add(type);
                }
            }
        }

        level.setAvailableCategories(newCategories);
        level.setAvailableTypes(newTypes);
        englishLevelRepository.save(level);

        return getCatalog(levelId);
    }
}
