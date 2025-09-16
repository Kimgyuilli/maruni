# MARUNI 기술 규격서

**프로젝트 전반의 기술 표준 및 개발 컨벤션 통합 가이드**

---

## 📝 기술 규격 문서 구조

### **📂 `specifications/` 폴더 (9개 문서)**

1. **[coding-standards.md](coding-standards.md)** ⭐⭐⭐ **(매일 참조)**
   - Java 코딩 컨벤션 및 네이밍 규칙
   - Entity/Service/Controller/DTO 표준 패턴
   - Import 순서 및 어노테이션 순서

2. **[architecture-guide.md](architecture-guide.md)** ⭐⭐ **(새 기능 개발)**
   - DDD 계층 구조 및 의존성 규칙
   - 도메인 간 상호작용 패턴
   - Package 구조 및 조직화 표준

3. **[api-design-guide.md](api-design-guide.md)** ⭐⭐ **(API 개발)**
   - REST API 설계 원칙
   - Controller 구현 표준
   - 응답 래핑 및 예외 처리

4. **[database-design-guide.md](database-design-guide.md)** ⭐ **(Entity 작업)**
   - Entity 설계 원칙 (BaseTimeEntity 상속)
   - JPA 관계 매핑 패턴
   - Repository 쿼리 작성법

5. **[testing-guide.md](testing-guide.md)** ⭐ **(테스트 작성)**
   - TDD Red-Green-Blue 사이클
   - 테스트 구조 및 명명 규칙
   - Mock 활용 패턴

6. **[security-guide.md](security-guide.md)** 🔒 **(보안 설정)**
   - JWT 인증 시스템 구현
   - Spring Security 설정
   - 데이터 암호화 및 보안 모범사례

7. **[performance-guide.md](performance-guide.md)** ⚡ **(성능 최적화)**
   - JPA N+1 쿼리 해결
   - 캐싱 전략
   - 데이터베이스 인덱스 설계

8. **[tech-stack.md](tech-stack.md)** 🛠️ **(기술 스택 정보)**
   - 전체 기술 스택 및 버전 정보
   - 환경 설정 및 Docker 구성
   - 개발 환경 설정

9. **[quick-reference.md](quick-reference.md)** 🚀 **(빠른 참조)**
   - 자주 사용하는 템플릿 모음
   - 체크리스트 및 어노테이션 모음
   - 트러블슈팅 가이드

---

## 🎯 문서 활용 가이드

### **일반적인 개발 흐름에 따른 문서 순서:**

```
1. 새 기능 개발 시작
   └─ architecture-guide.md (도메인 구조 설계)

2. Entity 설계
   └─ database-design-guide.md (엔티티 패턴)

3. Service/Controller 구현
   └─ coding-standards.md (코딩 컨벤션)
   └─ api-design-guide.md (API 설계)

4. 테스트 작성
   └─ testing-guide.md (TDD 사이클)

5. 보안/성능 검토
   └─ security-guide.md (보안 점검)
   └─ performance-guide.md (최적화)

6. 막혔을 때
   └─ quick-reference.md (템플릿/트러블슈팅)
```

### **자주 참조하는 문서 (우선순위)**

- ⭐⭐⭐ **`coding-standards.md`** - 매일 개발 시 참조
- ⭐⭐⭐ **`quick-reference.md`** - 빠른 템플릿 참조
- ⭐⭐ **`architecture-guide.md`** - 새 도메인 개발 시
- ⭐⭐ **`api-design-guide.md`** - Controller 작성 시
- ⭐ **`database-design-guide.md`** - Entity 설계 시
- ⭐ **`testing-guide.md`** - 테스트 작성 시

---

## 🔄 문서 업데이트 정책

- **실제 코드 변경 시**: 해당 문서도 함께 업데이트
- **새 패턴 발견 시**: 관련 문서에 추가
- **버전 업데이트**: 각 문서 하단의 Version 정보 갱신
- **문서 충돌 시**: 실제 코드를 기준으로 문서 수정

---

**📍 이 폴더의 9개 문서가 MARUNI 프로젝트의 모든 기술 표준을 정의합니다.**

**Version**: v2.0.0 | **Updated**: 2025-09-16