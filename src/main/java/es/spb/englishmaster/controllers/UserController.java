package es.spb.englishmaster.controllers;

import es.spb.englishmaster.entity.EnglishLevelEntity;
import es.spb.englishmaster.entity.UserEntity;
import es.spb.englishmaster.repository.UserRepository;
import es.spb.englishmaster.service.JwtService;
import es.spb.englishmaster.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{userId}/english-level")
    public ResponseEntity<?> getUserEnglishLevel(@PathVariable String userId, HttpServletRequest request) {
        Integer userIdToUse;
    
        if ("me".equals(userId)) {
            // Obtener el usuario autenticado del contexto de seguridad
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || 
            authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No hay sesión de usuario activa");
        }
        
        // El principal debería ser una instancia de UserDetails de nuestro sistema
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername(); // En Spring Security, el username suele ser el email
        
        // Buscar el usuario por email
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        
        userIdToUse = user.get().getId();
    } else {
        try {
            userIdToUse = Integer.parseInt(userId);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("ID de usuario inválido");
        }
    }
    
    return userService.getUserEnglishLevel(userIdToUse)
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