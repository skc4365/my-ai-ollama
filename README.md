# Spring AI + Ollama (qwen2.5) Review Analysis Server 🚀

이 프로젝트는 **Spring Boot 4.1.0, Spring AI, Ollama(qwen2.5:3b), PostgreSQL**을 연동하여 주문 정보 관리 및 고객 리뷰 감정 분석/요약을 수행하는 백엔드 서버 애플리케이션입니다.

---

## 🛠️ 1. 사전 요구사항 (Prerequisites)

프로젝트를 로컬 환경에서 실행하기 전 다음 사양들이 준비되어 있어야 합니다.

* **Java Version**: JDK 21 이상
* **Database**: PostgreSQL (포트: `5432`, DB명: `proj`, 사용자: `postgres`, 비밀번호: `1234`)
* **AI Engine**: Ollama (로컬 실행 필수)

### Ollama 모델 다운로드 및 실행
```bash
# Qwen 2.5 3B 한국어 모델 다운로드
ollama pull qwen2.5:3b

# Ollama 모델 로드 상태 확인
ollama list
```

---

## 🚀 2. 빌드 및 실행 방법 (Build & Run)

프로젝트 루트 디렉토리에서 아래 명령어로 애플리케이션을 구동합니다.

### Windows (PowerShell / CMD)
```powershell
.\gradlew.bat bootRun
```

### macOS / Linux
```bash
chmod +x gradlew
./gradlew bootRun
```

서버 구동 완료 후 기본적으로 **`http://localhost:8080`** 포트로 대기합니다.

---

## 🔍 3. API 테스트 방법 (API Testing)

### (1) Swagger UI 접속 (권장)
서버가 켜진 상태에서 브라우저로 아래 링크에 접속하면 API Spec 문서 확인 및 직접 테스트가 가능합니다.
👉 **[Swagger UI (http://localhost:8080/swagger)](http://localhost:8080/swagger)**

---

### (2) 단계별 테스트 시나리오

#### [Step 1] 테스트용 주문 데이터 등록
리뷰 작성을 위해 주문 내역을 데이터베이스에 먼저 등록합니다.
- **Method**: `POST`
- **URL**: `http://localhost:8080/orders`
- **Headers**: `Content-Type: application/json`
- **Request Body**:
  ```json
  {
    "customer": "홍길동",
    "product": "무선 마우스"
  }
  ```
- **결과**: 주문 데이터가 생성되며 고유 `id` (예: `1`)가 발급됩니다.

#### [Step 2] 고객 리뷰 등록 및 AI 감정 분석 수행
주문 번호(`orderId`)를 포함하여 후기 텍스트를 전송합니다. 서버는 백그라운드에서 Ollama AI와 통신하여 긍정/부정 판단 및 요약문을 자동으로 생성해 데이터베이스에 저장합니다.
- **Method**: `POST`
- **URL**: `http://localhost:8080/api/reviews`
- **Headers**: `Content-Type: application/json`
- **Request Body**:
  ```json
  {
    "orderId": 1,
    "content": "배송이 정말 빨랐고 제품 성능도 대만족입니다! 디자인도 예쁘네요."
  }
  ```
- **Response Example**:
  ```json
  {
    "reviewId": 1,
    "sentiment": "긍정",
    "summary": "빠른 배송, 훌륭한 제품 성능 및 만족스러운 디자인에 대해 긍정적으로 평가함"
  }
  ```

#### [Step 3] 등록된 리뷰 조회
- **전체 리뷰 및 분석 결과 목록 조회**
  - **Method**: `GET`
  - **URL**: `http://localhost:8080/api/reviews`
- **특정 리뷰 단건 조회**
  - **Method**: `GET`
  - **URL**: `http://localhost:8080/api/reviews/{id}`

---

## 💾 4. 데이터베이스 및 샘플 데이터 활용

- **자동 테이블 생성**: `application.yml` 파일의 `ddl-auto: update` 설정에 의해 실행 시 테이블이 자동 생성됩니다.
  - 생성 테이블: `qwen_order` (주문), `qwen_review` (리뷰), `qwen_review_analysis` (AI 분석)
- **샘플 데이터 스크립트**:
  - 로컬 데이터베이스 도구에서 [query.sql]을 실행하여 초기 가상 데이터를 한 번에 밀어 넣고 시퀀스를 안전하게 자동 재설정할 수 있습니다.

---

## 📂 5. 주요 소스 파일 경로 (Core Files)

* [build.gradle] - 프로젝트 라이브러리 의존관계 설정
* [application.yml] - DB 및 Ollama AI 연동 정보 설정
* [query.sql] - 테스트 데이터 초기화 및 시퀀스 정렬 쿼리
* [ReviewAiService.java] - Ollama 프롬프트 구성 및 ChatClient 연동
* [ReviewService.java] - AI 결과 파싱 및 DB 영속화 서비스
