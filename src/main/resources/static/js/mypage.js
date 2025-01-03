document.addEventListener('DOMContentLoaded', () => {
    if (!isAuthenticated()) {
        alert('로그인이 필요합니다.');
        window.location.href = '/login';
        return;
    }
    loadUserInfo();
    initializeEventListeners();
});

// ✅ 인증 확인
function isAuthenticated() {
    const accessToken = localStorage.getItem('access_token');
    return accessToken && validateToken(accessToken);
}

// ✅ 토큰 유효성 검사
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

// ✅ 사용자 정보 로드
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

            // 사용자 정보 설정
            document.getElementById('user-email').textContent = data.email || '-';
            document.getElementById('user-name').textContent = data.name || '-';
            document.getElementById('user-birth').textContent = data.birth || '-';
            document.getElementById('user-nickname').textContent = data.nickname || '-';

            // 장르 변환
            document.getElementById('user-genre').textContent = convertGenreIdsToNames(data.genre);
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

    // 장르 ID 문자열을 배열로 변환
    const genreIds = genreString.split(' OR ').map(id => parseInt(id.trim()));

    // 매칭되는 장르 이름 찾기
    const genreNames = genreIds
        .map(id => genres.find(genre => genre.id === id))
        .filter(genre => genre !== undefined)
        .map(genre => genre.name);

    return genreNames.length > 0 ? genreNames.join(', ') : '-';
}

// 로그아웃
function logout() {
    if (confirm('로그아웃 하시겠습니까?')) {
        fetch('/api/refresh-token', {
            method: 'DELETE',
            headers: {
                "Authorization": 'Bearer ' + localStorage.getItem('access_token'),
                "Content-Type": "application/json"
            }
        })
            .then(response => {
                if (response.ok) {
                    localStorage.removeItem('access_token');
                    alert('로그아웃 되었습니다.');
                    window.location.href = '/login';
                } else {
                    alert('로그아웃 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('서버와의 통신 중 문제가 발생했습니다.');
            });
    }
}

// ✅ 회원 탈퇴
function deleteUser() {
    if (confirm('정말로 회원 탈퇴를 진행하시겠습니까?')) {
        fetch('/user/delete', {
            method: 'POST',
            headers: {
                "Authorization": 'Bearer ' + localStorage.getItem('access_token'),
                "Content-Type": "application/json"
            }
        })
            .then(response => {
                if (response.ok) {
                    localStorage.removeItem('access_token');
                    alert('회원 탈퇴가 완료되었습니다.');
                    window.location.href = '/login';
                } else {
                    alert('회원 탈퇴에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('서버와의 통신 중 문제가 발생했습니다.');
            });
    }
}

// ✅ 이벤트 리스너 초기화
function initializeEventListeners() {
    document.getElementById('logout-btn').addEventListener('click', logout);
    document.getElementById('delete-user-btn').addEventListener('click', deleteUser);
}
