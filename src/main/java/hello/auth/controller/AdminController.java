package hello.auth.controller;

import hello.auth.dto.SignupResponse;
import hello.auth.exception.AccessDeniedException;
import hello.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/users/{userId}/roles")
    public ResponseEntity<SignupResponse> grantAdminRole(@PathVariable Long userId) {
        System.out.println("=== AdminController 진입 ===");
        System.out.println("userId: " + userId);

        // 현재 인증된 사용자가 ADMIN 권한이 있는지 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("현재 인증 정보: " + authentication);

        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_Admin"))) {
            throw new AccessDeniedException("관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다.");
        }

        SignupResponse response = userService.grantAdminRole(userId);
        return ResponseEntity.ok(response);
    }
}
