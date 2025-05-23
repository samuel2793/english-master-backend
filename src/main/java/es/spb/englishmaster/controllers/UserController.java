package es.spb.englishmaster.controllers;

import es.spb.englishmaster.entity.EnglishLevelEntity;
import es.spb.englishmaster.entity.UserEntity;
import es.spb.englishmaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}/english-level")
    public ResponseEntity<?> getUserEnglishLevel(@PathVariable Integer userId) {
        return userService.getUserEnglishLevel(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok().build()); // Devuelve 200 con cuerpo vacío si es null
    }

    @PutMapping("/{userId}/english-level/{levelId}")
    public ResponseEntity<UserEntity> setUserEnglishLevel(
            @PathVariable Integer userId,
            @PathVariable(required = false) Long levelId) {

        UserEntity updatedUser = userService.setUserEnglishLevel(userId, levelId);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{userId}/remove-english-level")
    public ResponseEntity<UserEntity> removeUserEnglishLevel(@PathVariable Integer userId) {
        UserEntity updatedUser = userService.setUserEnglishLevel(userId, null);
        return ResponseEntity.ok(updatedUser);
    }
}