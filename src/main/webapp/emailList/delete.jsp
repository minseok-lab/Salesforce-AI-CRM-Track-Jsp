<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="emaillist.EmaillistDao"%>
<%@ page import="emaillist.EmaillistVo"%>

<% 
	request.setCharacterEncoding("UTF-8");
	String no = request.getParameter("no");
	
	//out.println(no);
	
  EmaillistDao dao = new EmaillistDao();
	dao.delete(no);
  
  

	response.sendRedirect("list.jsp");
	
%>
