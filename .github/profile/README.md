# 🏫 미래대학교 학사관리시스템

> 학생과 교수의 학사 업무를 하나의 웹 환경에서 처리할 수 있도록 구현한 학사관리시스템입니다.
> 사용자 인증과 권한 구분을 기반으로 수강신청, 강의, 성적, 출결, 휴·복학 등의 학사 기능을 제공합니다.

<!-- 추천: 이 위치에 대표 화면 또는 대시보드 스크린샷을 넣어주세요. -->
![미래대학교 학사관리시스템](/asstets/미래대학교_대표화면.png)
![미래대학교 학사관리시스템_대시보드](/asstets/미래대학교_대시보드.png)

## 프로젝트 개요

| 구분 | 내용 |
| --- | --- |
| 진행 기간 | 2026.06 ~ 2026.07 |
| 프로젝 형태 | 팀 프로젝 |
| 아키텍처 | Monolithic Architecture |
| Backend | Java 17, Spring Boot, Spring Security, MyBatis, MySQL |
| Frontend | Vue 3, Vite, Pinia, Vue Router, Axios |
| Infra | Docker, Nginx, Jenkins |

## 주요 기능

| 영역 | 주요 기능 |
| --- | --- |
| 인증·권한 | JWT 기반 로그인, 로그아웃, 토큰 재발급, 학생·교수 권한 구분 |
| 프로필 | 학생·교수 프로필 조회, 비밀번호 변경 |
| 수강관리 | 강의 조회, 수강신청·취소, 수강 내역 관리 |
| 강의·출결 | 강의 관리, 학생 출결 관리 |
| 성적 | 성적 조회 및 교수 성적 정정 |
| 학적 | 휴학·복학 신청 및 관리 |
| 대시보드 | 사용자 역할에 따른 학사 정보 제공 |

## 시스템 구성

```text
[ Vue 3 Client ]
        |
        | Axios / REST API
        v
[ Spring Boot Application ]
        |
        |-- Auth / User / Profile
        |-- Lecture / Enrollment
        |-- Grade / Attendance
        |-- Leave & Return / Dashboard
        v
[ MySQL Database ]
```

1차 프로젝트는 학사 도메인을 하나의 Spring Boot 애플리케이션 내에서 관리하는 구조로 구현했습니다. 도메인별 패키지를 분리해 각 영역의 책임을 구분했습니다.

## 기술 스택

### Backend

- **Java 17**
- **Spring Boot** — REST API 및 서버 애플리케이션 구현
- **Spring Security** — 인증·인가 처리
- **JJWT** — JWT 생성 및 검증
- **MyBatis** — 데이터 영속성 처리
- **MySQL** — 학사 데이터 저장
- **Bean Validation** — 요청 데이터 검증

### Frontend

- **Vue 3** — 사용자 인터페이스 구현
- **Vite** — 개발 및 빌드 환경
- **Pinia** — 전역 상태 관리
- **Vue Router** — 화면 라우팅
- **Axios** — 백엔드 API 통신
- **FullCalendar** — 일정 및 캘린더 UI
- **Day.js** — 날짜 데이터 처리
- **jwt-decode** — 클라이언트의 JWT 정보 확인

### Infra & Collaboration

- **Docker** — 애플리케이션 컨테이너화
- **Nginx** — 프론트엔드 정적 파일 서빙
- **Jenkins** — 빌드·배포 자동화
- **GitHub** — 소스 코드 및 협업 관리

## Repository

| Repository | Description |
| --- | --- |
| [msa4-lms](https://github.com/msa4-lms/msa4-lms) | Spring Boot 기반 백엔드 |
| [msa4-lms-client](https://github.com/msa4-lms/msa4-lms-client) | Vue 3 기반 프론트엔드 |

## 팀원 및 담당 역할

<!-- 팀원별 GitHub 계정과 실제 담당 역할로 교체해 주세요. -->

| 팀원  | 역할 | 담당 영역                                  |
|-----| --- |----------------------------------------|
| 도진희 | 팀장 | 인증·권한, 프로필, 공통 UI, 대시보드, 교수 성적 정정      |
| 김정태 | 팀원 | 교수 기능, 성적 관리                           |
| 조수혁 | 팀원 | 시간표, 성적 조회, 출결 관리                      |
| 홍대운 | 팀원 | 수강 신청, 수강 취소, 수강 관리, 휴/복학 신청, 교수 출결 확인 |

## 협업 방식

- 기능 단위 브랜치를 사용해 작업을 분리했습니다.
- Pull Request와 코드 리뷰를 통해 구현 내용과 로직을 공유했습니다.
- 공통 컴포넌트와 UI 기준을 마련해 화면의 일관성을 높였습니다.

## 관련 자료

<!-- 준비된 링크로 교체해 주세요. -->

- [프로젝트 문서](https://app.notion.com/p/MSA4-LMS-1-37dbf02975b380578d08d95862a13b71?source=copy_link)
- [발표자료](https://canva.link/vbbkpjihy0kn6w5)
- [시연 영상](https://drive.google.com/file/d/1XI8K8ggCF-swAAxELeolvRCE6yN7k4iD/view?usp=sharing)

---