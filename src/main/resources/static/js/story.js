/**
	2. 스토리 페이지
	(1) 스토리 로드하기
	(2) 스토리 스크롤 페이징하기
	(3) 좋아요, 안좋아요
	(4) 댓글쓰기
	(5) 댓글삭제
 */

 var token = $("meta[name='_csrf']").attr("content"); //token의 실제 값
 var header = $("meta[name='_csrf_header']").attr("content"); //token의 name

// (0) 현재 로그인한 사용자 아이디
let principalId = $("#principalId").val();

//alert(principalId);

// (1) 스토리 로드하기
let page = 0;

function storyLoad() {
	$.ajax({ // type의 get은 디폴트/ 그래서 여기선 생략함.
		url: `/post?page=${page}`,
		type : "GET",
		dataType: "json",
		beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        }
	}).done(res => {
      		console.log(res);
      		res.data.content.forEach((image)=>{
      			let storyItem = getStoryItem(image);
      			$("#storyList").append(storyItem);
      		});
      	}).fail(error=>{
          		console.log("오류", error.responseText)
          	});
}

storyLoad();

function getStoryItem(post) {
    let profileImageUrl = post.users.profileImageUrl == null ? '/images/person.jpeg' : '/upload/' + post.users.profileImageUrl;
	let item = `<div class="story-list__item">
	<div class="sl__item__header">
		<div>
			<img class="profile-image" src="${profileImageUrl}"/>
		</div>
		<div>${post.users.username}</div>
	</div>

	<div class="sl__item__img">
		<img src="/upload/${post.postImageUrl}" />
	</div>

	<div class="sl__item__contents">
		<div class="sl__item__contents__icon">

			<button>`;
			
			if(post.likeState){
				item +=`<i class="fas fa-heart active" id="storyLikeIcon-${post.id}" onclick="toggleLike(${post.id})"></i>`;
			}else{
				item +=`<i class="far fa-heart" id="storyLikeIcon-${post.id}" onclick="toggleLike(${post.id})"></i>`;
			}
				item +=`
			</button>
		</div>

		<span class="like"><b id="storyLikeCount-${post.id}">${post.likeCount}</b>likes</span>

		<div class="sl__item__contents__content">
			<p>${post.caption}</p>
		</div>

		<div id="storyCommentList-${post.id}">`;

			post.comments.forEach((comment)=>{
				item +=`<div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}">
				<p>
					<b>${comment.users.username} :</b> ${comment.content}
				</p>`;
				
				if(principalId == comment.users.id){
					item +=`<button onclick="deleteComment(${comment.id})">
				     	              <i class="fas fa-times"></i>
                                 </button>`;
				}

			item +=`
			</div>`;

			});

		item +=`
		</div>

		<div class="sl__item__input">
			<input type="text" placeholder="댓글 달기..." id="storyCommentInput-${post.id}" />
			<button type="button" onClick="addComment(${post.id})">게시</button>
		</div>

	</div>
</div>`;
console.log(item)
	return item;
}

// (2) 스토리 스크롤 페이징하기
$(window).scroll(() => {
	//console.log("윈도우 scrollTop", $(window).scrollTop());
	//console.log("문서의 높이", $(document).height());
	//console.log("윈도우의 높이", $(window).height());
	
	let checkNum = $(window).scrollTop() - ($(document).height() - $(window).height());
	//console.log(checkNum);
	
	if(checkNum < 1 && checkNum > -1){
		page++;
		storyLoad();
	}
});


// (3) 좋아요, 좋아요취소
function toggleLike(postId) {
	let likeIcon = $(`#storyLikeIcon-${postId}`);
	
	if (likeIcon.hasClass("far")) { // 클릭을 했을때 far(빈하트)라는걸 좋아요를 하겠다..
		
		$.ajax({
			url: `/post/${postId}/likes`,
			type: "post",
			dataType: "json",
			contentType : "application/json",
            beforeSend : function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success : function(result, status) {
			    let likeCountStr = $(`#storyLikeCount-${postId}`).text(); // 해당 아이디로 접근해서 그 내부에있는 text를 가져와라..
		        let likeCount = Number(likeCountStr) + 1; // likeCountStr는 문자열. Number로 캐스팅해줘야함.
			    $(`#storyLikeCount-${postId}`).text(likeCount);

			    likeIcon.addClass("fas");
	    	    likeIcon.addClass("active");
	    	    likeIcon.removeClass("far");
            },
            error : function(jqXHR, status, error) {
			    console.log("오류", error);
            }
		});

	} else { // 좋아요 취소를 하겠다..
		
		$.ajax({
			url: `/post/${postId}/likes`,
			type : "DELETE",
            dataType : "json",
            contentType : "application/json",
            beforeSend : function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success : function(result, status) {
			    let likeCountStr = $(`#storyLikeCount-${postId}`).text(); // 해당 아이디로 접근해서 그 내부에있는 text를 가져와라..
		        let likeCount = Number(likeCountStr) - 1; // likeCountStr는 문자열. Number로 캐스팅해줘야함.
			    $(`#storyLikeCount-${postId}`).text(likeCount);

	    	    likeIcon.removeClass("fas");
	    	    likeIcon.removeClass("active");
	    	    likeIcon.addClass("far");
            },
            error : function(jqXHR, status, error) {
			    console.log("오류", error);
            }
		});
	}
}

// (4) 댓글쓰기
function addComment(postId) {

	let commentInput = $(`#storyCommentInput-${postId}`);
	let commentList = $(`#storyCommentList-${postId}`);

	let data = {
		postId: postId,
		content: commentInput.val()
	}
	
	//console.log(data);
	//console.log(JSON.stringify(data));

if (data.content === "") {
		alert("댓글을 작성해주세요!");
		return;
	}
	
	$.ajax({
		url: "/comment",
		type: "POST",
		data: JSON.stringify(data),
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success : function(res, status) {
		    //console.log("성공", res);

		    let comment = res.data;

		    let content = `
		        <div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}">
		        <p>
		            <b>${comment.users.username} :</b>
		            ${comment.content}
		        </p>
		        <button onclick="deleteComment(${comment.id})"><i class="fas fa-times"></i></button>
		        </div>
            `;

	        commentList.prepend(content);
        },
        error : function(jqXHR, status, error) {
		    console.log("오류", error.responseJSON.data.content);
		    alert(error.responseJSON.data.content);
        }
	});

	commentInput.val(""); // 인풋 필드를 깨끗하게 비워준다. 오류가 나도 비워줄꺼라 res에 안 넣고 따로 둔것.
}

// (5) 댓글 삭제
function deleteComment(commentId) {
	$.ajax({
		url: `/comment/${commentId}`,
		type : "DELETE",
        dataType : "json",
        contentType : "application/json",
        beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success : function(result, status) {
		    console.log("성공", res);
		    $(`#storyCommentItem-${commentId}`).remove();
        },
        error : function(jqXHR, status, error) {
		    console.log("오류", error);
        }
	});
}







