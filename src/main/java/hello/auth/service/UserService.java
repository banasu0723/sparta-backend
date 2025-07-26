package hello.auth.service;

import hello.auth.dto.LoginRequest;
import hello.auth.dto.LoginResponse;
import hello.auth.dto.SignupRequest;
import hello.auth.dto.SignupResponse;
import hello.auth.entity.Role;
import hello.auth.entity.User;
import hello.auth.exception.InvalidCredentialsException;
import hello.auth.exception.UserAlreadyExistsException;
import hello.auth.exception.UserNotFoundException;
import hello.auth.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("이미 가입된 사용자입니다.");
        }

        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname()
        );

        User savedUser = userRepository.save(user);
        return new SignupResponse(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        String token = jwtService.generateToken(user);
        return new LoginResponse(token);
    }

    public SignupResponse grantAdminRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        // 기존 역할을 모두 제거하고 ADMIN만 설정
        user.getRoles().clear();
        user.addRole(Role.ADMIN);

        User updatedUser = userRepository.save(user);
        return new SignupResponse(updatedUser);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    @PostConstruct
    public void createInitialAdmin() {
        if (!userRepository.existsByUsername("admin")) {
            User adminUser = new User(
                    "admin",
                    passwordEncoder.encode("admin1234"),
                    "Administrator"
            );
            adminUser.getRoles().clear();
            adminUser.addRole(Role.ADMIN);
            userRepository.save(adminUser);

            System.out.println("초기 관리자 계정 생성됨: admin/admin1234");
        }
    }

}
