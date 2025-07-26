package hello.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "로그인 요청 DTO")
public class LoginRequest {
    @Schema(
            description = "사용자명",
            example = "testuser",
            required = true
    )
    @NotBlank(message = "사용자명은 필수입니다.")
    private String username;

    @Schema(
            description = "비밀번호",
            example = "testpass123",
            required = true
    )
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
