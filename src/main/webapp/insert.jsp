<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>insert title</title>
</head>

<body>

<%
	request.setCharacterEncoding("UTF-8");
	
	String id = request.getParameter("id");
	String password = request.getParameter("password");
	String file = request.getParameter("file");
	String authorID = request.getParameter("authorID");
	
	out.println("<span>"+ id + "</span>");
	out.println("<span>"+ password + "</span>");
	out.println("<span>"+ file + "</span>");
	out.println("<span>"+ authorID + "</span>");
	
%>

	<form method="get" action="insert.jsp " enctype="">
		<input type="text" name="id" value="bit"/><br/>
		
		<input type="password" name="password" /> <br/>
		<input type="file" name="file" /> <br/>
		<input type="hidden" name="authorId" /> <br/>
		<input type="button" value="내가만든버튼" /> <br/>
		
		<input type="submit" />
	</form>
</body>

</html>

