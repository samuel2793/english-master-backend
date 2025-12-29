package es.spb.englishmaster.repository;

import es.spb.englishmaster.entity.ExerciseCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ExerciseCategoryRepository extends JpaRepository<ExerciseCategoryEntity, Long> {
    Optional<ExerciseCategoryEntity> findByCode(String code);
}
