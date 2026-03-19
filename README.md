# 🐶 댕댕트립 (DangdangTrip)

반려동물과 함께할 수 있는 여행지를 탐색하는 서비스입니다.
공공 반려동물 관광 API를 기반으로 **동반 가능 여부와 이용 조건**을 중심으로 장소 정보를 제공합니다.

---

## 📌 주요 기능

- **지역별 장소 탐색** — 전국 17개 시·도 선택 후 관광지 / 음식점 / 숙소 / 문화시설 카테고리별 조회
- **키워드 검색** — 지역명(서울, 제주) 또는 장소 이름(한옥마을, 공원) 으로 검색
- **장소 상세 페이지** — 이미지 갤러리, 소개, 운영시간, 주차 정보 제공
- **반려동물 동반 정보 시각화** — 목줄 필요 / 이동장 필요 / 자유이용 / 소·중·대형견 가능 여부를 배지로 표시

---

## 🛠 기술 스택

### Frontend
| 항목 | 버전 |
|------|------|
| React | 19 |
| TypeScript | 5.9 |
| Vite | 8 |
| React Router | 7 |
| Axios | 1.13 |

### Backend
| 항목 | 버전 |
|------|------|
| Spring Boot | 3.5.11 |
| Java | 17 |
| Spring WebFlux (WebClient) | - |

### 외부 API
- **한국관광공사 KorPetTourService2** — 반려동물 동반 가능 장소 목록 및 상세 정보

---

## 🗂 프로젝트 구조

```
dangdangtrip/
├── src/                        # Spring Boot 백엔드
│   └── main/java/com/dangdangtrip/
│       ├── controller/         # REST API 컨트롤러
│       ├── service/            # 비즈니스 로직
│       ├── external/tourism/   # 공공 API 클라이언트
│       ├── dto/                # 요청/응답 DTO
│       └── config/             # WebClient 설정
│
└── dangdangtrip-frontend/      # React 프론트엔드
    └── src/
        ├── pages/              # HomePage, DetailPage, SearchPage
        ├── components/         # Header, Hero, RegionList, PlaceCard
        └── api/                # Axios API 함수
```

---

## 🚀 실행 방법

### 백엔드

```bash
# application-secret.yml 설정 필요 (tourapi.service-key)
./gradlew bootRun --args='--spring.profiles.active=secret'
```

### 프론트엔드

```bash
cd dangdangtrip-frontend
npm install
npm run dev
```

프론트엔드는 `http://localhost:5173`, 백엔드는 `http://localhost:8080`에서 실행됩니다.
Vite 프록시 설정으로 `/api` 요청이 백엔드로 자동 전달됩니다.

---

## ⚙️ 환경 설정

`src/main/resources/application-secret.yml` 파일을 생성하고 아래 내용을 입력합니다.

```yaml
tourapi:
  service-key: "YOUR_SERVICE_KEY"
  base-url: "https://apis.data.go.kr/B551011/KorPetTourService2"
```

공공데이터포털(data.go.kr)에서 **KorPetTourService2** API 키를 발급받아 사용합니다.

---

## 📡 API 엔드포인트

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/places` | 장소 목록 조회 (지역 / 카테고리 / 키워드 필터) |
| GET | `/api/places/{contentId}` | 장소 상세 조회 |
| GET | `/api/areas` | 지역 코드 목록 조회 |
