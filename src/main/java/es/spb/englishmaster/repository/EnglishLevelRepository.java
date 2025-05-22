package es.spb.englishmaster.repository;

import es.spb.englishmaster.entity.EnglishLevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnglishLevelRepository extends JpaRepository<EnglishLevelEntity, Long> {
}