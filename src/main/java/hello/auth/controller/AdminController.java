package hello.auth.controller;

import hello.auth.dto.SignupResponse;
import hello.auth.exception.AccessDeniedException;
import hello.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "관리자 API", description = "관리자 권한이 필요한 API")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "관리자 권한 부여",
            description = "특정 사용자에게 관리자 권한을 부여합니다. 이 API는 관리자 권한이 필요합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "관리자 권한 부여 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SignupResponse.class),
                            examples = @ExampleObject(
                                    name = "권한 부여 성공 예시",
                                    value = """
                                            {
                                              "username": "testuser",
                                              "nickname": "테스트유저",
                                              "roles": [
                                                {
                                                  "role": "Admin"
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 요청 (토큰 없음 또는 유효하지 않음)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "인증 실패",
                                    value = """
                                            {
                                              "error": {
                                                "code": "INVALID_TOKEN",
                                                "message": "유효하지 않은 인증 토큰입니다."
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 부족 (관리자 권한 필요)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "권한 부족",
                                    value = """
                                            {
                                              "error": {
                                                "code": "ACCESS_DENIED",
                                                "message": "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "사용자 없음",
                                    value = """
                                            {
                                              "error": {
                                                "code": "USER_NOT_FOUND",
                                                "message": "사용자를 찾을 수 없습니다."
                                              }
                                            }
                                            """
                            )
                    )
            )
    })
    @PatchMapping("/users/{userId}/roles")
    public ResponseEntity<SignupResponse> grantAdminRole(
            @Parameter(
                    description = "관리자 권한을 부여할 사용자 ID",
                    example = "2",
                    required = true
            )
            @PathVariable Long userId) {
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
