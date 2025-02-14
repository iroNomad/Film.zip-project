# 🎬 Film.zip - Film Discovery Platform  

Film.zip is a **film discovery web application** that allows users to explore and search for movies. The platform provides essential details about films and helps users find interesting movies to watch.  

## 📌 Project Overview  

| Feature | Description |
|---------|------------|
| 🔍 **Movie Search** | Search for movies by title |
| 📋 **Movie Details** | View movie information (title, genre, rating, description, etc.) |
| 🎯 **Personalized Recommendations** | Suggest movies based on users' preferred genres (planned feature) |
| ❤️ **Watch List** | Save movies to a personal watch list |
| 🏆 **Trending & Popular Movies** | Discover trending and top-rated films |
| 💬 **Community Page** | A space where users can discuss movies, share reviews, and interact with others |

## 🛠 Tech Stack  

| Layer | Technology |
|-------|------------|
| **Backend** | Spring Boot, Spring Data JPA, Oracle DB |
| **Frontend** | HTML, CSS, JavaScript, Thymeleaf, Bootstrap |
| **API Integration** | TMDb API |
| **Build & Deployment** | Gradle, AWS EC2 / Render (planned) |

## 📂 Project Structure  

| Directory/File | Description |
|---------------|------------|
| `src/` | Source code |
| `src/main/` | Application code |
| `src/main/java/com/filmzip/` | Backend logic |
| `src/main/resources/` | Static resources (HTML, templates, config) |
| `src/test/` | Test cases |
| `build.gradle` | Gradle build script |
| `settings.gradle` | Gradle settings |
| `README.md` | Project documentation |
| `.gitignore` | Git ignore rules |

## UI Preview
![Main page](https://private-user-images.githubusercontent.com/89756508/413120483-7f30872d-0468-4eba-a3fa-d8a83558d157.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3Mzk0OTkwMDUsIm5iZiI6MTczOTQ5ODcwNSwicGF0aCI6Ii84OTc1NjUwOC80MTMxMjA0ODMtN2YzMDg3MmQtMDQ2OC00ZWJhLWEzZmEtZDhhODM1NThkMTU3LmdpZj9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTAyMTQlMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUwMjE0VDAyMDUwNVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPTcwMzNlNzZhZTEzY2U4NDRmMmZkOGIwNzQ4ZGYwZWJkNWM2OWQ0ZDUyOTZiOGQwMDIzNGNjODk2OWU4N2MyZTkmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.Mh95deiGnwGXEXFH09fRegKWA_LGR3s2Qpzw_MHhd3c)

---

![Main page](https://private-user-images.githubusercontent.com/89756508/413122772-48073cf4-11d0-4a1d-ac07-b23ddcfe1600.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3Mzk0OTkyNTEsIm5iZiI6MTczOTQ5ODk1MSwicGF0aCI6Ii84OTc1NjUwOC80MTMxMjI3NzItNDgwNzNjZjQtMTFkMC00YTFkLWFjMDctYjIzZGRjZmUxNjAwLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTAyMTQlMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUwMjE0VDAyMDkxMVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWU1ZTEyMjQwN2UxYmU3YmJiYzE3ZDk0MzU0Njc4ZDMwOWEzMjkzZGY0ZDg2ZmZhNDUxNWU1YjJjMWVmY2M4MjUmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.CWU7JwimsIDi-S8yfbzNcAu7vd9i6WNX_hw_Il5Zh_M)

---

![Search result page](https://private-user-images.githubusercontent.com/89756508/413126707-96c2418f-01aa-47d5-8528-5058407bb03b.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3Mzk0OTkyNTEsIm5iZiI6MTczOTQ5ODk1MSwicGF0aCI6Ii84OTc1NjUwOC80MTMxMjY3MDctOTZjMjQxOGYtMDFhYS00N2Q1LTg1MjgtNTA1ODQwN2JiMDNiLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTAyMTQlMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUwMjE0VDAyMDkxMVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWI0ZTkzY2JkNDQ4YjE0ZTkyNGI0NDNmYWNlYTc1ODczZTA1NGJmNWQ1MDhkNjdmYjMyYzUxYzE3MjY1NzcwNjUmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.W3IsaZUBuy4cr_m7Zx07g4Pi7ndIYXJc7lg8Fmsniwo)

---

![Movie details page](https://private-user-images.githubusercontent.com/89756508/413127315-40b8972a-858c-42ae-a07d-17738c3992cc.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3Mzk0OTkyNTEsIm5iZiI6MTczOTQ5ODk1MSwicGF0aCI6Ii84OTc1NjUwOC80MTMxMjczMTUtNDBiODk3MmEtODU4Yy00MmFlLWEwN2QtMTc3MzhjMzk5MmNjLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTAyMTQlMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUwMjE0VDAyMDkxMVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWIxNGEzNGIyYTQyZWU3M2ZiOTA1NGIzNjZmYmU5OGFkZTNhZTk1ZTAzOTgzYTlkZmYzOTNlNjQ3ODJjY2FkZWQmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.dUvN-_-Z6CM-rulbVBkB90PAiqZj8k4Mq4l_5tedLag)

---

![Movie trailer](https://private-user-images.githubusercontent.com/89756508/413127420-ba914d56-e14a-4236-86e8-a29c660ea1ea.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3Mzk0OTkyNTEsIm5iZiI6MTczOTQ5ODk1MSwicGF0aCI6Ii84OTc1NjUwOC80MTMxMjc0MjAtYmE5MTRkNTYtZTE0YS00MjM2LTg2ZTgtYTI5YzY2MGVhMWVhLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTAyMTQlMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUwMjE0VDAyMDkxMVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPTM0Yjc2YmYxMmVkNjE5NDM4ZDcxZDVlZmVkODdlN2YwNDk3MTVmYTdkMTViYTdlZWY4NGY3NzEwYmE4YmM4MGMmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.fIH2zZoZRVbARwZOD9neCwHYv_MGdCXJykUqdcFjyrk)
