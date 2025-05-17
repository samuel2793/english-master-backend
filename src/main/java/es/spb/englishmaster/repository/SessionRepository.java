package es.spb.englishmaster.repository;

import es.spb.englishmaster.entity.SessionEntity;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Integer> {

    Optional<SessionEntity> findByAccessToken(String token);

    Optional<SessionEntity> findByAccessTokenAndRefreshTokenAndPublicId(String accessToken, String refreshToken, String publicId);

    @Modifying
    @Transactional
    void deleteByPublicId(String publicId);
}