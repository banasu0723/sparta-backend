# Sparta Backend - JWT 인증/인가 시스템

Spring Boot를 기반으로 한 JWT 인증 및 권한 관리 시스템입니다. 회원가입, 로그인, 관리자 권한 부여 기능을 제공합니다.


## 🎯 프로젝트 개요

이 프로젝트는 JWT(JSON Web Token)를 활용한 인증 및 인가 시스템을 구현한 Spring Boot 애플리케이션입니다.
메모리 기반 데이터 저장소를 사용하여 사용자 정보를 관리하며, Swagger UI를 통한 API 문서화를 제공합니다.

## 🛠 기술 스택

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security 6.5.2**
- **JWT (jjwt 0.11.5)**
- **Gradle 8.x**

### Documentation
- **Springdoc OpenAPI 3 (Swagger UI)**

### Testing
- **JUnit 5**
- **Mockito**
- **Spring Boot Test**

### Deployment
- **AWS EC2 (Ubuntu 22.04)**
- **Nginx (리버스 프록시)**

## ✨ 주요 기능

### 1. 사용자 인증
- **회원가입**: 사용자명, 비밀번호, 닉네임으로 계정 생성
- **로그인**: JWT Access Token 발급
- **비밀번호 암호화**: BCrypt 사용

### 2. JWT 토큰 관리
- **토큰 발급**: 로그인 시 2시간 유효한 JWT 토큰 생성
- **토큰 검증**: 모든 보호된 API 요청에서 토큰 유효성 검사
- **권한 정보**: 토큰에 사용자 ID, 사용자명, 역할 정보 포함

### 3. 권한 기반 접근 제어
- **일반 사용자**: 기본 USER 권한
- **관리자**: ADMIN 권한으로 다른 사용자에게 관리자 권한 부여 가능

### 4. API 문서화
- **Swagger UI**: 모든 API 엔드포인트 문서화
- **Interactive Testing**: 브라우저에서 직접 API 테스트 가능

## 실행 확인

애플리케이션이 정상적으로 실행되면 다음 URL에서 확인할 수 있습니다:

- **로컬**: http://localhost:8080/swagger-ui/index.html
- **배포 서버**:
    - **Swagger UI**: http://13.124.189.237:8080/swagger-ui/index.html
    - **API Base URL**: http://13.124.189.237:8080


### 테스트 구조

- **단위 테스트**: JwtService, UserService 테스트
- **통합 테스트**: Controller 레이어 테스트
- **보안 테스트**: JWT 인증/인가 플로우 테스트

### 테스트 케이스

#### AuthControllerTest
- 회원가입 성공/실패 테스트
- 로그인 성공/실패 테스트
- 유효성 검증 테스트

#### AdminControllerTest
- 관리자 권한 부여 성공 테스트
- 권한 부족 테스트
- 토큰 검증 테스트

#### JwtServiceTest
- JWT 토큰 생성/검증 테스트
- 토큰 만료 테스트
- 클레임 추출 테스트

## 테스트 커버리지

<img width="1452" height="654" alt="image" src="https://github.com/user-attachments/assets/800c9654-7b88-4cc0-a9da-7f21a5a14193" />

