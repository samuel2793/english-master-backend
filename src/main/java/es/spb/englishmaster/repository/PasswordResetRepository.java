package es.spb.englishmaster.repository;

import es.spb.englishmaster.entity.PasswordResetEntity;
import es.spb.englishmaster.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordResetEntity, Integer> {
    Optional<PasswordResetEntity> findByToken(String token);

    Optional<PasswordResetEntity> findByUserEntity(UserEntity userEntity);
}
