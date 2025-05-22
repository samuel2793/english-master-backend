package es.spb.englishmaster.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    private String jwt;
    private String email;
    private String username;
}
