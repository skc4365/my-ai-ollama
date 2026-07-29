# Spring AI + Ollama (qwen2.5) 리뷰 분석 시스템 시작 가이드 🚀

이 프로젝트는 **Spring Boot, Spring AI, Ollama(Qwen 2.5:3b 모델), PostgreSQL**을 사용하여 주문 정보를 등록하고, 고객의 후기(리뷰)를 AI가 자동으로 분석(긍정/부정 감정 분류 및 핵심 내용 요약)하여 데이터베이스에 저장하는 간단한 웹 애플리케이션입니다.

처음 사용하는 사용자가 로컬 환경에서 프로젝트를 빌드하고 테스트해볼 수 있도록 단계별 가이드를 제공합니다.

---

## 🛠️ 1. 사전 요구사항 및 환경 설정 (Prerequisites)

이 프로젝트를 실행하려면 다음 개발 환경이 로컬에 세팅되어 있어야 합니다.

### (1) build.gradle 의존관계 확인
프로젝트의 [build.gradle] 파일에 필요한 주요 의존성이 잘 구성되어 있는지 먼저 체크합니다.
* **Spring Boot**: `org.springframework.boot` (버전 `4.1.0`)
* **Spring AI BOM & Ollama Starter**: `spring-ai-starter-model-ollama`
* **PostgreSQL**: `org.postgresql:postgresql` 및 `spring-boot-starter-data-jpa`
* **Swagger/OpenAPI**: `springdoc-openapi-starter-webmvc-ui`

### (2) application.yml 환경설정 파일 확인
[application.yml] 파일에서 데이터베이스 및 AI 엔진 연결 정보를 확인하고 본인의 로컬 환경에 맞추어 수정합니다.
* **데이터베이스 연결 주소 및 계정**:
  ```yaml
  datasource:
    url: jdbc:postgresql://localhost:5432/proj
    username: postgres
    password: 1234
  ```
* **Ollama AI 서버 주소 및 모델**:
  ```yaml
  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        model: qwen2.5:3b
  ```

### (3) JDK 21 설치
프로젝트는 **Java 21** 기반으로 작성되었습니다. 자바 버전 확인이 필요합니다.
```bash
java -version
```

### (4) Ollama (Local AI Engine) 설정
로컬 환경에서 AI 모델을 구동하기 위한 Ollama 설치 및 설정 단계입니다.

