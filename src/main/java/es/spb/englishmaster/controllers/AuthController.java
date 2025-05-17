package es.spb.englishmaster.controllers;

import es.spb.englishmaster.dto.AuthenticationResponse;
import es.spb.englishmaster.dto.LoginRequest;
import es.spb.englishmaster.dto.RegisterRequest;
import es.spb.englishmaster.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    @Autowired
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {;
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @GetMapping("/verification/verify")
    public ResponseEntity<HttpStatus> verifyEmailByToken(@RequestParam("token") String token) {
        authService.verifyEmailByToken(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/recover")
    public ResponseEntity<HttpStatus> recoverPassword(@RequestParam("token") String token, @RequestBody @Valid String password) {
        authService.recoverPassword(token, password);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }
}
