/*****************************************************************
 * 1) 문서 로드 후 (DOMContentLoaded) 실행 로직
 *****************************************************************/
document.addEventListener('DOMContentLoaded', function () {
    console.log("[DEBUG] DOMContentLoaded fired.");

    // 1-1) 인증 검사
    if (!isAuthenticated()) {
        alert('로그인이 필요합니다.');
        window.location.href = '/login';
        return;
    }

    // 1-2) 인증된 상태면 /api/main 호출
    fetch('/api/main', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json',
        }
    })
        .then(response => {
            console.log("[DEBUG] /api/main status =", response.status);
            console.log(response);
            if (!response.ok) {
                console.log('Token expired or not authorized');
                return null; // then(data => ...)로 넘어갈 때 data=null
            }
            console.log(response);
            return response.json(); // { trendingList: [...], recommendedList: [...], watchList: [...] }
        })
        .then(data => {
            console.log("data:", data);
            if (!data) return; // null이면 렌더 스킵

            // 콘솔에서 실제 데이터 구조 확인
            console.log("[DEBUG] /api/main data:", data);

            // 1-3) DOM 렌더
            renderTrendingList(data.trendingList);
            renderRecommendedList(data.recommendedList);
            renderWatchList(data.watchList);
        })
        .catch(error => {
            console.error('Error fetching /api/main:', error);
        });
});


/*****************************************************************
 * 2) 인증 / 토큰 관련 함수
 *****************************************************************/
function isAuthenticated() {
    const accessToken = localStorage.getItem('access_token');
    const refreshToken = getCookie('refresh_token');
    if (!accessToken || !refreshToken) {
        return false;
    }
    return validateToken(accessToken);
}

function validateToken(token) {
    try {
        const payload = JSON.parse(atob(token.split('.')[1])); // JWT 디코드
        const currentTime = Math.floor(Date.now() / 1000);
        return payload.exp > currentTime; // 토큰 만료 시간 체크
    } catch (e) {
        return false; // 토큰 파싱 실패
    }
}

// 로그아웃 기능 (버튼이 있다면)
const logoutButton = document.getElementById('logout-btn');
if (logoutButton) {
    logoutButton.addEventListener('click', event => {
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
    });
}


/*****************************************************************
 * 3) Render Functions
 *****************************************************************/

/** 인기 영화 (trendingList) 캐러셀 렌더링 */
function renderTrendingList(trendingList) {
    console.log("[DEBUG] renderTrendingList:", trendingList);

    const indicators = document.getElementById('carousel-indicators');
    const inner      = document.getElementById('carousel-inner');
    if (!indicators || !inner) {
        console.warn("Cannot find carousel elements.");
        return;
    }

    indicators.innerHTML = '';
    inner.innerHTML = '';

    trendingList.forEach((movie, idx) => {
        // 인디케이터
        const indicatorBtn = document.createElement('button');
        indicatorBtn.type = 'button';
        indicatorBtn.setAttribute('data-bs-slide-to', idx);
        indicatorBtn.setAttribute('data-bs-target', '#carouselAutoplaying');
        indicatorBtn.setAttribute('aria-label', `Slide ${idx+1}`);
        if (idx === 0) indicatorBtn.classList.add('active');
        indicators.appendChild(indicatorBtn);

        // carousel-item
        const carouselItem = document.createElement('div');
        carouselItem.classList.add('carousel-item');
        if (idx === 0) carouselItem.classList.add('active');
        carouselItem.setAttribute('data-bs-interval', '3000');

        // 링크 + 이미지
        const detailLink = document.createElement('a');
        detailLink.href = `/detail/${movie.movieID}`;
        const img = document.createElement('img');
        img.classList.add('card-img-top');
        img.alt = 'Movie Poster';
        img.src = `https://image.tmdb.org/t/p/original${movie.backdrop_path}`;
        detailLink.appendChild(img);

        carouselItem.appendChild(detailLink);

        // 별점
        const ratingBadge = document.createElement('div');
        ratingBadge.className = 'position-absolute top-0 end-0 p-3';
        ratingBadge.innerHTML = `
          <span class="badge text-white fs-3" style="background-color: rgba(0, 0, 0, 0.6);">
            ⭐${movie.rating}
          </span>`;
        carouselItem.appendChild(ratingBadge);

        // 제목
        const caption = document.createElement('div');
        caption.className = 'carousel-caption d-none d-md-block';
        const captionLink = document.createElement('a');
        captionLink.href = `/detail/${movie.movieID}`;
        captionLink.style.textDecoration = 'none';
        captionLink.innerHTML = `
          <h3 style="background-color: rgba(0, 0, 0, 0.6); color: #fff; padding: 10px;">
            ${movie.title}
          </h3>
        `;
        caption.appendChild(captionLink);

        carouselItem.appendChild(caption);

        inner.appendChild(carouselItem);
    });
}

