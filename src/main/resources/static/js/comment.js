// 좋아요 기능
function likeComment(commentId) {
    fetch(`/comments/like/${commentId}`, {
        method: 'POST',
    })

        .then(response => response.json())
        .then(data => {
            if (data.message) {
                alert(data.message);
                location.reload();
            } else {
                alert('좋아요 추가에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('좋아요 추가 중 문제가 발생했습니다.');
        });

}

// 댓글 수정
function editComment(btn, commentId, content) {
    const sanitizedContent = content.replace(/"/g, '');
    const contentSpan = document.getElementById(`commentContent_${commentId}`);
    if (!contentSpan) {
        alert('수정할 댓글을 찾을 수 없습니다.');
        return;
    }

    // 입력 필드 생성 및 기존 텍스트 설정
    const inputField = document.createElement('input');
    inputField.type = 'text';
    inputField.value = sanitizedContent;
    inputField.id = `editInput_${commentId}`;
    inputField.className = 'form-control';
    inputField.style.display = 'inline-block';

    // 기존 댓글 숨기고 입력 필드 추가
    contentSpan.style.display = 'none';
    contentSpan.parentNode.insertBefore(inputField, contentSpan);

    // 저장 및 취소 버튼 추가
    const saveButton = document.createElement('button');
    saveButton.textContent = '저장';
    saveButton.className = 'btn btn-primary btn-sm';
    saveButton.onclick = () => saveComment(commentId);

    const cancelButton = document.createElement('button');
    cancelButton.textContent = '취소';
    cancelButton.className = 'btn btn-secondary btn-sm';
    cancelButton.style.marginLeft = '5px';
    cancelButton.onclick = () => cancelEdit(commentId, content);

    contentSpan.parentNode.appendChild(saveButton);
    contentSpan.parentNode.appendChild(cancelButton);

    btn.disabled = true;
}

// 댓글 저장
function saveComment(commentId) {
    const inputField = document.getElementById(`editInput_${commentId}`);
    const newContent = inputField.value;

    if (!newContent.trim()) {
        alert('내용을 입력하세요.');
        return;
    }

    fetch(`/api/comments/edit/${commentId}`, {
        method: 'PUT',
        headers: {
            "Authorization": 'Bearer ' + localStorage.getItem('access_token'),
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ content: newContent }),
    })

        .then(response => {
            if (response.ok) {
                alert('댓글이 수정되었습니다.');
                const contentSpan = document.getElementById(`commentContent_${commentId}`);
                contentSpan.textContent = newContent; // 업데이트된 내용 반영
                cancelEdit(commentId); // 편집 모드 종료
                location.reload(); // 페이지 새로고침
            } else {
                return response.text().then((errorMessage) => {
                    alert(`댓글 수정 실패: ${errorMessage}`);
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('댓글 수정 중 문제가 발생했습니다.');
        });

}

// 편집 취소
function cancelEdit(commentId, originalContent) {
    const inputField = document.getElementById(`editInput_${commentId}`);
    const saveButton = inputField.nextSibling; // 저장 버튼
    const cancelButton = saveButton.nextSibling; // 취소 버튼
    const contentSpan = document.getElementById(`commentContent_${commentId}`);

    // 기존 요소 복원
    if (contentSpan) {
        contentSpan.style.display = 'inline';
        contentSpan.textContent = originalContent || contentSpan.textContent; // 원래 내용을 복원
    }
    inputField.remove();
    saveButton.remove();
    cancelButton.remove();
    location.reload(); // 페이지 새로고침
}

// 댓글 삭제
function deleteComment(commentId) {
    if (confirm("정말 이 댓글을 삭제하시겠습니까?")) {
        fetch(`/api/comments/delete/${commentId}`, {
            method: 'DELETE',
            headers: {
                "Authorization": 'Bearer ' + localStorage.getItem('access_token'),
                "Content-Type": "application/json",
            },
        })
            .then(response => {
                if (response.ok) {
                    alert('댓글이 삭제되었습니다.');
                    location.reload(); // 페이지 새로고침
                } else {
                    return response.text().then((errorMessage) => {
                        alert(`댓글 삭제 실패: ${errorMessage}`);
                    });
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('댓글 삭제 중 문제가 발생했습니다.');
            });

    }
}

// 대댓글 폼 토글
function toggleReplyForm(commentId) {
    const form = document.getElementById(`replyForm_${commentId}`);
    // 이미 폼이 보이면 숨기고, 보이지 않으면 보이게 한다
    if (form.style.display === 'none' || form.style.display === '') {
        form.style.display = 'block';
    } else {
        form.style.display = 'none';
    }
}

// 댓글 추처언
function recommendComment(commentId) {
    // 서버와 통신하는 AJAX 요청 (예제 코드)
    fetch(`/api/comments/reaction`, {
        method: 'POST',
        headers: {
            "Authorization": 'Bearer ' + localStorage.getItem('access_token'),
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            commentId: commentId,
            reaction: 'RECOMMEND',
        }),
    })
    .then(response => response.json())
    .then(responseData => {
        if (responseData.status === "OK") {
            const recommendCountElem = document.getElementById(`recommendCount_${commentId}`);
            const notRecommendCountElem = document.getElementById(`notRecommendCount_${commentId}`);

            let recommendCount = parseInt(recommendCountElem.innerText.replace('👍 ', '')) || 0;
            let notRecommendCount = parseInt(notRecommendCountElem.innerText.replace('👎 ', '')) || 0;

            // 추천 증가, 비추천 감소
            if (notRecommendCount > 0) {
                notRecommendCount--;
                notRecommendCountElem.innerText = ` ${notRecommendCount}`;
            }
            recommendCount++;
            recommendCountElem.innerText = ` ${recommendCount}`;
        } else if (responseData.status === "NOT_OK") {
            alert("이미 추천을 하셨습니다.");
        } else {
            alert("알 수 없는 응답이 발생했습니다.");
        }
    }).catch(error => console.error('Error:', error));

}

// 댓글 비이추우처언
function notRecommendComment(commentId) {
    // 서버와 통신하는 AJAX 요청 (예제 코드)
    fetch(`/api/comments/reaction`, {
        method: 'POST',
        headers: {
            "Authorization": 'Bearer ' + localStorage.getItem('access_token'),
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            commentId: commentId,
            reaction: 'NOTRECOMMEND',
        }),
    })
    .then(response => response.json())
    .then(responseData => {
        if (responseData.status === "OK") {
            const recommendCountElem = document.getElementById(`recommendCount_${commentId}`);
            const notRecommendCountElem = document.getElementById(`notRecommendCount_${commentId}`);

            let recommendCount = parseInt(recommendCountElem.innerText.replace('👍 ', '')) || 0;
            let notRecommendCount = parseInt(notRecommendCountElem.innerText.replace('👎 ', '')) || 0;

            // 비추천 증가, 추천 감소
            if (recommendCount > 0) {
                recommendCount--;
                recommendCountElem.innerText = ` ${recommendCount}`;
            }
            notRecommendCount++;
            notRecommendCountElem.innerText = ` ${notRecommendCount}`;
        } else if (responseData.status === "NOT_OK") {
            alert("이미 비추천을 하셨습니다.");
        } else {
            alert("알 수 없는 응답이 발생했습니다.");
        }
    }).catch(error => console.error('Error:', error));
}

function fn_saveAnswer(articleId) {
    const content = document.getElementById('commentContent').value; // 텍스트 박스 내용 가져오기

    if (!content.trim()) {
        alert('댓글 내용을 입력하세요.');
        return;
    }

    fetch(`/api/comments/create/${articleId}`, {
        method: 'POST',
        headers: {
            "Authorization": 'Bearer ' + localStorage.getItem('access_token'),
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ content: content }), // JSON 형식으로 전송
    })
        .then(response => {
            if (response.ok) {
                alert('댓글이 성공적으로 등록되었습니다.');
                window.location.reload(); // 페이지 새로고침
            } else {
                alert('댓글 등록에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류가 발생했습니다.');
        });
}

function fn_saveReply(articleId,commentId) {
    const content = document.getElementById('content_'+commentId).value; // 텍스트 박스 내용 가져오기

    if (!content.trim()) {
        alert('답글 내용을 입력하세요!');
        return;
    }

    fetch(`/api/comments/reply/${articleId}/${commentId}`, {
        method: 'POST',
        headers: {
            "Authorization": 'Bearer ' + localStorage.getItem('access_token'),
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ content: content }), // JSON 형식으로 전송
    })
        .then(response => {
            if (response.ok) {
                alert('답글이 성공적으로 등록되었습니다.');
                window.location.reload(); // 페이지 새로고침
            } else {
                alert('답글 등록에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류가 발생했습니다.');
        });
}

document.addEventListener("DOMContentLoaded", function () {
    // 서버에서 로그인 사용자 ID 가져오기
    fetch("/api/comments/user/current", {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("access_token"),
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("로그인 사용자 정보를 가져올 수 없습니다.");
        }
        return response.json();
    })
    .then(data => {
        const loggedInUserId = data.userId;
        const loggedInUserNickname = data.userNickName;
        //console.log("loggedInUserNickname = "+loggedInUserNickname);
        //console.log("loggedInUserId = "+loggedInUserId);

        // 게시글 작성자 닉네임과 로그인 사용자 닉네임를 비교하여 버튼 표시
        const articleWriter = document.getElementById('articleWriter').textContent;
        //console.log("articleWriter = "+articleWriter);
        if(loggedInUserNickname == articleWriter) {
            document.getElementById('btnMod').style.display = "block";
            document.getElementById('btnDel').style.display = "block";
        }

        // 댓글 작성자와 로그인 사용자 ID를 비교하여 해당 댓글의 버튼 표시
        document.querySelectorAll("[id^='answerBtns_']").forEach(div => {
            const userId = div.id.split("_")[1]; // answerBtns_ 뒤의 숫자(ID) 추출
            if (userId == loggedInUserId) { // 문자열/숫자 비교를 위해 ==
                div.style.display = "block"; // div 표시
            }
        });

        // 답글 작성자와 로그인 사용자 ID를 비교하여 해당 댓글의 버튼 표시
        document.querySelectorAll("[id^='replyBtns_']").forEach(div => {
            const userId = div.id.split("_")[1]; // replyBtns_ 뒤의 숫자(ID) 추출
            if (userId == loggedInUserId) { // 문자열/숫자 비교를 위해 ==
                div.style.display = "block"; // div 표시
            }
        });

    })
    .catch(error => {
        console.error("Error fetching user info:", error);
    });
});

document.addEventListener('DOMContentLoaded', () => {
    if (!isAuthenticated()) {
        alert('로그인이 필요합니다.');
        window.location.href = '/login';
        return;
    }
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