package hello.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "회원가입 요청 DTO")
public class SignupRequest {
    @Schema(
            description = "사용자명",
            example = "testuser",
            required = true,
            minLength = 1
    )
    @NotBlank(message = "사용자명은 필수입니다")
    private String username;

    @Schema(
            description = "비밀번호",
            example = "testpass123",
            required = true,
            minLength = 8
    )
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    @Schema(
            description = "닉네임",
            example = "테스트유저",
            required = true,
            minLength = 1
    )
    @NotBlank(message = "닉네임은 필수입니다")
    private String nickname;

    public SignupRequest() {
    }

    public SignupRequest(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

}
