# 🎬 Film.zip - 영화 탐색 플랫폼  

**Film.zip**은 사용자가 영화를 탐색하고 검색할 수 있는 **영화 탐색 웹 애플리케이션**입니다. 이 플랫폼은 영화의 주요 정보를 제공하며, 흥미로운 영화를 쉽게 찾을 수 있도록 도와줍니다.  

## 📌 프로젝트 개요  

| 기능 | 설명 |
|------|------|
| 🔍 **영화 검색** | 영화 제목으로 검색 가능 |
| 📋 **영화 상세 정보** | 제목, 장르, 평점, 설명 등 영화 정보 제공 |
| 🎯 **맞춤 추천** | 사용자의 선호 장르 기반 영화 추천 (추후 개발 예정) |
| ❤️ **감상 목록** | 관심 있는 영화를 개인 감상 목록에 저장 |
| 🏆 **트렌딩 & 인기 영화** | 현재 인기 있는 영화 및 높은 평점의 영화 탐색 |
| 💬 **커뮤니티 페이지** | 사용자들이 영화에 대해 토론하고 리뷰를 공유하는 공간 |

## 🛠 기술 스택  

| 계층 | 기술 |
|------|------|
| **백엔드** | Spring Boot, Spring Data JPA, Oracle DB |
| **프론트엔드** | HTML, CSS, JavaScript, Thymeleaf, Bootstrap |
| **API 연동** | TMDb API |
| **빌드 & 배포** | Gradle, AWS EC2 / Render (추후 계획) |

## 📂 프로젝트 구조  

| 디렉토리/파일 | 설명 |
|--------------|------|
| `src/` | 소스 코드 |
| `src/main/` | 애플리케이션 코드 |
| `src/main/java/com/filmzip/` | 백엔드 로직 |
| `src/main/resources/` | 정적 리소스 (HTML, 템플릿, 설정 파일) |
| `src/test/` | 테스트 코드 |
| `build.gradle` | Gradle 빌드 스크립트 |
| `settings.gradle` | Gradle 설정 파일 |
| `README.md` | 프로젝트 문서 |
| `.gitignore` | Git 제외 규칙 |

## UI 미리보기  

![Main page](https://private-user-images.githubusercontent.com/89756508/413120483-7f30872d-0468-4eba-a3fa-d8a83558d157.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3Mzk0OTkwMDUsIm5iZiI6MTczOTQ5ODcwNSwicGF0aCI6Ii84OTc1NjUwOC80MTMxMjA0ODMtN2YzMDg3MmQtMDQ2OC00ZWJhLWEzZmEtZDhhODM1NThkMTU3LmdpZj9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTAyMTQlMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUwMjE0VDAyMDUwNVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPTcwMzNlNzZhZTEzY2U4NDRmMmZkOGIwNzQ4ZGYwZWJkNWM2OWQ0ZDUyOTZiOGQwMDIzNGNjODk2OWU4N2MyZTkmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.Mh95deiGnwGXEXFH09fRegKWA_LGR3s2Qpzw_MHhd3c)

---

![Main page](https://private-user-images.githubusercontent.com/89756508/413122772-48073cf4-11d0-4a1d-ac07-b23ddcfe1600.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3Mzk0OTkyNTEsIm5iZiI6MTczOTQ5ODk1MSwicGF0aCI6Ii84OTc1NjUwOC80MTMxMjI3NzItNDgwNzNjZjQtMTFkMC00YTFkLWFjMDctYjIzZGRjZmUxNjAwLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTAyMTQlMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUwMjE0VDAyMDkxMVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWU1ZTEyMjQwN2UxYmU3YmJiYzE3ZDk0MzU0Njc4ZDMwOWEzMjkzZGY0ZDg2ZmZhNDUxNWU1YjJjMWVmY2M4MjUmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.CWU7JwimsIDi-S8yfbzNcAu7vd9i6WNX_hw_Il5Zh_M)

---

![Search result page](https://private-user-images.githubusercontent.com/89756508/413126707-96c2418f-01aa-47d5-8528-5058407bb03b.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3Mzk0OTkyNTEsIm5iZiI6MTczOTQ5ODk1MSwicGF0aCI6Ii84OTc1NjUwOC80MTMxMjY3MDctOTZjMjQxOGYtMDFhYS00N2Q1LTg1MjgtNTA1ODQwN2JiMDNiLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTAyMTQlMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUwMjE0VDAyMDkxMVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWI0ZTkzY2JkNDQ4YjE0ZTkyNGI0NDNmYWNlYTc1ODczZTA1NGJmNWQ1MDhkNjdmYjMyYzUxYzE3MjY1NzcwNjUmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.W3IsaZUBuy4cr_m7Zx07g4Pi7ndIYXJc7lg8Fmsniwo)

---

![Movie details page](https://private-user-images.githubusercontent.com/89756508/413127315-40b8972a-858c-42ae-a07d-17738c3992cc.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3Mzk0OTkyNTEsIm5iZiI6MTczOTQ5ODk1MSwicGF0aCI6Ii84OTc1NjUwOC80MTMxMjczMTUtNDBiODk3MmEtODU4Yy00MmFlLWEwN2QtMTc3MzhjMzk5MmNjLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTAyMTQlMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUwMjE0VDAyMDkxMVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWIxNGEzNGIyYTQyZWU3M2ZiOTA1NGIzNjZmYmU5OGFkZTNhZTk1ZTAzOTgzYTlkZmYzOTNlNjQ3ODJjY2FkZWQmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.dUvN-_-Z6CM-rulbVBkB90PAiqZj8k4Mq4l_5tedLag)

---

![Movie trailer](https://private-user-images.githubusercontent.com/89756508/413127420-ba914d56-e14a-4236-86e8-a29c660ea1ea.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3Mzk0OTkyNTEsIm5iZiI6MTczOTQ5ODk1MSwicGF0aCI6Ii84OTc1NjUwOC80MTMxMjc0MjAtYmE5MTRkNTYtZTE0YS00MjM2LTg2ZTgtYTI5YzY2MGVhMWVhLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTAyMTQlMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUwMjE0VDAyMDkxMVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPTM0Yjc2YmYxMmVkNjE5NDM4ZDcxZDVlZmVkODdlN2YwNDk3MTVmYTdkMTViYTdlZWY4NGY3NzEwYmE4YmM4MGMmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.fIH2zZoZRVbARwZOD9neCwHYv_MGdCXJykUqdcFjyrk)
