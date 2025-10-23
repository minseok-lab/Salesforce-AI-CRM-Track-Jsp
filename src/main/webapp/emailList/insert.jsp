<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="emaillist.EmaillistDao"%>
<%@ page import="emaillist.EmaillistVo"%>

<% 
	request.setCharacterEncoding("UTF-8");
	String lastName = request.getParameter("ln");
	String firstName = request.getParameter("fn");
	String email = request.getParameter("email");
	
	EmaillistVo vo = new EmaillistVo(lastName, firstName, email);
	
	EmaillistDao dao = new EmaillistDao();
	dao.insert(vo);

	response.sendRedirect("list.jsp");
	
%>
