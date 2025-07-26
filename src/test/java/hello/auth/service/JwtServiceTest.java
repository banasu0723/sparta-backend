package hello.auth.service;

import hello.auth.entity.Role;
import hello.auth.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "jwt.secret=testSecretKeyForJWTTokenGenerationAndValidationThatIsLongEnoughFor64Characters",
        "jwt.expiration=7200000"
})
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "testuser", "encodedPassword", "테스트유저");
        testUser.addRole(Role.USER);
    }

    @Test
    @DisplayName("JWT 토큰 생성 성공")
    void generateToken_Success() {
        // When
        String token = jwtService.generateToken(testUser);

        // Then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3); // Header.Payload.Signature
    }

    @Test
    @DisplayName("JWT 토큰 검증 성공 - 유효한 토큰")
    void validateToken_Success() {
        // Given
        String token = jwtService.generateToken(testUser);

        // When
        boolean isValid = jwtService.validateToken(token);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("JWT 토큰 검증 실패 - 잘못된 토큰")
    void validateToken_Fail_InvalidToken() {
        // Given
        String invalidToken = "invalid.token.format";

        // When
        boolean isValid = jwtService.validateToken(invalidToken);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("토큰에서 사용자 ID 추출 성공")
    void getUserIdFromToken_Success() {
        // Given
        String token = jwtService.generateToken(testUser);

        // When
        Long userId = jwtService.getUserIdFromToken(token);

        // Then
        assertThat(userId).isEqualTo(1L);
    }

    @Test
    @DisplayName("토큰에서 사용자명 추출 성공")
    void getUsernameFromToken_Success() {
        // Given
        String token = jwtService.generateToken(testUser);

        // When
        String username = jwtService.getUsernameFromToken(token);

        // Then
        assertThat(username).isEqualTo(testUser.getUsername());
    }

    @Test
    @DisplayName("토큰에서 역할 정보 추출 성공")
    void getRolesFromToken_Success() {
        // Given
        User adminUser = new User(2L, "admin", "encodedPassword", "관리자");
        adminUser.getRoles().clear();
        adminUser.addRole(Role.ADMIN);

        String token = jwtService.generateToken(adminUser);

        // When
        List<String> roles = jwtService.getRolesFromToken(token);

        // Then
        assertThat(roles).hasSize(1);
        assertThat(roles).contains("Admin");
    }

    @Test
    @DisplayName("토큰 만료 여부 확인 - 유효한 토큰")
    void isTokenExpired_False_ValidToken() {
        // Given
        String token = jwtService.generateToken(testUser);

        // When
        boolean isExpired = jwtService.isTokenExpired(token);

        // Then
        assertThat(isExpired).isFalse();
    }
}