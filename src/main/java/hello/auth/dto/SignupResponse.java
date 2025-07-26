package hello.auth.dto;

import hello.auth.entity.Role;
import hello.auth.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class SignupResponse {
    private String username;
    private String nickname;
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

    public static class RoleDto {
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
