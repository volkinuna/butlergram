//회원정보 수정
function update(userId) {
	event.preventDefault();
	
	let data = $("#profileUpdate").serialize();
	
	$.ajax({
	    url : "/user/" + userId,
		type: "PUT",
		data: data,
		contentType: "application/x-www-form-urlencoded; charset=utf-8",
		dataType: "json",
		beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success : function(res, status) {
            console.log("update 성공");
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