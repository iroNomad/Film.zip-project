/**
 * 김진원
 */
document.addEventListener('DOMContentLoaded', () => {
    if (!isAuthenticated()) {
        alert('로그인이 필요합니다.');
        window.location.href = '/login';
        return;
    }
    loadUserInfo();
    initializeEventListeners();
});

// 인증 확인
function isAuthenticated() {
    const accessToken = localStorage.getItem('access_token');
    return accessToken && validateToken(accessToken);
}

// 토큰 유효성 검사
function validateToken(token) {
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const currentTime = Math.floor(Date.now() / 1000);
        return payload.exp > currentTime;
    } catch (e) {
        console.error('토큰 검증 실패:', e);
        return false;
    }
}

// 사용자 정보 로드
function loadUserInfo() {
    fetch('/api/user/mypage', {
        method: 'GET',
        headers: {
            "Authorization": 'Bearer ' + localStorage.getItem('access_token'),
            "Content-Type": "application/json"
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('사용자 정보를 불러오는데 실패했습니다.');
            }
            return response.json();
        })
        .then(data => {
            // 프로필 이미지 설정
            document.querySelector('.profile-img').src = data.profileImage || '/profile/default.png';

            // 사용자 정보 설정 (★ textContent => value 로 변경 ★)
            document.getElementById('user-email').value = data.email || '-';
            document.getElementById('user-name').value = data.name || '-';
            document.getElementById('user-birth').value = data.birth || '-';
            document.getElementById('user-nickname').value = data.nickname || '-';

            // 장르 변환
            document.getElementById('user-genre').value = convertGenreIdsToNames(data.genre);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('사용자 정보를 불러오는데 실패했습니다.');
        });
}

// 장르 ID를 이름으로 변환
function convertGenreIdsToNames(genreString) {
    const genres = [
        { id: 28, name: '액션' }, { id: 12, name: '모험' }, { id: 16, name: '애니메이션' },
        { id: 35, name: '코미디' }, { id: 80, name: '범죄' }, { id: 99, name: '다큐멘터리' },
        { id: 18, name: '드라마' }, { id: 10751, name: '가족' }, { id: 14, name: '판타지' },
        { id: 36, name: '역사' }, { id: 27, name: '공포' }, { id: 10402, name: '음악' },
        { id: 9648, name: '미스터리' }, { id: 10749, name: '로맨스' }, { id: 878, name: 'SF' },
        { id: 10770, name: 'TV 영화' }, { id: 53, name: '스릴러' }, { id: 10752, name: '전쟁' },
        { id: 37, name: '서부' }
    ];

    if (!genreString) return '-';

    // 장르 ID 문자열을 배열로 변환 (예: "28 OR 12" -> [28,12])
    const genreIds = genreString.split(' OR ').map(id => parseInt(id.trim()));

    // 매칭되는 장르 이름 찾기
    const genreNames = genreIds
        .map(id => genres.find(genre => genre.id === id))
        .filter(Boolean)
        .map(genre => genre.name);

    return genreNames.length > 0 ? genreNames.join(', ') : '-';
}

// 회원 탈퇴 (JS fetch)
function deleteUser() {
    if (!confirm('정말로 회원 탈퇴를 진행하시겠습니까?')) return;

    fetch('/api/user/delete', {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('회원 탈퇴 요청 실패');
            }
            return response.text(); // 예: "회원 탈퇴가 완료되었습니다."
        })
        .then(msg => {
            alert(msg);

            // 회원 탈퇴 후, refresh_token 삭제 로직 (서버 호출)
            httpRequest('DELETE','/api/refresh-token', null,
                () => {
                    // 로컬 스토리지 / 쿠키 정리
                    localStorage.removeItem('access_token');
                    deleteCookie('refresh_token');
                    window.location.href = '/login';
                },
                () => {
                    // refresh-token 삭제 실패 시에도 일단 로컬 토큰은 삭제
                    localStorage.removeItem('access_token');
                    deleteCookie('refresh_token');
                    alert('회원 탈퇴가 완료되었지만, refresh_token 삭제는 실패했습니다.');
                    window.location.href = '/login';
                }
            );
        })
        .catch(error => {
            console.error(error);
            alert('회원 탈퇴에 실패했습니다.');
        });
}

// 이벤트 리스너 초기화
function initializeEventListeners() {
    // 로그아웃 버튼
    document.getElementById('logout-btn2').addEventListener('click', logout);

    // 회원 탈퇴 버튼
    const deleteButton = document.getElementById('delete-user-btn');
    if (deleteButton) {
        deleteButton.addEventListener('click', deleteUser);
    }
}

// 로그아웃 기능
function logout() {
    if (!confirm('로그아웃 하시겠습니까?')) return;

    function success() {
        // 로컬 스토리지 토큰 삭제
        localStorage.removeItem('access_token');
        // 쿠키 리프레시 토큰 삭제
        deleteCookie('refresh_token');
        location.replace('/login');
    }
    function fail() {
        alert('로그아웃 실패');
    }
    httpRequest('DELETE','/api/refresh-token', null, success, fail);
}

/*****************************************************************
 * 4) 공용 함수들 (httpRequest, getCookie, deleteCookie 등)
 *****************************************************************/

/** 쿠키 가져오기 */
function getCookie(key) {
    let result = null;
    const cookieArr = document.cookie.split(';');
    cookieArr.some(item => {
        item = item.trim();
        const dic = item.split('=');
        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });
    return result;
}

/** 쿠키 삭제 */
function deleteCookie(name) {
    document.cookie = name + '=; path=/; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

/** HTTP 요청(재발급 로직 포함) */
function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json'
        },
        body: body
    })
        .then(response => {
            if (response.status === 200 || response.status === 201) {
                success && success();
                return;
            }
            const refresh_token = getCookie('refresh_token');
            if (response.status === 401 && refresh_token) {
                // 토큰 재발급
                fetch('/api/token', {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('access_token'),
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ refreshToken: refresh_token })
                })
                    .then(res => {
                        if (!res.ok) throw new Error('Token refresh failed.');
                        return res.json();
                    })
                    .then(result => {
                        // 새 access_token 교체 후 재시도
                        localStorage.setItem('access_token', result.accessToken);
                        httpRequest(method, url, body, success, fail);
                    })
                    .catch(error => {
                        console.error(error);
                        fail && fail();
                    });
            } else {
                fail && fail();
            }
        })
        .catch(err => {
            console.error(err);
            fail && fail();
        });
}
