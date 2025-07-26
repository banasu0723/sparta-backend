package hello.auth.dto;

import hello.auth.entity.Role;
import hello.auth.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Schema(description = "회원가입/사용자 정보 응답 DTO")
public class SignupResponse {
    @Schema(description = "사용자명", example = "테스트유저")
    private String username;

    @Schema(description = "닉네임", example = "테스트유저")
    private String nickname;

    @Schema(description = "사용자 권한 목록")
    private List<RoleDto> roles;

    public SignupResponse() {
    }

    public SignupResponse(User user){
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.roles = user.getRoles().stream()
                .map(RoleDto::new)
                .collect(Collectors.toList());
    }

    @Schema(description = "권한 정보")
    public static class RoleDto {
        @Schema(description = "권한명", example = "USER")
        private String role;

        public RoleDto(){
        }

        public RoleDto(Role role){
            this.role = role.getValue();
        }

        public String getRole() {
            return role;
        }
        public void setRole(String role) {
            this.role = role;
        }
    }
}
