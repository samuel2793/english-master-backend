package es.spb.englishmaster.controllers;

import es.spb.englishmaster.entity.EnglishLevelEntity;
import es.spb.englishmaster.service.EnglishLevelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/english-levels")
public class EnglishLevelController {

    private final EnglishLevelService englishLevelService;

    @Autowired
    public EnglishLevelController(EnglishLevelService englishLevelService) {
        this.englishLevelService = englishLevelService;
    }

    /**
     * Obtiene todas las dificultades (niveles de inglés)
     * @return Lista de niveles de inglés
     */
    @GetMapping
    public ResponseEntity<List<EnglishLevelEntity>> getAllLevels() {
        List<EnglishLevelEntity> levels = englishLevelService.findAll();
        return ResponseEntity.ok(levels);
    }

    /**
     * Obtiene un nivel de inglés específico por su ID
     * @param id Identificador del nivel
     * @return El nivel de inglés solicitado
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnglishLevelEntity> getLevelById(@PathVariable Long id) {
        return englishLevelService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo nivel de inglés
     * @param englishLevel Datos del nuevo nivel
     * @return El nivel creado con su ID asignado
     */
    @PostMapping
    public ResponseEntity<EnglishLevelEntity> createLevel(@Valid @RequestBody EnglishLevelEntity englishLevel) {
        // Nos aseguramos de que sea una creación, no una actualización
        englishLevel.setId(null);
        EnglishLevelEntity savedLevel = englishLevelService.save(englishLevel);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLevel);
    }

    /**
     * Actualiza un nivel de inglés existente
     * @param id ID del nivel a actualizar
     * @param englishLevel Nuevos datos del nivel
     * @return El nivel actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<EnglishLevelEntity> updateLevel(@PathVariable Long id,
                                                    @Valid @RequestBody EnglishLevelEntity englishLevel) {
        return englishLevelService.findById(id)
                .map(existingLevel -> {
                    englishLevel.setId(id);
                    EnglishLevelEntity updatedLevel = englishLevelService.update(englishLevel);
                    return ResponseEntity.ok(updatedLevel);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un nivel de inglés
     * @param id ID del nivel a eliminar
     * @return Respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLevel(@PathVariable Long id) {
        return englishLevelService.findById(id)
                .map(level -> {
                    englishLevelService.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}