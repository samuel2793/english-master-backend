package es.spb.englishmaster.repository;

import es.spb.englishmaster.entity.ExerciseTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseTypeRepository extends JpaRepository<ExerciseTypeEntity, Long> {
    Optional<ExerciseTypeEntity> findByCode(String code);
    List<ExerciseTypeEntity> findByCategory_Code(String categoryCode);
}
