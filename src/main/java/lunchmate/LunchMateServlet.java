package lunchmate;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher; // JSP로 포워딩
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet; // URL 매핑
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// [핵심] @WebServlet 어노테이션
// 브라우저에서 /randomLunch URL 요청이 오면 이 서블릿이 실행됩니다.
@WebServlet({ "/randomLunch", "/randomLunch/" })
public class LunchMateServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	// GET 방식 요청을 처리하는 메서드
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("--- LunchMateServlet.doGet() 메서드 실행됨! ---");

		// 1. (M) Model 객체 생성 및 로직 호출
		LunchMate mateGenerator = new LunchMate();
		List<List<String>> lunchGroups = mateGenerator.generateLunchGroups();

		// 2. View(JSP)에 전달할 데이터를 request 객체에 "속성"으로 저장
		// "groups"라는 이름표를 붙여서 조 편성 결과를 담습니다.
		request.setAttribute("lunchGroups", lunchGroups);

		// 3. (V) View로 요청을 전달(forward)
		// [중요] JSP 파일의 새 위치를 지정합니다.
		// WEB-INF 폴더 안으로 JSP를 이동시켜서, 사용자가 URL로 직접 접근하는 것을 막습니다. (보안 강화)
		String jspPath = "/WEB-INF/view/lunchMateResult.jsp"; // JSP 파일의 새 경로
		RequestDispatcher dispatcher = request.getRequestDispatcher(jspPath);
		dispatcher.forward(request, response);
	}
}