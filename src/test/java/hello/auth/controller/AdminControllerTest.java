package hello.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.auth.config.TestConfig;
import hello.auth.dto.SignupRequest;
import hello.auth.dto.LoginRequest;
import hello.auth.entity.Role;
import hello.auth.entity.User;
import hello.auth.repository.UserRepository;
import hello.auth.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@Import(TestConfig.class)
class AdminControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        objectMapper = new ObjectMapper();
        userRepository.clear();

        // 관리자 계정 생성
        User admin = new User("admin", passwordEncoder.encode("admin1234"), "Administrator");
        admin.getRoles().clear();
        admin.addRole(Role.ADMIN);
        userRepository.save(admin);
    }

    @Test
    @DisplayName("관리자 권한 부여 성공 - 관리자가 요청")
    void grantAdminRole_Success() throws Exception {
        // Given - 일반 사용자 생성
        User normalUser = new User("normaluser", passwordEncoder.encode("password123"), "일반유저");
        User savedUser = userRepository.save(normalUser);

        // 관리자 토큰 생성
        User admin = userRepository.findByUsername("admin").get();
        String adminToken = jwtService.generateToken(admin);

        // When & Then
        mockMvc.perform(patch("/admin/users/{userId}/roles", savedUser.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("normaluser"))
                .andExpect(jsonPath("$.nickname").value("일반유저"))
                .andExpect(jsonPath("$.roles[0].role").value("Admin"));
    }

    @Test
    @DisplayName("관리자 권한 부여 실패 - 일반 사용자가 요청")
    void grantAdminRole_Fail_AccessDenied() throws Exception {
        // Given - 일반 사용자들 생성
        User normalUser1 = new User("user1", passwordEncoder.encode("password123"), "유저1");
        User savedUser1 = userRepository.save(normalUser1);

        User normalUser2 = new User("user2", passwordEncoder.encode("password123"), "유저2");
        User savedUser2 = userRepository.save(normalUser2);

        // 일반 사용자 토큰 생성
        String userToken = jwtService.generateToken(savedUser1);

        // When & Then
        mockMvc.perform(patch("/admin/users/{userId}/roles", savedUser2.getId())
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error.code").value("ACCESS_DENIED"))
                .andExpect(jsonPath("$.error.message").value("관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."));
    }



    @Test
    @DisplayName("관리자 권한 부여 실패 - 토큰 없이 요청")
    void grantAdminRole_Fail_NoToken() throws Exception {
        // Given - 일반 사용자 생성
        User normalUser = new User("normaluser", passwordEncoder.encode("password123"), "일반유저");
        User savedUser = userRepository.save(normalUser);

        // When & Then - Authorization 헤더 없이 요청
        mockMvc.perform(patch("/admin/users/{userId}/roles", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value("INVALID_TOKEN"))
                .andExpect(jsonPath("$.error.message").value("유효하지 않은 인증 토큰입니다."));
    }

    @Test
    @DisplayName("관리자 권한 부여 실패 - 존재하지 않는 사용자")
    void grantAdminRole_Fail_UserNotFound() throws Exception {
        // Given
        User admin = userRepository.findByUsername("admin").get();
        String adminToken = jwtService.generateToken(admin);

        Long nonExistentUserId = 999L;

        // When & Then
        mockMvc.perform(patch("/admin/users/{userId}/roles", nonExistentUserId)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.code").value("USER_NOT_FOUND"))
                .andExpect(jsonPath("$.error.message").value(containsString("사용자를 찾을 수 없습니다")));

    }

    @Test
    @DisplayName("관리자 권한 부여 실패 - 만료된 토큰")
    void grantAdminRole_Fail_ExpiredToken() throws Exception {
        // Given - 일반 사용자 생성
        User normalUser = new User("normaluser", passwordEncoder.encode("password123"), "일반유저");
        User savedUser = userRepository.save(normalUser);

        // 만료된 토큰 생성 (임의로 만료된 토큰 문자열 사용)
        String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJhZG1pbiIsInJvbGVzIjpbIkFkbWluIl0sImlhdCI6MTYwMDAwMDAwMCwiZXhwIjoxNjAwMDA3MjAwfQ.invalidSignature";

        // When & Then
        mockMvc.perform(patch("/admin/users/{userId}/roles", savedUser.getId())
                        .header("Authorization", "Bearer " + expiredToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value("INVALID_TOKEN"))
                .andExpect(jsonPath("$.error.message").value("유효하지 않은 인증 토큰입니다."));
    }

    @Test
    @DisplayName("관리자 권한 부여 실패 - 잘못된 형식의 토큰")
    void grantAdminRole_Fail_InvalidTokenFormat() throws Exception {
        // Given - 일반 사용자 생성
        User normalUser = new User("normaluser", passwordEncoder.encode("password123"), "일반유저");
        User savedUser = userRepository.save(normalUser);

        // When & Then - 잘못된 형식의 토큰
        mockMvc.perform(patch("/admin/users/{userId}/roles", savedUser.getId())
                        .header("Authorization", "Bearer invalid.token.format")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value("INVALID_TOKEN"))
                .andExpect(jsonPath("$.error.message").value("유효하지 않은 인증 토큰입니다."));
    }
}
