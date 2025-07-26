package hello.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "로그인 응답 DTO")
public class LoginResponse {
    @Schema(
            description = "JWT 토큰",
            example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0dXNlciIsInJvbGVzIjpbIlVTRVIiXSwiaWF0IjoxNjk5NTM2MDAwLCJleHAiOjE2OTk1NDMyMDB9.signature"
    )
    private String token;
    public LoginResponse(){
    }
    public LoginResponse(String token) {
        this.token = token;
    }
}
