package es.spb.englishmaster.dto;

import es.spb.englishmaster.entity.EnglishLevelEntity;
import es.spb.englishmaster.entity.UserEntity;
import es.spb.englishmaster.type.AccountStatusType;
import es.spb.englishmaster.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String username;
    private String email;
    private List<RoleType> rolesList;
    private AccountStatusType accountStatusType;
    private EnglishLevelEntity englishLevel;

    // Método estático para convertir de UserEntity a UserResponse
    public static UserResponse fromEntity(UserEntity user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .rolesList(user.getRolesList())
                .accountStatusType(user.getAccountStatusType())
                .englishLevel(user.getEnglishLevel())
                .build();
    }
}