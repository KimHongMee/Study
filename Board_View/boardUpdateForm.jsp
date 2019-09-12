<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>게시판 글 수정</title>
</head>
<body>

<h3>작성하신 게시글을 수정합니다</h3>

<form action = "boardUpdate.bbs" method = "post">


<table>

	<tr>
		<td colspan="4" align="right">
		<input type = "hidden" name="num" value="${boardUpdateForm.num}">
		<a href="boardList.bbs"> [목록으로]</a>
		</td>
	</tr>
	<tr>
		<td> 글 제목 </td>
		<td colspan="3">
		<input type="text" name="subject" 
		size="50" maxlength="50" value="${boardUpdateForm.subject }">
		</td>
	</tr>
	<tr>
		<td> 작성자</td>
		<td>
		<input type ="text" name="name" maxlength="20" size="20"
		value="${boardUpdateForm.name }">
		</td>
	</tr>
	<tr>
		<td> 비밀번호</td>
		<td>		
		<input type ="text" name="password" maxlength="20" size="12"
		value="${boardUpdateForm.password }">
		</td>
	</tr>	
 	<tr>
 		<td> 본문</td>
 		<td colspan="3">
		<textarea rows="8" cols="45" name="content">${boardUpdateForm.content }</textarea>
		</td>
	</tr>
	<tr>
		<td colspan="4" align="right">
		<input type="submit" value="수정하기">
		</td>	
	</tr>
</table>

</form>

</body>
</html>