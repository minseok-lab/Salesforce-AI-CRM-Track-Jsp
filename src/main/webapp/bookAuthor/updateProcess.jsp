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
	// [!] 수정 SQL (WHERE 조건절이 중요!)
	String sql = "UPDATE author SET author_name = ?, author_desc = ? WHERE author_id = ?";%>
<%
request.setCharacterEncoding("utf-8");

// 1. 폼에서 3개의 파라미터 받기
String author_id = request.getParameter("author_id");
String author_name = request.getParameter("author_name");
String author_desc = request.getParameter("author_desc");

try {
	Class.forName("oracle.jdbc.driver.OracleDriver");
	conn = DriverManager.getConnection(url, user, pass);

	pstmt = conn.prepareStatement(sql);

	// 2. SQL 바인딩 (순서 중요!)
	pstmt.setString(1, author_name);
	pstmt.setString(2, author_desc);
	pstmt.setInt(3, Integer.parseInt(author_id)); // [!] WHERE 절에 들어갈 ID

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