package es.spb.englishmaster.service;

import es.spb.englishmaster.entity.EnglishLevelEntity;
import es.spb.englishmaster.repository.EnglishLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EnglishLevelService {

    private final EnglishLevelRepository englishLevelRepository;

    @Autowired
    public EnglishLevelService(EnglishLevelRepository englishLevelRepository) {
        this.englishLevelRepository = englishLevelRepository;
    }

    public List<EnglishLevelEntity> findAll() {
        return englishLevelRepository.findAll();
    }

    public Optional<EnglishLevelEntity> findById(Long id) {
        return englishLevelRepository.findById(id);
    }

    @Transactional
    public EnglishLevelEntity save(EnglishLevelEntity englishLevel) {
        return englishLevelRepository.save(englishLevel);
    }

    @Transactional
    public void deleteById(Long id) {
        englishLevelRepository.deleteById(id);
    }

    @Transactional
    public EnglishLevelEntity update(EnglishLevelEntity englishLevel) {
        // Asegura que existe antes de actualizar
        if (englishLevelRepository.existsById(englishLevel.getId())) {
            return englishLevelRepository.save(englishLevel);
        }
        throw new RuntimeException("No se encontró el nivel de inglés con ID: " + englishLevel.getId());
    }
}