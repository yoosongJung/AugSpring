<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>게시글 상세</title>
		<link rel="stylesheet" href="../resources/css/main.css">
	</head>
	<body>
		<h1>게시글 상세</h1>
			<ul>
				<li>
					<label>제목</label>
					<input type="text" name="boardTitle" value="${board.boardTitle }" readonly>
				</li>
				<li>
					<label>작성자</label>
					<input type="text" name="boardWriter" value="${board.boardWriter }" readonly>
				</li>
				<li>
					<label>내용</label>
					<textarea rows="4" cols="50" name="boardContent" readonly>${board.boardContent }</textarea>
				</li>
				<li>
					<label>첨부파일</label>
					<%-- <img alt="첨부파일" src="../resources/nuploadFiles/${board.boardFileRename }"> --%>
					<a href="../resources/buploadFiles/${board.boardFileRename }" download>${board.boardFilename }</a>
				</li>
			</ul>
			<br>
			<div>
				<button type="button" onclick="showModifyPage();">수정하기</button>
				<button>삭제하기</button>
				<button type="button" onclick="showBoardList();">목록으로</button>
			</div>
			<!-- 댓글 등록 -->
			<form action="/reply/add.kh" method="post">
			<input type="hidden" name="refBoardNo" value="${board.boardNo }">
				<table width="550" border="1">
					<tr>
						<td>
							<textarea rows="3" cols="55" name="replyContent"></textarea>
						</td>
						<td>
							<input type="submit" value="완료">
						</td>
					</tr>
				</table>
			</form>
			<!-- 댓글 목록 -->
			<table width="550" border="1">
				<tr>
					<th>작성자</th>
					<th>내용</th>
					<th>작성일</th>
					<th>수정/삭제</th>
				</tr>
				<c:forEach var="reply" items="${rList }">
				<tr>
					<td>${reply.replyWriter }</td>
					<td>${reply.replyContent }</td>
					<td>${reply.rCreateDate }</td>
					<td>
						<a href="javascript:void(0);" onclick="showReplyModifyForm(this);">수정하기</a>
						<a href="#">삭제하기</a>
					</td>
				</tr>
				<tr id="replyModifyForm" style="display:none;">
					<%-- <form action="/reply/update.kh" method="post">
						<input type="hidden" name="replyNo" value="${reply.replyNo }">
						<input type="hidden" name="refBoardNo" value="${reply.refBoardNo }">
						<td colspan="3"><input type="text" size="50" name="replyContent" value="${reply.replyContent }"></td>
						<td><input type="submit" value="완료"></td>
					</form> --%>
					<td colspan="3"><input id="replyContent" type="text" size="50" name="replyContent" value="${reply.replyContent }"></td>
					<td><input type="button" onclick="replyModify(this,'${reply.replyNo}','${reply.refBoardNo }')" value="완료"></td>
				</tr>
				</c:forEach>
			</table>
			<script>
				function showModifyPage(){
					location.href="/board/modify.kh?boardNo=${board.boardNo}";
				}
				function showBoardList(){
					location.href="/board/list.kh";
				}
				function replyModify(obj, replyNo, refBoardNo){// 댓글 수정 폼태그로 보낼때 자바스크립트로 하기
					const form = document.createElement("form");
					form.action="/reply/update.kh";
					form.method="post";
					const input = document.createElement("input");
					input.type="hidden";
					input.value=replyNo;
					input.name="replyNo";
					const input2 = document.createElement("input");
					input2.type="hidden";
					input2.value=refBoardNo;
					input2.name="refBoardNo";
					const input3 = document.createElement("input");
					input3.type="text";
					input3.value=obj.parentElement.previousElementSibling.firstChild.value;
					input3.name="replyContent";
					form.appendChild(input);
					form.appendChild(input2);
					form.appendChild(input3);
					
					document.body.appendChild(form);
					form.submit();
				}
				function showReplyModifyForm(obj){
					/* <tr id="replyModifyForm" style="display:none;">
					<td colspan="3"><input type="text" size="50" value="${reply.replyContent }"></td>
					<td><input type="button" value="완료"></td>
					</tr> */
					/* const trTag = document.createElement("tr");
					const tdTag1 = document.createElement("td");
					tdTag1.colSpan = 3;
					const inputTag1 = document.createElement("input");
					inputTag1.type="text";
					inputTag1.size=50;
					inputTag1.value=replyConent;
					tdTag1.appendChild(inputTag1);
					const tdTag2 = document.createElement("td");
					const inputTag2 = document.createElement("input");
					inputTag2.type="button";
					inputTag2.value="완료";
					tdTag2.appendChild(inputTag2);
					trTag.appendChild(tdTag1);
					trTag.appendChild(tdTag2);
					console.log(trTag);
					const prevTrTag = obj.parentElement.parentElement;
					if(!prevTrTag.nextElementSibling.querySelector("input"))
					prevTrTag.parentNode.insertBefore(trTag, prevTrTag.nextSibling); */
					if(obj.parentElement.parentElement.nextElementSibling.style.display=="none"){
						obj.parentElement.parentElement.nextElementSibling.style.display="";
						obj.innerText = "수정취소";
					} else{
						obj.parentElement.parentElement.nextElementSibling.style.display="none";
						obj.innerText = "수정하기";
					}
				}
			</script>
	</body>
</html>