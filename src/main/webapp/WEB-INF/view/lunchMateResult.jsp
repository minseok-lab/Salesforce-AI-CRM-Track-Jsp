<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>



<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>점심 메이트 추첨 결과</title>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/LunchStyle.css">

</head>

<body>

	<h1>🍱 점심 메이트 추첨 결과!</h1>

	<div class="group-container">

	<c:forEach items="${lunchGroups}" var="group" varStatus="loop">

		<div class="group-box">


			<h2>${loop.index + 1}조 (${fn:length(group)}명)</h2>


			<ul>

				<c:forEach items="${group}" var="member">

					<li>${member}</li>
				</c:forEach>

			</ul>

		</div>

	</c:forEach>

	</div> <p>즐거운 점심시간 되세요!</p>
	
	<form action="${pageContext.request.contextPath}/randomLunch" method="get">
        <button type="submit">
            🔄 다시 뽑기 (리롤)
        </button>
    </form>

</body>

</html>