document.addEventListener('DOMContentLoaded', () => {
    if (!isAuthenticated()) {
        alert('로그인이 필요합니다.');
        window.location.href = '/login';
        return;
    }

    loadUserInfo();
    initializeProfileModal();
    initializeGenreModal();
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

let originalNickname = ''; // 전역 변수

// 사용자 정보 로드
function loadUserInfo() {
    fetch('/api/user/mypage', {
        method: 'GET',
        headers: {
            "Authorization": `Bearer ${localStorage.getItem('access_token')}`
        }
    })
        .then(res => res.json())
        .then(data => {
            // 프로필 이미지
            document.getElementById('profilePreview').src = data.profileImage || '/profile/profile0.png';
            document.getElementById('profileImage').value = data.profileImage || 'profile0.png';

            // 이메일, 이름, 출생 연도
            document.getElementById('email').value = data.email || '';
            document.getElementById('name').value = data.name || '';
            document.getElementById('birth').value = data.birth || '';

            // 닉네임
            document.getElementById('nickname').value = data.nickname || '';
            originalNickname = data.nickname || '';

            // 장르
            let genreString = data.genre || '28 OR 12 OR 16';
            document.getElementById('genre').value = genreString;
            document.getElementById('genreDisplay').value = convertGenreIdsToNames(genreString);

            // 이미 selectedGenres에 반영
            selectedGenres = parseGenreString(genreString);
            updateGenreButtons();
        })
        .catch(err => console.error('Failed to load user info:', err));
}

// 프로필 이미지 모달 초기화
function initializeProfileModal() {
    document.getElementById('profilePreview').addEventListener('click', () => {
        const modal = new bootstrap.Modal(document.getElementById('profileModal'));
        modal.show();
    });

    const profileOptions = document.getElementById('profileOptions');
    for (let i = 0; i < 30; i++) {
        const img = document.createElement('img');
        img.src = `/profile/profile${i}.png`;
        img.alt = `Profile ${i}`;
        img.dataset.value = `profile${i}.png`;
        img.addEventListener('click', () => {
            document.getElementById('profilePreview').src = img.src;
            document.getElementById('profileImage').value = `profile${i}.png`;

            const modal = bootstrap.Modal.getInstance(document.getElementById('profileModal'));
            modal.hide();
        });
        profileOptions.appendChild(img);
    }
}

// 장르 모달 초기화
function initializeGenreModal() {
    const genreOptions = document.getElementById('genreOptions');
    genres.forEach(g => {
        const btn = document.createElement('button');
        btn.className = 'btn btn-outline-primary m-1 genre-btn';
        btn.textContent = g.name;
        btn.dataset.id = g.id;

        btn.addEventListener('click', () => {
            toggleGenreSelection(parseInt(g.id), btn);
        });

        genreOptions.appendChild(btn);
    });

    document.getElementById('confirmGenres').addEventListener('click', () => {
        if (selectedGenres.length === 0) {
            alert('최소 1개의 장르를 선택해야 합니다.');
            return;
        }
        // 장르 이름
        const selectedGenreNames = selectedGenres
            .map(id => genres.find(g => g.id === id)?.name)
            .join(', ');

        // DB 저장용
        const genreString = selectedGenres.join(' OR ');

        document.getElementById('genre').value = genreString;
        document.getElementById('genreDisplay').value = selectedGenreNames;

        const modal = bootstrap.Modal.getInstance(document.getElementById('genreModal'));
        modal.hide();
    });
}

// 버튼 이벤트 리스너
function initializeEventListeners() {
    document.getElementById('saveBtn').addEventListener('click', async () => {
        // 닉네임 중복 확인
        const nickname = document.getElementById('nickname').value;
        if (nickname !== originalNickname) {
            if (await checkDuplicateNickname(nickname)) {
                document.getElementById('nicknameHelp').textContent = '이미 사용 중인 닉네임입니다.';
                return;
            } else {
                document.getElementById('nicknameHelp').textContent = '';
            }
        }

        // body 생성
        const body = {
            profileImage: document.getElementById('profileImage').value,
            nickname: nickname,
            genre: document.getElementById('genre').value
        };

        fetch('/api/user/modify', {
            method: 'POST',
            headers: {
                "Authorization": `Bearer ${localStorage.getItem('access_token')}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        })
            .then(res => {
                if (!res.ok) throw new Error('프로필 수정 실패');
                return res.text();
            })
            .then(msg => {
                alert(msg);
                window.location.href = '/user/mypage';
            })
            .catch(err => {
                alert('수정에 실패했습니다.');
                console.error('Error:', err);
            });
    });
}

// ======================= 헬퍼 함수들 =======================

// 닉네임 중복 확인
async function checkDuplicateNickname(nickname) {
    const res = await fetch(`/check-nickname?nickname=${encodeURIComponent(nickname)}`);
    if (res.ok) return await res.json();
    return false;
}

// 장르 데이터
const genres = [
    { id: 28, name: '액션' }, { id: 12, name: '모험' }, { id: 16, name: '애니메이션' },
    { id: 35, name: '코미디' }, { id: 80, name: '범죄' }, { id: 99, name: '다큐멘터리' },
    { id: 18, name: '드라마' }, { id: 10751, name: '가족' }, { id: 14, name: '판타지' },
    { id: 36, name: '역사' }, { id: 27, name: '공포' }, { id: 10402, name: '음악' },
    { id: 9648, name: '미스터리' }, { id: 10749, name: '로맨스' }, { id: 878, name: 'SF' },
    { id: 10770, name: 'TV 영화' }, { id: 53, name: '스릴러' }, { id: 10752, name: '전쟁' },
    { id: 37, name: '서부' }
];

let selectedGenres = []; // 선택된 장르 ID 배열

// 문자열 "28 OR 12 OR 16" -> [28, 12, 16]
function parseGenreString(genreString) {
    if (!genreString) return [];
    return genreString.split(' OR ').map(s => parseInt(s.trim()));
}

// [28,12,16] -> "액션, 모험, 애니메이션"
function convertGenreIdsToNames(genreString) {
    if (!genreString) return '';
    const ids = parseGenreString(genreString);
    const names = ids.map(id => genres.find(g => g.id === id)?.name).filter(Boolean);
    return names.join(', ');
}

// 장르 버튼 토글
function toggleGenreSelection(genreId, btn) {
    if (selectedGenres.includes(genreId)) {
        // 이미 선택 -> 해제
        selectedGenres = selectedGenres.filter(id => id !== genreId);
        btn.classList.remove('active');
    } else {
        // 새로 선택
        if (selectedGenres.length >= 3) {
            alert('최대 3개의 장르만 선택할 수 있습니다.');
            return;
        }
        selectedGenres.push(genreId);
        btn.classList.add('active');
    }
}

// 페이지 로드 시 이미 선택된 장르 버튼도 표시
function updateGenreButtons() {
    const buttons = document.querySelectorAll('#genreOptions .genre-btn');
    buttons.forEach(btn => {
        const id = parseInt(btn.dataset.id);
        if (selectedGenres.includes(id)) {
            btn.classList.add('active');
        } else {
            btn.classList.remove('active');
        }
    });
}