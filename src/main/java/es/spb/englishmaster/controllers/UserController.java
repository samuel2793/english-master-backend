package es.spb.englishmaster.controllers;

import es.spb.englishmaster.dto.UserResponse;
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
    public ResponseEntity<?> getUserEnglishLevel(@PathVariable String userId) {
        Integer userIdToUse = getUserIdFromPathVariable(userId);
        if (userIdToUse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no válido o no autenticado");
        }

        return userService.getUserEnglishLevel(userIdToUse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok().build()); // Devuelve 200 con cuerpo vacío si es null
    }

    @PutMapping("/{userId}/english-level/{levelCode}")
    public ResponseEntity<?> setUserEnglishLevel(
            @PathVariable String userId,
            @PathVariable String levelCode) {

        Integer userIdToUse = getUserIdFromPathVariable(userId);
        if (userIdToUse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserEntity updatedUser = userService.setUserEnglishLevelByCode(userIdToUse, levelCode);
        return ResponseEntity.ok(UserResponse.fromEntity(updatedUser));
    }


    @PutMapping("/{userId}/remove-english-level")
    public ResponseEntity<?> removeUserEnglishLevel(@PathVariable String userId) {
        Integer userIdToUse = getUserIdFromPathVariable(userId);
        if (userIdToUse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserEntity updatedUser = userService.setUserEnglishLevel(userIdToUse, null);
        return ResponseEntity.ok(UserResponse.fromEntity(updatedUser));
    }


    // Método auxiliar para obtener el ID del usuario actual o convertir el ID proporcionado
    private Integer getUserIdFromPathVariable(String userId) {
        if ("me".equals(userId)) {
            // Obtener el usuario autenticado del contexto de seguridad
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || 
                authentication instanceof AnonymousAuthenticationToken) {
                return null; // No hay usuario autenticado
            }
            
            // El principal debería ser una instancia de UserDetails de nuestro sistema
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername(); // En Spring Security, el username suele ser el email
            
            // Buscar el usuario por email
            Optional<UserEntity> user = userRepository.findByEmail(email);
            if (user.isEmpty()) {
                return null; // Usuario no encontrado
            }
            
            return user.get().getId();
        } else {
            try {
                return Integer.parseInt(userId);
            } catch (NumberFormatException e) {
                return null; // ID de usuario inválido
            }
        }
    }
}