<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>게시판 목록 조회</title>
</head>
<body>

<h3>이렇게 예쁠 수가 없는 나의 게시판 ^^</h3>

<table border="1">
	<tr>
		<td colspan="7" align="right">
			<a href="boardWriteForm.bbs">[새 글 작성]</a>
		</td>
	</tr>
	<tr>
		<td align="center">No.    </td>
		<td align="center">글 제목    </td>
		<td align="center">작성자     </td>
		<td align="center">작성일     </td>
		<td align="center">작성시간  </td>
		<td align="center">조회수     </td>
		<td align="center">답글수     </td>	
	</tr>
		<c:forEach items="${boardList }" var="dto">
	<tr>
		<td align="center">
		<a href="boardRead.bbs?num=${dto.num}">${dto.num }</a>
		</td>
		<td>
		<c:forEach begin="1" end="${dto.lev }">
		<%="&nbsp;&nbsp;" %>
		</c:forEach>
		<a href="boardRead.bbs?num=${dto.num }">${dto.subject }</a>
		</td>
		<td>${dto.name }</td>
		<td align="center">${dto.writeDate }</td>
		<td align="center">${dto.writeTime }</td>
		<td align="center">${dto.readCnt }</td>
		<td align="center">${dto.childCnt }</td>
	</tr>
		</c:forEach>	
	<tr>
		<td colspan="7">
			<a href="boardList.bbs">[첫 페이지로]</a>
			<c:forEach var="i" begin="1" end="${pageCnt }">
			<a href="boardList.bbs?curPage=${i }">[${i }]</a>
			</c:forEach>
		</td>
	</tr>
		
	<tr>
		<td colspan="7" align="center">
			<form action="boardSearch.bbs" method="post">
				<select name="serchOption">
					<option value="subject">제목</option>
					<option value="content">본문</option>
					<option value="both">제목+본문</option>
					<option value="name">작성자</option>
				</select>
				<input type="text" name="searchWord">
				<input type="submit" value="검색">
			</form>
		
	</tr>
		
</table>

</body>
</html>