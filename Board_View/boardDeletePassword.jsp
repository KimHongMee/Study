<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>글 삭제 전 비밀번호 확인 페이지</title>
</head>
<body>

<h3>게시글 삭제를 위해 비밀번호를 입력해주세요</h3>

<form action="boardDeleteCheck.bbs" method="post">

<input type="password" name="password">
<input type="hidden" name="num" value="${num}">
<input type="submit" value="확인">

</form>


</body>
</html>