1. **Ollama 설치**: [Ollama 공식 홈페이지](https://ollama.com/)에서 운영체제에 맞는 버전을 설치합니다.
2. **Qwen 2.5 모델 다운로드**: 터미널(또는 CMD/PowerShell)을 열고 아래 명령어를 입력하여 3B 크기의 Qwen2.5 한국어 특화 모델을 다운로드합니다.
   ```bash
   ollama pull qwen2.5:3b
   ```
3. **다운로드 확인**: 다운로드가 완료되었는지 확인합니다.
   ```bash
   ollama list
   ```
   > **출력 예시:**
   > ```text
   > NAME           ID             SIZE      MODIFIED
   > qwen2.5:3b     a012345...   1.9 GB    ...
   > ```
4. **Ollama 구동**: Ollama 엔진이 백그라운드에서 구동되고 있어야 합니다. (기본 포트: `11434`)

### (5) PostgreSQL 설정
리뷰 및 분석 데이터를 저장하기 위해 PostgreSQL 데이터베이스가 필요합니다.
- **호스트**: `localhost` (포트: `5432`)
- **데이터베이스명**: `proj`
- **사용자명(Username)**: `postgres`
- **비밀번호(Password)**: `1234`

> [!IMPORTANT]
> * 애플리케이션을 실행하기 전에 PostgreSQL에 **`proj`**라는 이름의 데이터베이스를 미리 생성해 두어야 합니다.
> * 포트, 계정 정보가 로컬 환경과 다를 경우 [application.yml] 파일을 수정해야 합니다.

---

## 🚀 2. 프로젝트 실행 방법 (How to Run)

### 애플리케이션 실행
프로젝트 루트 디렉토리(`my-ai-ollama`)에서 아래 Gradle 명령어를 통해 서버를 구동합니다.

#### Windows (PowerShell 또는 Command Prompt)
```powershell
.\gradlew.bat bootRun
```

#### macOS / Linux
```bash
chmod +x gradlew
./gradlew bootRun
```

정상적으로 기동되었다면 기본 포트인 **`8080`**번 포트로 서버가 실행됩니다.

---

## 🔍 3. API 기능 테스트 (How to Test)

### (1) Swagger UI 접속 (추천)
가장 간편한 테스트 방법은 웹 브라우저에서 아래 주소로 접속하는 것입니다. API 스펙 확인과 동시에 직접 입력을 통한 테스트가 가능합니다.
👉 **[Swagger UI 링크](http://localhost:8080/swagger)** *(서버 구동 후 브라우저에 입력)*

---

### (2) 테스트 시나리오 수행 순서

AI 분석 흐름을 검증하기 위한 시나리오 순서입니다.

#### [Step 1] 테스트용 주문 등록 (POST)
후기를 등록하기 전에, 해당 후기에 매핑될 주문 데이터를 먼저 등록합니다.
- **URL**: `POST http://localhost:8080/orders`
- **Request Body (JSON)**:
  ```json
  {
    "customer": "홍길동",
    "product": "무선 마우스"
  }
  ```
- **Response**: 생성된 주문 데이터가 반환되며, `id` 값을 기억합니다. (예: `1`)

#### [Step 2] 고객 후기 등록 및 AI 분석 요청 (POST)
위에서 생성한 주문 번호(`orderId`)를 포함하여 후기 내용을 등록합니다. 서버는 자동으로 Ollama AI 모델을 호출하여 분석 결과를 도출합니다.
- **URL**: `POST http://localhost:8080/api/reviews`
- **Request Body (JSON)**:
  ```json
  {
    "orderId": 1,
    "content": "배송은 정말 빨랐고 제품 성능도 대만족입니다! 디자인도 예쁘네요."
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

#### [Step 3] 등록된 후기 확인 (GET)
- **전체 후기 조회**
  - **URL**: `GET http://localhost:8080/api/reviews`
- **특정 후기 상세 조회**
  - **URL**: `GET http://localhost:8080/api/reviews/{id}` 
              (예: `http://localhost:8080/api/reviews/1`)

---

## 💾 4. 데이터베이스 및 샘플 데이터 활용

애플리케이션 구동 시 `ddl-auto: update` 설정에 따라 자동으로 테이블(`qwen_order`, `qwen_review`, `qwen_review_analysis`)이 생성됩니다.

### 샘플 SQL 실행
로컬 DB 클라이언트가 있다면, 프로젝트 루트에 위치한 [query.sql](file:///c:/st202605/test_springAI/my-ai-ollama/query.sql) 스크립트를 사용하여 대량의 테스트용 초기 데이터 및 인공지능 분석 가상 데이터를 삽입해 볼 수 있습니다.

---

## 📂 5. 핵심 소스 코드 탐색 (Core Codebase)

프로젝트 분석 및 학습을 위한 소스 코드 경로입니다. 각 링크를 클릭하여 바로 코드를 열어보실 수 있습니다.

* **Configuration**:
  * [application.yml] - PostgreSQL DB 및 Ollama AI 모델 매핑 환경설정
  * [AiConfig.java]   - Spring AI의 `ChatClient` 빈 등록 설정
* **Controllers**:
  * [OrderController.java]  - 주문 API (`/orders`)
  * [ReviewController.java] - 리뷰 API (`/api/reviews`)
* **Services**:
  * [ReviewAiService.java]  - Ollama 프롬프트 구성 및 ChatClient 연동 코어가 내장된 클래스
  * [ReviewService.java]  - 리뷰 영속화 및 AI JSON 분석 결과를 파싱하여 JPA에 저장하는 컨트롤러와 AI의 브릿지 서비스

---

> [!TIP]
> **첫 실행 시 주의 사항**
> Ollama AI 모델(`qwen2.5:3b`)이 로컬 메모리에 처음 로드될 때는 첫 API 요청 시 수 초에서 수십 초 정도의 딜레이가 발생할 수 있습니다. 모델 로딩이 완료된 두 번째 요청부터는 정상 속도로 응답합니다.
