<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>회원 정보</title>
		<link rel="stylesheet" href="../resources/css/main.css">
	</head>
	<body>
		<h1>회원정보</h1>
			<fieldset>
				<legend>회원정보</legend>	
				<ul>
					<li>
						<label>아이디 *</label>
						<span>${member.memberId }</span>			
					</li>
					<li>
						<label>이름 *</label>	
						<span>${member.memberName }</span>				
					</li>
					<li>
						<label>나이</label>		
						<span>${member.memberAge }</span>			
					</li>
					<li>
						<label>성별</label>	
						<c:if test="${member.memberGender eq 'M' }">
						<span>남</span>
							<!-- <input type="radio" name="memberGender" value="M" checked="checked">남
							<input type="radio" name="memberGender" value="F">여 -->
						</c:if>	
						<c:if test="${member.memberGender eq 'F'}">
						<span>여</span>
							<!-- <input type="radio" name="memberGender" value="M">남
							<input type="radio" name="memberGender" value="F" checked="checked">여 -->
						</c:if>			
					</li>
					<li>
						<label>이메일</label>
						<span>${member.memberEmail }</span>					
					</li>
					<li>
						<label>전화번호</label>	
						<span>${member.memberPhone }</span>				
					</li>
					<li>
						<label>주소</label>	
						<span>${member.memberAddr }</span>				
					</li>
					<li>
						<label>취미</label>	
						<span>${member.memberHobby }</span>				
					</li>
				</ul>	
			</fieldset>
			<a href="/index.jsp">메인으로 이동</a>
			<a href="/member/update.kh?memberId=${member.memberId }">수정하기</a>
			<a href="/member/delete.kh?memberId=${member.memberId }">탈퇴하기</a>
	</body>
</html>