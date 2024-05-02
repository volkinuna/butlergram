// (1) 회원정보 수정
function update(userId, event) {
	event.preventDefault(); // 폼태그 액션을 막기!!
	
	let data = $("#profileUpdate").serialize(); // 데이터를 key=value 형태로 받아올때 쓴다. // FormData는 사진이나 글을 섞어서 보낼때 쓴다.
	
	console.log(data);
	
	$.ajax({
		type: "put",
		url : `/api/user/${userId}`,
		data: data, 
		contentType: "application/x-www-form-urlencoded; charset=utf-8", // data의 설명
		dataType: "json"	// 응답받을 data 타입	
	}).done(res=>{ // json data를 자바스크립트로 파싱해서 res에 응답을 받는다. 그래서 res는 자바스크립트 오브젝트가 된다. // Http Status 상태코드가 200번대면 done
		console.log("update 성공", res);
		location.href = `/user/${userId}`;
	}).fail(error=>{ // Http Status 상태코드가 200번대가 아닐때 fail
		if(error.data == null) {
			alert(error.responseJSON.message);
		} else {
			alert(JSON.stringify(error.responseJSON.data));
		}
	});
}