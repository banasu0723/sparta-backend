package hello.auth.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private Set<Role> roles = new HashSet<>();
    private LocalDateTime createdAt;

    public User() {
        this.createdAt = LocalDateTime.now();
    }

    public User(Long id, String username, String password, String nickname) {
        this();
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.roles.add(Role.USER); // 기본적으로 USER 역할 부여
    }

    public User(String username, String password, String nickname) {
        this();  // 기본 생성자 호출로 createdAt 설정
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.roles.add(Role.USER); // 기본적으로 USER 역할 부여
        // id는 null로 두어서 Repository에서 자동 생성되도록 함
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public boolean hasRole(Role role) {
        return this.roles.contains(role);
    }
}
