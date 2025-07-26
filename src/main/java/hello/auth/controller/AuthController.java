package hello.auth.controller;

import hello.auth.dto.LoginRequest;
import hello.auth.dto.LoginResponse;
import hello.auth.dto.SignupRequest;
import hello.auth.dto.SignupResponse;
import hello.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "인증 API", description = "사용자 회원가입 및 로그인 관련 API")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "회원가입",
            description = "새로운 사용자를 등록합니다. 사용자명은 중복될 수 없으며, 비밀번호는 최소 8자 이상이어야 합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SignupResponse.class),
                            examples = @ExampleObject(
                                    name = "회원가입 성공 예시",
                                    value = """
                                            {
                                              "username": "testuser",
                                              "nickname": "테스트유저",
                                              "roles": [
                                                {
                                                  "role": "USER"
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (유효성 검증 실패)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "유효성 검증 실패",
                                    value = """
                                            {
                                              "error": {
                                                "code": "VALIDATION_ERROR",
                                                "message": "비밀번호는 최소 8자 이상이어야 합니다."
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "이미 가입된 사용자",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "중복 사용자",
                                    value = """
                                            {
                                              "error": {
                                                "code": "USER_ALREADY_EXISTS",
                                                "message": "이미 가입된 사용자입니다."
                                              }
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(
            @Parameter(
                    description = "회원가입 요청 정보",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "회원가입 요청 예시",
                                    value = """
                                            {
                                              "username": "testuser",
                                              "password": "testpass123",
                                              "nickname": "테스트유저"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody SignupRequest request) {
        SignupResponse response = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "로그인",
            description = "사용자 인증을 통해 JWT 토큰을 발급받습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class),
                            examples = @ExampleObject(
                                    name = "로그인 성공 예시",
                                    value = """
                                            {
                                              "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0dXNlciIsInJvbGVzIjpbIlVTRVIiXSwiaWF0IjoxNjk5NTM2MDAwLCJleHAiOjE2OTk1NDMyMDB9.signature"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인 실패 (잘못된 자격 증명)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "로그인 실패",
                                    value = """
                                            {
                                              "error": {
                                                "code": "INVALID_CREDENTIALS",
                                                "message": "아이디 또는 비밀번호가 올바르지 않습니다."
                                              }
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Parameter(
                    description = "로그인 요청 정보",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "로그인 요청 예시",
                                    value = """
                                            {
                                              "username": "testuser",
                                              "password": "testpass123"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}
