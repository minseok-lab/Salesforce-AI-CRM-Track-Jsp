<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.sql.*"%>
<%! 
  Connection conn = null;
  PreparedStatement pstmt = null;
  ResultSet rs = null;
  
  String url = "jdbc:oracle:thin:@localhost:1521:XE";
  String user = "webdb";
  String pass = "1234";
  
  String sql = " Select author_id, " 
             + " author_name,"
             + " author_desc"
             + " from author order by 1 desc ";
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>저자정보</title>
<style type="text/css" media="screen">
* {
	margin: 0;
	padding: 0
}

body {
	font-family: '맑은 고딕' 돋움;
	font-size: 0.75em;
	color: #333
}

h1 {
	text-align: center;
	margin-top: 20px;
}

.tbl-ex {
	margin: 10px auto;
	border: 1px solid #333;
	border-collapse: collapse;
	width: 800px;
}

.tbl-ex th {
	border: 1px solid #333;
	padding: 8px;
	text-align: center;
}

.tbl-ex td {
	border: 1px solid #333;
	padding: 8px;
	text-align: left;
}

.tbl-ex th {
	background-color: #999;
	font-size: 1.1em;
	color: #fff;
}

.tbl-ex tr.even {
	background-color: #E8ECF6
}

.tbl-ex td:hover, .tbl-ex td.even:hover {
	background-color: #fc6;
	cursor: pointer
}
/* 행 선택 강조 (단일 선택 + 토글) */
tr.selected td {
	background: #ffe9a8 !important
}

tr.selected:hover td {
	background: #ffe9a8 !important
}

td.group-name {
	font-weight: 700;
	width: 120px
}
</style>
<script>
   // 행 단일 선택 토글 (기존 코드)
   document.addEventListener('DOMContentLoaded', function () {
     const table = document.getElementById('authorListTable');
     let selectedRow = null; // 선택된 행을 저장할 변수

     table.addEventListener('click', function (e) {
       const td = e.target.closest('td, th');
       if (!td) return;
       const tr = td.parentElement;
       if (tr.querySelector('th')) return; // 헤더 행 제외

       const already = tr.classList.contains('selected');
       
       // 이전에 선택된 행이 있다면 선택 해제
       if (selectedRow) {
         selectedRow.classList.remove('selected');
       }
       
       if (!already) {
         tr.classList.add('selected');
         selectedRow = tr; // 현재 선택된 행 저장
       } else {
         selectedRow = null; // 토글로 선택 해제 시 변수 비우기
       }
     });
     
     // --- 🚀 [여기부터 확장된 코드] ---

     // '삭제하기' 버튼 클릭 시
     document.getElementById('deleteBtn').addEventListener('click', function() {
        if (!selectedRow) {
            alert('삭제할 행을 먼저 선택하세요.');
            return;
        }
        
        // 선택된 행의 첫 번째 <td> (저자번호) 텍스트 가져오기
        const authorId = selectedRow.cells[0].innerText;
        
        // 사용자에게 한 번 더 확인
        if (confirm('저자번호 [' + authorId + '] 님을 정말 삭제하시겠습니까?')) {
            // deleteAuthor.jsp로 author_id를 쿼리 파라미터로 넘기며 이동
            document.location.href = 'deleteAuthor.jsp?author_id=' + authorId;
        }
     });

     // '수정하기' 버튼 클릭 시
     document.getElementById('updateBtn').addEventListener('click', function() {
        if (!selectedRow) {
            alert('수정할 행을 먼저 선택하세요.');
            return;
        }
        
        // 선택된 행의 첫 번째 <td> (저자번호) 텍스트 가져오기
        const authorId = selectedRow.cells[0].innerText;
        
        // updateForm.jsp로 author_id를 쿼리 파라미터로 넘기며 이동
        document.location.href = 'updateForm.jsp?author_id=' + authorId;
     });

   });
 </script>
</head>
<body>
	<h1>저자 관리 Application</h1>

	<div style="width: 800px; margin: 10px auto; text-align: right;">
		<button type="button" id="updateBtn">수정하기</button>
		<button type="button" id="deleteBtn">삭제하기</button>
	</div>
	<form action="insertAuthorTable.jsp" method="post">
		<table class='tbl-ex tbl' id="teams">
			<tr>
				<td><input type="text" name="author_name" size="20"
					placeholder="저자명"></td>
				<td><input type="text" name="author_desc" size="20"
					placeholder="설명"></td>
			</tr>
			<tr>
				<td colspan=2><input type="submit" value="전송"> <input
					type="reset" value="초기화"></td>
			</tr>
		</table>
	</form>

	<table class='tbl-ex tbl' id="authorListTable">
		<tr>
			<th>저자번호</th>
			<th>저자명</th>
			<th>설명</th>
		</tr>

		<%
  try{
    Class.forName("oracle.jdbc.driver.OracleDriver");
    conn = DriverManager.getConnection(url, user, pass);
    pstmt = conn.prepareStatement(sql);
    rs = pstmt.executeQuery();
    
    while(rs.next()){
      out.println("<tr>");
      out.println("<td class='group-name'>" + rs.getInt("author_id") + "</td>");
      out.println("<td class='group-name'>" + rs.getString("author_name") + "</td>");
      out.println("<td class='group-name'>" + rs.getString("author_desc") + "</td>");
      out.println("</tr>");
    }
  }catch(Exception e){
    e.printStackTrace();
  }finally{
    try{
      if(rs != null) rs.close();
      if(pstmt != null) pstmt.close();
      if(conn != null) conn.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  
  %>
	</table>
</body>
</html>