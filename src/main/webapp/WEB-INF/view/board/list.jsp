<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="/helloweb/assets/css/board.css" rel="stylesheet" type="text/css">
<title>Mysite</title>

<script
  src="https://code.jquery.com/jquery-3.6.3.js"
  integrity="sha256-nQLuAZGRRcILA+6dMBOvcRh5Pe310sBpanc6+QBmyVM="
  crossorigin="anonymous"></script>
  
<script type="text/javascript">
  function list() {
    document.listFrm.action = "list.jsp";
    document.listFrm.submit();
  }
  
  function pageing(page) {
    //alert(page)
    document.readFrm.nowPage.value = page;
    document.readFrm.submit();
  }
  
  function block(value){  // 3
     document.readFrm.nowPage.value=  (${pagePerBlock} * (value -1))+1; // 11
     document.readFrm.submit();
  } 
  
  function read(num){
    document.readFrm.num.value=num;
    document.readFrm.action="read.jsp";
    document.readFrm.submit();
  }
  
  function check() {
     //alert( $('#keyWord').val() );
     if ($('#keyWord').val()  == "") {
      alert("검색어를 입력하세요.");
      $('#keyWord').focus();
      return;
     }
     $('#search_form').submit();
   }
</script>
</head>
<body>
	<div id="container">
		
		<c:import url="/WEB-INF/view/includes/header.jsp"></c:import>
		<c:import url="/WEB-INF/view/includes/navigation.jsp"></c:import>
		
		<div id="content">
			<div id="board">
        
				<form id="search_form" action="board" method="get">
					<input type="text" id="keyWord" name="keyWord" value="${keyWord}">
          <input type="hidden" name="a" value="list">
          <input type="hidden" name="nowPage" value="1">
					<input type="button" value="찾기" onClick="javascript:check()">
				</form>
        
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>				
					<c:forEach items="${list }" var="vo">
						<tr>
							<td>${vo.no }</td>
							<td><a href="/helloweb/board?a=read&no=${vo.no }">${vo.title }</a></td>
							<td>${vo.userName }</td>
							<td>${vo.hit }</td>
							<td>${vo.regDate }</td>
							<td>
								<c:if test="${authUser.no == vo.userNo }">
									<a href="/helloweb/board?a=delete&no=${vo.no }" class="del">삭제</a>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</table>

        <!-- 페이징 정보 출력 
             pageStart: ${pageStart } pageEnd: ${pageEnd } 
             totalBlock: ${totalBlock} nowBlock: ${nowBlock} -->
				<div class="pager">
					<ul>
          
          <c:if test="${totalPage ne 0}">
            <c:if test="${nowBlock > 1}">
              <a href="javascript:block('${nowBlock -1}')">◀</a>
            </c:if>
          </c:if>
          
          <c:forEach var="i" begin="${pageStart }" end="${pageEnd }" step="1">
            <c:if test="${nowPage eq i}">
            <li class="selected"><a href="javascript:pageing('${i}')">${i}</a></li>
            </c:if>
            
            <c:if test="${nowPage ne i}">
            <li><a href="javascript:pageing('${i}')">${i}</a></li>
            </c:if>
          </c:forEach>
          
          <c:if test="${totalBlock > nowBlock}">
              <a href="javascript:block('${nowBlock +1}')">▶</a>
          </c:if>

					</ul>
				</div>
        <!-- /페이징 정보 출력 -->
				<c:if test="${authUser != null }">
					<div class="bottom">
						<a href="/helloweb/board?a=writeform" id="new-book">글쓰기</a>
					</div>
				</c:if>				
			</div>
		</div>
		
		<c:import url="/WEB-INF/view/includes/footer.jsp"></c:import>
		
	</div><!-- /container -->
  
  
  
  <form name="readFrm" method="get" action="board">
    <input type="hidden" name="a" value="list"> 
    <input type="hidden" name="num"> 
    <input type="hidden" name="nowPage" value="">
    <input type="hidden" name="keyWord" value="${keyWord}">
  </form>
  
</body>
</html>		
		
