// 공통 fetch 요청 처리 함수
function sendRequest(url, method, data, successMessage, failureMessage, redirectUrl) {
    fetch(url, {
        method: method,
        headers: {
            "Authorization": 'Bearer ' + localStorage.getItem('access_token'),
            "Content-Type": "application/json",
        },
        body: data ? JSON.stringify(data) : null,
    })
        .then(response => {
            if (response.ok) {
                alert(successMessage);
                if (redirectUrl) location.replace(redirectUrl);
            } else {
                alert(failureMessage);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('서버와의 통신 중 문제가 발생했습니다.');
        });
}

function deleteArticle() {
    // HTML에서 게시글 ID 가져오기
    const articleId = document.getElementById('article-id')?.value;

    // articleId가 없을 경우 처리
    if (!articleId) {
        console.error("Article ID is undefined or not found!");
        alert('게시글 정보를 찾을 수 없습니다.');
        return;
    }

    console.log("Deleting article with ID:", articleId); // 디버깅 로그

    if (confirm('정말 삭제하시겠습니까?')) {
        fetch(`/api/community/${articleId}`, {
            method: 'DELETE',
            headers: {
                "Authorization": 'Bearer ' + localStorage.getItem('access_token'),
                "Content-Type": "application/json"
            }
        })
            .then(response => {
                if (response.ok) {
                    alert('삭제가 완료되었습니다.');
                    location.replace('/community/list'); // 성공 시 목록으로 이동
                } else {
                    alert('삭제에 실패했습니다.');
                    console.error('Failed to delete article:', response.status);
                }
            })
            .catch(error => {
                console.error('Error during deletion:', error);
                alert('서버와의 통신 중 문제가 발생했습니다.');
            });
    }
}

// 게시글 생성 및 수정
function saveArticle() {
    const id = document.getElementById('article-id')?.value; // 수정 시 ID
    const title = document.getElementById('title').value; // 제목 입력값
    const content = document.getElementById('content').value; // 내용 입력값

    if (!title || !content) {
        alert('제목과 내용을 모두 입력해주세요.');
        return;
    }

    const isEdit = !!id; // 수정 여부를 ID 존재 여부로 판단
    const url = isEdit ? `/api/community/${id}` : '/api/community';
    const method = isEdit ? 'PUT' : 'POST';

    fetch(url, {
        method: method,
        headers: {
            "Authorization": 'Bearer ' + localStorage.getItem('access_token'),
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ title, content }),
    })
        .then(response => {
            if (response.ok) {
                alert(isEdit ? '수정이 완료되었습니다.' : '등록이 완료되었습니다.');
                location.replace('/community/list');
            } else {
                alert(isEdit ? '수정에 실패했습니다.' : '등록에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('서버와의 통신 중 문제가 발생했습니다.');
        });
}

// 게시글 추천/비추천
function reactToPost(postId, reactionType) {
    fetch(`/api/community/${postId}/react?reactionType=${reactionType}`, {
        method: 'POST',
        headers: {
            "Authorization": 'Bearer ' + localStorage.getItem('access_token'),
            "Content-Type": "application/json",
        }
    })
        .then(response => {
            if (response.ok) {
                alert(`${reactionType === 'LIKE' ? '추천' : '비추천'}이 완료되었습니다.`);
                location.reload();
            } else {
                alert(`${reactionType === 'LIKE' ? '추천' : '비추천'}에 실패했습니다.`);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('서버와의 통신 중 문제가 발생했습니다.');
        });
}

// 버튼 이벤트 리스너 추가
function initializeEventListeners() {
    const deleteButton = document.getElementById('delete-btn');
    if (deleteButton) {
        deleteButton.addEventListener('click', () => {
            const id = document.getElementById('article-id').value;
            deleteArticle(id);
        });
    }

    const modifyButton = document.getElementById('modify-btn');
    if (modifyButton) {
        modifyButton.addEventListener('click', () => {
            const id = document.getElementById('article-id').value;
            saveArticle(id);
        });
    }

    const createButton = document.getElementById('create-btn');
    if (createButton) {
        createButton.addEventListener('click', saveArticle);
    }
}