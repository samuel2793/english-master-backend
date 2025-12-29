package es.spb.englishmaster.controllers;

import es.spb.englishmaster.dto.LevelExerciseCatalogResponse;
import es.spb.englishmaster.dto.UpsertLevelExerciseCatalogRequest;
import es.spb.englishmaster.service.ExerciseCatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/english-levels")
@RequiredArgsConstructor
public class EnglishLevelExerciseCatalogController {

    private final ExerciseCatalogService exerciseCatalogService;

    @GetMapping("/{levelId}/exercise-catalog")
    public ResponseEntity<LevelExerciseCatalogResponse> getCatalog(@PathVariable Long levelId) {
        return ResponseEntity.ok(exerciseCatalogService.getCatalog(levelId));
    }

    @PutMapping("/{levelId}/exercise-catalog")
    public ResponseEntity<LevelExerciseCatalogResponse> upsertCatalog(
            @PathVariable Long levelId,
            @RequestBody @Valid UpsertLevelExerciseCatalogRequest request
    ) {
        return ResponseEntity.ok(exerciseCatalogService.upsertCatalog(levelId, request));
    }
}
