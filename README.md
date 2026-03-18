# 💬 고독한채팅방 : 실시간 채팅 서비스

### 🔍 Overview

- Java, Spring Boot 기반 실시간 채팅 및 알림 시스템 개발
- WebSocket, Kafka 기반 메시지 처리 구조 구현
- QueryDSL 기반 무한 스크롤로 대량 메시지 조회 처리
- Amazon S3 이미지 업로드 및 Spring Security 기반 인증 구현

### 🛠️ Tech Stack

- Backend: Java, Spring Boot (JPA, QueryDSL, Security)
- Messaging: Kafka, WebSocket
- Database: H2
- Infra & Storage: Amazon S3

### 📐 Architecture

![Godokbang Architecture.png](readme_assets/Godokbang%20Architecture.png)

### 👩‍💻 Key Implementations

- Kafka + WebSocket 기반 실시간 채팅 및 알림 처리 구현
- 채팅 메시지 / 알림 토픽 분리를 통한 메시지 처리 구조 개선
- QueryDSL 기반 No-offset 페이징으로 대량 메시지 조회 처리
- Spring Security 기반 세션 인증 및 필터 커스터마이징
- Amazon S3 기반 이미지 업로드 및 파일 관리

### 🧠 Problem Solving

1. **Kafka 토픽 분리를 통한 메시지 처리 구조 개선**
    - 채팅 메시지와 알림을 단일 토픽에서 처리 시 로직 복잡도 증가
    - 채팅 / 알림 토픽 분리 및 Consumer 역할 분리
    - 메시지 처리 안정성 및 확장성 확보
2. **No-offset 페이징을 통한 조회 성능 개선**
    - offset 기반 페이징의 성능 저하 발생
    - QueryDSL 기반 커서 페이징 적용
    - 대량 데이터에서도 일관된 성능 유지
3. **S3 기반 이미지 업로드 구조 구현**
    - 로컬 스토리지 사용 시 확장성 및 보안 문제 발생
    - S3를 외부 스토리지로 도입
    - 파일 관리 안정성 및 접근성 개선
