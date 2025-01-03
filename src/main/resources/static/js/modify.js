// 페이지 로드 시 사용자 정보 불러오기
document.addEventListener('DOMContentLoaded', () => {
    loadUserInfo();
    initializeModals();
    initializeEventListeners();
});

// 사용자 정보 불러오기 (GET /api/user/mypage)
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

            // 이메일, 이름, 출생연도
            document.getElementById('email').value = data.email || '';
            document.getElementById('name').value = data.name || '';
            document.getElementById('birth').value = data.birth || '';

            // 닉네임
            document.getElementById('nickname').value = data.nickname || '';

            // 장르 (예: 28 OR 12 OR 16)
            let genre = data.genre || '28 OR 12 OR 16';
            document.getElementById('genre').value = genre;
            document.getElementById('genreDisplay').value = convertGenreIdsToNames(genre);
        })
        .catch(err => console.error('Failed to load user info:', err));
}

// 프로필 이미지, 장르 모달 초기화
function initializeModals() {
    // 프로필 이미지 모달 열기
    document.getElementById('profilePreview').addEventListener('click', () => {
        const modal = new bootstrap.Modal(document.getElementById('profileModal'));
        modal.show();
    });

    // 프로필 이미지 옵션 생성
    const profileOptions = document.getElementById('profileOptions');
    for (let i = 0; i < 30; i++) {
        const img = document.createElement('img');
        img.src = `/profile/profile${i}.png`;
        img.alt = `Profile ${i}`;
        img.addEventListener('click', () => {
            document.getElementById('profilePreview').src = img.src;
            document.getElementById('profileImage').value = `profile${i}.png`;
            const modal = bootstrap.Modal.getInstance(document.getElementById('profileModal'));
            modal.hide();
        });
        profileOptions.appendChild(img);
    }

    // 장르 버튼 생성
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

    // 장르 모달 Confirm 버튼
    document.getElementById('confirmGenres').addEventListener('click', () => {
        if (selectedGenres.length === 0) {
            alert('최소 1개의 장르를 선택해야 합니다.');
            return;
        }
        // 보여줄 장르 이름
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

// 선택된 장르 목록
let selectedGenres = [28, 12, 16]; // 초기값

// 영화 장르 데이터
const genres = [
    { id: 28, name: '액션' }, { id: 12, name: '모험' }, { id: 16, name: '애니메이션' },
    { id: 35, name: '코미디' }, { id: 80, name: '범죄' }, { id: 99, name: '다큐멘터리' },
    { id: 18, name: '드라마' }, { id: 10751, name: '가족' }, { id: 14, name: '판타지' },
    { id: 36, name: '역사' }, { id: 27, name: '공포' }, { id: 10402, name: '음악' },
    { id: 9648, name: '미스터리' }, { id: 10749, name: '로맨스' }, { id: 878, name: 'SF' },
    { id: 10770, name: 'TV 영화' }, { id: 53, name: '스릴러' }, { id: 10752, name: '전쟁' },
    { id: 37, name: '서부' }
];

// 장르 버튼 토글
function toggleGenreSelection(genreId, btn) {
    if (selectedGenres.includes(genreId)) {
        selectedGenres = selectedGenres.filter(id => id !== genreId);
        btn.classList.remove('active');
    } else {
        if (selectedGenres.length >= 3) {
            alert('최대 3개의 장르만 선택할 수 있습니다.');
            return;
        }
        selectedGenres.push(genreId);
        btn.classList.add('active');
    }
}

// 장르 ID -> 이름 변환
function convertGenreIdsToNames(genreString) {
    if (!genreString) return '';
    const ids = genreString.split(' OR ').map(s => parseInt(s.trim()));
    const names = ids
        .map(id => genres.find(g => g.id === id)?.name)
        .filter(name => name);

    return names.join(', ');
}

// 닉네임 중복 확인
async function checkDuplicateNickname(nickname) {
    const res = await fetch(`/check-nickname?nickname=${encodeURIComponent(nickname)}`);
    if (res.ok) {
        return await res.json(); // 서버에서 boolean 반환
    }
    return false;
}

// 이벤트 리스너
function initializeEventListeners() {
    document.getElementById('saveBtn').addEventListener('click', async () => {
        const nickname = document.getElementById('nickname').value;
        // 닉네임 중복 체크
        if (await checkDuplicateNickname(nickname)) {
            document.getElementById('nicknameHelp').textContent = '이미 사용 중인 닉네임입니다.';
            return;
        } else {
            document.getElementById('nicknameHelp').textContent = '';
        }

        // body 데이터 생성
        const body = {
            profileImage: document.getElementById('profileImage').value,
            nickname,
            genre: document.getElementById('genre').value
        };

        // 수정 API 호출
        fetch('/api/user/modify', {
            method: 'POST',
            headers: {
                "Authorization": `Bearer ${localStorage.getItem('access_token')}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('프로필 수정 실패');
                }
                return response.text();
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
