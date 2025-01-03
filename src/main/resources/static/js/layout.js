// 페이지 접근 제어
document.addEventListener('DOMContentLoaded', function () {
    if (!isAuthenticated()) {
        // 인증되지 않은 경우 로그인 페이지로 이동
        alert('로그인이 필요합니다.');
        window.location.href = '/login';
    }

    httpRequest('GET','/main', null, null, null);
});

// 사용자 인증 상태 확인 함수
function isAuthenticated() {
    // 로컬 스토리지에서 액세스 토큰 확인
    const accessToken = localStorage.getItem('access_token');
    const refreshToken = getCookie('refresh_token');

    // 액세스 토큰 또는 리프레시 토큰이 없으면 인증되지 않은 상태로 간주
    if (!accessToken || !refreshToken) {
        return false;
    }

    // 액세스 토큰 유효성을 확인하기 위해 API 호출 (옵션)
    return validateToken(accessToken);
}

// 액세스 토큰 유효성 확인 함수 (선택적 구현)
function validateToken(token) {
    // 간단한 유효성 체크 구현 (옵션)
    try {
        const payload = JSON.parse(atob(token.split('.')[1])); // JWT 디코드
        const currentTime = Math.floor(Date.now() / 1000);
        return payload.exp > currentTime; // 토큰 만료 시간 체크
    } catch (e) {
        return false; // 토큰이 올바르지 않으면 false 반환
    }
}

// 로그아웃 기능
const logoutButton = document.getElementById('logout-btn');

if (logoutButton) {
    logoutButton.addEventListener('click', event => {
        function success() {
            // 로컬 스토리지에 저장된 액세스 토큰을 삭제
            localStorage.removeItem('access_token');

            // 쿠키에 저장된 리프레시 토큰을 삭제
            deleteCookie('refresh_token');
            location.replace('/login');
        }
        function fail() {
            alert('로그아웃 실패했습니다.');
        }

        httpRequest('DELETE','/api/refresh-token', null, success, fail);
    });
}



// 쿠키를 가져오는 함수
function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(';');
    cookie.some(function (item) {
        item = item.replace(' ', '');

        var dic = item.split('=');

        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });

    return result;
}

// 쿠키를 삭제하는 함수
function deleteCookie(name) {
    document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
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
                location.reload();
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
                    location.reload();
                } else {
                    alert('삭제에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('서버와의 통신 중 문제가 발생했습니다.');
            });
}

// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: { // 로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json',
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return success();
        }
        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: getCookie('refresh_token'),
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then(result => { // 재발급이 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch(error => fail());
        } else {
            return fail();
        }
    });
}