/** 추천 리스트 */
function renderRecommendedList(recommendedList) {
    console.log("[DEBUG] renderRecommendedList:", recommendedList);

    const container = document.getElementById('recommended-list');
    if (!container) {
        console.warn("Cannot find recommended-list element.");
        return;
    }
    container.innerHTML = '';

    recommendedList.forEach(movie => {
        const card = document.createElement('div');
        card.className = 'card mb-3 bg-transparent text-white rounded-0 border-0';
        card.style.cssText = 'flex: 0 0 auto; width: 300px; height: auto;';

        // 링크 + 이미지
        const link = document.createElement('a');
        link.href = `/detail/${movie.movieID}`;
        const img = document.createElement('img');
        // 만약 movie.backdrop_path가 이미 full url이면 그대로 사용
        img.src = movie.backdrop_path;
        img.alt = 'Movie Poster';
        img.className = 'card-img-top rounded-0';
        img.style.objectFit = 'cover';

        link.appendChild(img);
        card.appendChild(link);

        // 제목
        const cardBody = document.createElement('div');
        cardBody.className = 'card-body p-1';
        cardBody.style.height = '30px';
        cardBody.style.overflow = 'hidden';

        const titleP = document.createElement('p');
        titleP.className = 'text-center';
        titleP.textContent = movie.title;

        cardBody.appendChild(titleP);
        card.appendChild(cardBody);

        container.appendChild(card);
    });
}

/** 관심 리스트 */
function renderWatchList(watchList) {
    console.log("[DEBUG] renderWatchList:", watchList);

    const container = document.getElementById('watch-list');
    if (!container) {
        console.warn("Cannot find watch-list element.");
        return;
    }
    container.innerHTML = '';

    watchList.forEach(movie => {
        const card = document.createElement('div');
        card.className = 'card mb-3 bg-transparent text-white rounded-0 border-0';
        card.style.cssText = 'flex: 0 0 auto; width: 300px; height: auto;';

        // 링크 + 이미지
        const link = document.createElement('a');
        link.href = `/detail/${movie.movieApiId}`;

        const img = document.createElement('img');
        img.src = movie.backdropPath;
        img.alt = 'Movie Poster';
        img.className = 'card-img-top rounded-0';
        img.style.objectFit = 'cover';

        link.appendChild(img);
        card.appendChild(link);

        // 제목
        const cardBody = document.createElement('div');
        cardBody.className = 'card-body p-1';
        cardBody.style.height = '30px';
        cardBody.style.overflow = 'hidden';

        const titleP = document.createElement('p');
        titleP.className = 'text-center';
        titleP.textContent = movie.title;

        cardBody.appendChild(titleP);
        card.appendChild(cardBody);

        container.appendChild(card);
    });
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
    document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
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
                return success && success();
            }
            const refresh_token = getCookie('refresh_token');
            if (response.status === 401 && refresh_token) {
                // 재발급 시도
                fetch('/api/token', {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('access_token'),
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        refreshToken: refresh_token
                    })
                })
                    .then(res => {
                        if (!res.ok) throw new Error('Token refresh failed.');
                        return res.json();
                    })
                    .then(result => {
                        // 새 액세스 토큰 교체
                        localStorage.setItem('access_token', result.accessToken);
                        // 재시도
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

function addToWatchList() {

    const movieApiId = parseInt(document.getElementById('movieID').textContent, 10);
    const title = document.getElementById('title').textContent;
    const backdropPath = document.getElementById('backdropPath').textContent;

    const url = "/api/watchList";

    console.log(movieApiId, title, backdropPath);

    fetch(url, {
        method: "POST",
        headers: {
            "Authorization": 'Bearer ' + localStorage.getItem('access_token'),
            "Content-Type": "application/json",
        },
        body: JSON.stringify(
            {
                movieApiId,
                title,
                backdropPath
            }
        )
    })
        .then(response => {

            if (response.ok) {
                alert('내 리스트에 추가되었습니다.');
                location.replace('/main');
            }
            else {
                alert('등록에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('서버와의 통신 중 문제가 발생했습니다.');
        });
}

function deleteFromWatchList() {
    const movieApiId = document.getElementById('movieID').textContent;

    const url = "/api/watchList/" + movieApiId;
    console.log("Deleting movie with ID: " + movieApiId);

    fetch(url, {
        method: "DELETE",
        headers: {
            "Authorization": 'Bearer ' + localStorage.getItem('access_token'),
            "Content-Type": "application/json",
        },
    })
        .then(response => {
            if (response.ok) {
                alert('내 관심 리스트에서 삭제되었습니다.');
                location.replace('/main');
            } else {
                alert('삭제에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('서버와의 통신 중 문제가 발생했습니다.');
        });
}