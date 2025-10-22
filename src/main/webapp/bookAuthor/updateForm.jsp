<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%!
    // ... DB 연결 정보 (authorApp.jsp와 동일하게) ...
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    
    String url = "jdbc:oracle:thin:@localhost:1521:XE";
    String user = "webdb";
    String pass = "1234";
    String sql = "SELECT author_id, author_name, author_desc FROM author WHERE author_id = ?";
%>
<%
    // 1. 수정할 ID 받기
    String author_id = request.getParameter("author_id");
    
    // 2. 해당 ID의 데이터 조회하기
    String author_name = "";
    String author_desc = "";
    
    try {
        // ... DB 연결 로직 ...
        Class.forName("oracle.jdbc.driver.OracleDriver");
		conn = DriverManager.getConnection(url, user, pass); // url, user, pass 설정 필요
        
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, Integer.parseInt(author_id));
        rs = pstmt.executeQuery();
        
        if (rs.next()) {
            author_name = rs.getString("author_name");
            author_desc = rs.getString("author_desc");
        }
    } catch(Exception e) {
        e.printStackTrace();
    } finally {
        // ... DB 닫기 로직 (rs, pstmt, conn) ...
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>저자 정보 수정</title>
</head>
<body>
	<h1>저자 정보 수정</h1>

	<form action="updateProcess.jsp" method="post">

		<input type="hidden" name="author_id" value="<%= author_id %>">

		<table width=800 class='tbl-ex tbl'>
			<tr>
				<th>저자번호</th>
				<td><%= author_id %></td>
			</tr>
			<tr>
				<th>저자명</th>
				<td><input type="text" name="author_name"
					value="<%= author_name %>" size="20"></td>
			</tr>
			<tr>
				<th>설명</th>
				<td><input type="text" name="author_desc"
					value="<%= author_desc %>" size="40"></td>
			</tr>
			<tr>
				<td colspan=2><input type="submit" value="수정완료"> <input
					type="button" value="취소"
					onclick="document.location.href='authorApp.jsp'"></td>
			</tr>
		</table>
	</form>
</body>
</html>