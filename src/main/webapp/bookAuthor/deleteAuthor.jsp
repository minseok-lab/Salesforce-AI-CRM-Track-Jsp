<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%!Connection conn = null;
	PreparedStatement pstmt = null;
	String url = "jdbc:oracle:thin:@localhost:1521:XE";
	String user = "webdb";
	String pass = "1234";
	
	String sql = "DELETE FROM author WHERE author_id = ?"; // [!] 삭제 SQL%>
<%
// 1. 전달받은 author_id 가져오기
String author_id = request.getParameter("author_id");

try {
	Class.forName("oracle.jdbc.driver.OracleDriver");
	conn = DriverManager.getConnection(url, user, pass);

	pstmt = conn.prepareStatement(sql);

	// 2. SQL 바인딩
	pstmt.setInt(1, Integer.parseInt(author_id));

	// 3. SQL 실행
	pstmt.executeUpdate();

} catch (Exception e) {
	e.printStackTrace();
} finally {
	try {
		if (pstmt != null)
	pstmt.close();
		if (conn != null)
	conn.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
}

// 4. 완료 후, 메인 페이지로 다시 이동 (목록 새로고침)
response.sendRedirect("authorApp.jsp");
%>