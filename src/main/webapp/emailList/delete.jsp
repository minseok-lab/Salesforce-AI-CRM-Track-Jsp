<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="emaillist.EmaillistDao"%>
<%@ page import="emaillist.EmaillistVo"%>

<% 
	request.setCharacterEncoding("UTF-8");
	
	//out.println(no);
	
  	// 1. "no" 값을 문자열(String)로 받습니다. (기존 코드)
    String no = request.getParameter("no");

    // 2. [중요] EmaillistVo 객체를 새로 생성합니다.
    EmaillistVo vo = new EmaillistVo();
    
    // 3. [추측] 'no'가 실제로는 숫자(int) 타입이라고 추측합니다.
    //    getParameter()는 항상 문자열을 반환하므로, 숫자로 변환해줍니다.
    int no_int = Integer.parseInt(no);
    
    // 4. 생성한 vo 객체에 숫자 no 값을 설정합니다.
    //    (EmaillistVo.java에 setNo(int no) 메서드가 있어야 합니다.)
    vo.setNo(no_int); 

    // 5. DAO 객체를 생성합니다.
    EmaillistDao dao = new EmaillistDao();
    
    // 6. 'no' 문자열 대신 'vo' 객체를 전달합니다.
    dao.delete(vo);
	
	response.sendRedirect("list.jsp");
	
%>
