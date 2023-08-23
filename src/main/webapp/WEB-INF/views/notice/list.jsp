<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>공지사항 목록</title>
		<link rel="stylesheet" href="../resources/css/notice/notice.css">
	</head>
	<body>
		<h1>공지사항 목록</h1>
		<table>
			<thead>
				<tr>
					<th>번호</th>
					<th>제목</th>
					<th>작성자</th>
					<th>작성날짜</th>
					<th>첨부파일</th>
				</tr>
			</thead>
			<tbody>
			<!-- list데이터는 items에 넣었고 var에서 설정한 변수로 list데이터에서
			꺼낸 값을 사용하고 i의 값은 varStatus로 사용 -->
				<c:forEach var="notice" items="${nList }" varStatus="i">
					<tr>
						<td>${notice.noticeNo }</td>
						<c:url var="detailUrl" value="/notice/detail.kh">
						<c:param name="noticeNo" value="${notice.noticeNo }"></c:param>
						</c:url>
						<td><a href="${detailUrl }">${notice.noticeSubject }</a></td>
						<td>${notice.noticeWriter }</td>
						<td>
							<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${notice.nCreateDate }"/>
						</td>
						<td>
							<c:if test="${!empty notice.noticeFileName }">O</c:if>
							<c:if test="${empty notice.noticeFileName }">X</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr align="center">
					<td colspan="5">
					<c:if test="${pInfo.currentPage != 1 }">
								<a href="/notice/list.kh?page=${pInfo.currentPage - 1 }">[이전]</a>&nbsp;
					</c:if>
					<c:forEach begin="${pInfo.startNavi }" end="${pInfo.endNavi }" var="p">
						<c:url var="pageUrl" value="/notice/list.kh">
							<c:param name="page" value="${p }"></c:param>
						</c:url>
						<a href="${pageUrl }">${p }</a>&nbsp;
					</c:forEach>
					<c:if test="${pInfo.currentPage ne pInfo.naviTotalCount }">
								<a href="/notice/list.kh?page=${pInfo.currentPage + 1 }">[다음]</a>
					</c:if>
					</td>
				</tr>
				<tr>
					<td colspan="4">
						<form action="/notice/search.kh" method="get">
							<select name="searchCondition">
								<option value="all">전체</option>
								<option value="writer"">작성자</option>
								<option value="title">제목</option>
								<option value="content">내용</option>
							</select>
							<input type="text" name="searchKeyword" placeholder="검색어를 입력하세요">
							<input type="submit" value="검색">
						</form>
					</td>
					<td>
						<button type="button" onclick="showRegisterForm();">글쓰기</button>
					</td>
				</tr>
			</tfoot>
		</table>
		<script>
			function showRegisterForm(){
				location.href = "/notice/insert.kh";
			}
		</script>
	</body>
</html>