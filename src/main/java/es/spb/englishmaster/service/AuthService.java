package es.spb.englishmaster.service;

import es.spb.englishmaster.dto.AuthenticationResponse;
import es.spb.englishmaster.dto.LoginRequest;
import es.spb.englishmaster.dto.RegisterRequest;
import es.spb.englishmaster.entity.PasswordResetEntity;
import es.spb.englishmaster.entity.SessionEntity;
import es.spb.englishmaster.entity.UserEntity;
import es.spb.englishmaster.exception.ConflictException;
import es.spb.englishmaster.exception.DataNotFoundException;
import es.spb.englishmaster.exception.InvalidTokenException;
import es.spb.englishmaster.exception.UnauthorizedException;
import es.spb.englishmaster.repository.PasswordResetRepository;
import es.spb.englishmaster.repository.SessionRepository;
import es.spb.englishmaster.repository.UserRepository;
import es.spb.englishmaster.type.AccountStatusType;
import es.spb.englishmaster.type.RoleType;
import es.spb.englishmaster.type.TokenType;
import jakarta.transaction.Transactional;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    public static final String VERIFY_EMAIL_PATH = "/api/v1/auth/verification/verify?token=";

    @Value("${application.host}")
    private String host;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordResetRepository passwordResetRepository;

    @Autowired
    private final SessionRepository sessionRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final AuthenticationManager authenticationManager;

    //@Autowired
    //private final EmailingService emailingService;

    private static final SecureRandom secureRandom = new SecureRandom(); // thread safe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    private final static String EMAIL_SUBJECT = "Verifica tu correo";
    private final static String EMAIL_TEXT = "Por favor verifique su correo en el siguiente enlace {link}";

    private final static Integer PASSWORD_RESET_EXPIRATION = 60 * 60; // SECONDS

    public void recoverPassword(String token, String newPassword) {
        // Obtenemos el PasswordResetEntity
        PasswordResetEntity passwordReset = passwordResetRepository.findByToken(token).orElseThrow(DataNotFoundException::tokenNotFound);

        // Comprobamos que no ha caducado el token
        if (passwordReset.getExpiryDate().before(new Date())) {
            throw InvalidTokenException.tokenExpired();
        }

        // Comprobamos que no se ha usado el token
        if (passwordReset.isUsed()) {
            throw InvalidTokenException.tokenAlreadyUsed();
        }

        // Cambiamos la contraseña y guardamos
        UserEntity userEntity = passwordReset.getUserEntity();
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);

        passwordReset.setUsed(true);
        passwordResetRepository.save(passwordReset);
    }

    public AuthenticationResponse register(RegisterRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent((userEntity) -> {
            throw ConflictException.emailIsRegistered();
        });

        List<RoleType> roleTypeList = new ArrayList<>();
        roleTypeList.add(RoleType.USER);
        UserEntity userEntity = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountStatusType(AccountStatusType.NOT_VERIFIED)
                .verificationToken(jwtService.generateEmailVerificationToken(request.getEmail()))
                .rolesList(roleTypeList)
                .build();

        var savedUser = saveCredentials(userEntity);

        var accessToken = jwtService.generateAccessToken(userEntity);
        var refreshToken = jwtService.generateRefreshToken(userEntity);
        var publicId = getPublicId();

        saveUserSession(savedUser, accessToken, refreshToken, publicId);

        var jwt = jwtService.generateJWT(userEntity, accessToken, refreshToken, publicId);
        return AuthenticationResponse.builder()
                .jwt(jwt)
                .build();
    }

    @Transactional
    public void logout(String rawToken) {
        if (rawToken == null || !rawToken.startsWith("Bearer ")) {
            return;
        }
        String jwt = rawToken.substring(7);
        sessionRepository.findByAccessToken(jwtService.extractAccessToken(jwt)).ifPresent(sessionRepository::delete);
    }

    @Transactional
    @Modifying
    public void verifyEmailByToken(String token) {
        UserEntity userEntity = userRepository.findByVerificationToken(token).orElseThrow(UnauthorizedException::notAuthorized);

        userEntity.setVerificationToken(null);
        userEntity.setAccountStatusType(AccountStatusType.VERIFIED);

        userRepository.save(userEntity);
    }

    public AuthenticationResponse login(LoginRequest request) {
        UserEntity userEntity = userRepository.findByEmail(request.getEmail())
                .orElseThrow(DataNotFoundException::userNotFound);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var accessToken = jwtService.generateAccessToken(userEntity);
        var refreshToken = jwtService.generateRefreshToken(userEntity);
        var publicId = getPublicId();

        saveUserSession(userEntity, accessToken, refreshToken, publicId);

        var jwt = jwtService.generateJWT(userEntity, accessToken, refreshToken, publicId);

        return AuthenticationResponse.builder()
                .jwt(jwt)
                .build();
    }

    private UserEntity saveCredentials(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    private void saveUserSession(UserEntity userEntity, String accessToken,
                                 String refreshToken, String publicId) {
        SessionEntity session = SessionEntity.builder()
                .user(userEntity)
                .publicId(publicId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(TokenType.BEARER)
                .build();
        sessionRepository.save(session);
    }

    public static String getPublicId() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}