
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

//유저 프로파일 페이지 구독하기, 구독취소
function toggleSubscribe(toUserId, obj) {
	if ($(obj).text() === "구독취소") {
		$.ajax({
		    url : "/user/subscribe/" + toUserId,
			type : "DELETE",
			dataType : "json",
			contentType : "application/json",
			beforeSend : function(xhr) {
			    xhr.setRequestHeader(header, token);
			},
			success : function(result, status) {
			    $(obj).text("구독하기");
                $(obj).toggleClass("blue");
			},
			error : function(jqXHR, status, error) {
			    if(jqXHR.status == '401') {
			        console.log("구독취소실패", error);
			    } else {
			        alert(jqXHR.responseText);
			    }
			}
		});
	} else {
		$.ajax({
		    url: "/user/subscribe/" + toUserId,
			type: "POST",
			dataType: "json",
			contentType : "application/json",
			beforeSend : function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success : function(result, status) {
                $(obj).text("구독취소");
                $(obj).toggleClass("blue");
            },
            error : function(jqXHR, status, error) {
            	if(jqXHR.status == '401') {
            	    console.log("구독하기실패", error);
            	} else {
            		alert(jqXHR.responseText);
            	}
            }
        });
	}
}

//구독자 정보 모달 보기
function subscribeInfoModalOpen(userId) {
	$(".modal-subscribe").css("display", "flex");

	$.ajax({
		url: "/user/" + userId + "/subscribe",
		dataType: "json",
		contentType : "application/json",
		beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success : function(res, status) {
		    console.log(res.data);

		    res.data.forEach((u)=>{
			    let item = getSubscribeModalItem(u);
			    $("#subscribeModalList").append(item);
			});
        },
        error : function(jqXHR, status, error) {
            if(jqXHR.status == '401') {
		        console.log("구독정보 불러오기 오류", error);
            } else {
                alert(jqXHR.responseText);
            }
        }
    });
}

function getSubscribeModalItem(u) {
	let item = `<div class="subscribe__item" id="subscribeModalItem-${u.id}">
	<div class="subscribe__img">
		<img src="/upload/${u.profileImageUrl}" onerror="this.src='/images/person.jpeg'" />
	</div>
	<div class="subscribe__text">
		<h2>${u.username}</h2>
	</div>
	<div class="subscribe__btn">`;

	if(!u.equalUserState){ //동일 유저가 아닐 때 버튼이 만들어져야 함
		if(u.subscribeState){ //구독 한 상태
			item += `<button class="cta blue" onclick="toggleSubscribe(${u.id}, this)">구독취소</button>`;
		}else{ //구독 안한 상태
			item += `<button class="cta" onclick="toggleSubscribe(${u.id}, this)">구독하기</button>`;
		}
	}
	item += `
	</div>
</div>`;

	return item;
}


//유저 프로필 사진 변경
function profileImageUpload(userId, principalId) {

	if(userId != principalId){
		alert("프로필 사진을 수정할 수 없는 유저입니다.");
		return;
	}

	$("#userProfileImageInput").click();

	$("#userProfileImageInput").on("change", (e) => {
		let f = e.target.files[0];

		if (!f.type.match("image.*")) {
			alert("이미지를 등록해야 합니다.");
			return;
		}

        //서버에 이미지를 전송
        let profileImageForm = $("#userProfileImageForm")[0];

		let formData = new FormData(profileImageForm);

		$.ajax({
			url: "/user/" + principalId + "/profileImageUrl",
			type: "PUT",
			data: formData,
			contentType: false,
			processData: false,
			enctype: "multipart/form-data",
			dataType: "json",
			beforeSend : function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success : function(result, status) {
		        //사진 전송 성공시 이미지 변경
	    	    let reader = new FileReader();
	    	    reader.onload = (e) => {
			        $("#userProfileImage").attr("src", e.target.result);
	    	    }
	    	    reader.readAsDataURL(f); //이 코드 실행시 reader.onload 실행됨
            },
           error : function(jqXHR, status, error) {
                if(jqXHR.status == '401') {
            	    console.log("오류", error);
                } else {
                    alert(jqXHR.responseText);
                }
            }
		});
	});
}

//사용자 정보 메뉴 열기 닫기
function popup(obj) {
	$(obj).css("display", "flex");
}

function closePopup(obj) {
	$(obj).css("display", "none");
}

//사용자 정보(회원정보, 로그아웃, 닫기) 모달
function modalInfo() {
	$(".modal-info").css("display", "none");
}

//사용자 프로파일 이미지 메뉴(사진업로드, 취소) 모달
function modalImage() {
	$(".modal-image").css("display", "none");
}

//구독자 정보 모달 닫기
function modalClose() {
	$(".modal-subscribe").css("display", "none");
	location.reload();
}
