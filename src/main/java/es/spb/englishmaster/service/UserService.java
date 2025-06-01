package es.spb.englishmaster.service;

import es.spb.englishmaster.entity.EnglishLevelEntity;
import es.spb.englishmaster.entity.UserEntity;
import es.spb.englishmaster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EnglishLevelService englishLevelService;

    @Autowired
    public UserService(UserRepository userRepository, EnglishLevelService englishLevelService) {
        this.userRepository = userRepository;
        this.englishLevelService = englishLevelService;
    }

    public Optional<UserEntity> findById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    @Transactional
    public Optional<EnglishLevelEntity> getUserEnglishLevel(Integer userId) {
        return userRepository.findById(userId)
                .map(UserEntity::getEnglishLevel);
    }

    @Transactional
    public UserEntity setUserEnglishLevel(Integer userId, Long levelId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));
        
        if (levelId == null) {
            user.setEnglishLevel(null);
        } else {
            EnglishLevelEntity level = englishLevelService.findById(levelId)
                    .orElseThrow(() -> new RuntimeException("Nivel de inglés no encontrado con ID: " + levelId));
            user.setEnglishLevel(level);
        }
        
        return userRepository.save(user);
    }

    @Transactional
    public UserEntity setUserEnglishLevelByCode(Integer userId, String levelCode) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));

        if (levelCode == null || levelCode.isEmpty()) {
            user.setEnglishLevel(null);
        } else {
            EnglishLevelEntity level = englishLevelService.findByCode(levelCode)
                .orElseThrow(() -> new RuntimeException("Nivel de inglés no encontrado con código: " + levelCode));
            user.setEnglishLevel(level);
        }

        return userRepository.save(user);
    }
}