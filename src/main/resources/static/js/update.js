// (1) 회원정보 수정
function update(userId, event) {
	event.preventDefault(); // 폼태그 액션을 막기!!
	
	let data = $("#profileUpdate").serialize(); // 데이터를 key=value 형태로 받아올때 쓴다. // FormData는 사진이나 글을 섞어서 보낼때 쓴다.
	
	console.log(data);
	
	$.ajax({
	    url : "user/" + userId,
		type: "PUT",
		data: data,
		contentType: "application/x-www-form-urlencoded; charset=utf-8", // data의 설명
		dataType: "json",	// 응답받을 data 타입
		beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success : function(result, status) {
            console.log("update 성공", res);
            location.href = `/user/${userId}`;
        },
        error : function(jqXHR, status, error) {
            if(error.data == null) {
        	    alert(error.responseJSON.message);
        	} else {
        		alert(JSON.stringify(error.responseJSON.data));
        	}
        }
	});
}