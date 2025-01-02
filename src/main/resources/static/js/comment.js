
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

    fetch(`/comments/edit/${commentId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
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
        fetch(`/comments/delete/${commentId}`, {
            method: 'DELETE',
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
