package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import board.BoardDao;
import board.BoardDaoImpl;
import util.WebUtil;
import board.BoardVo;
import board.UserVo;

@WebServlet("/board")
public class BoardServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final String ATTACHES_DIR = "C:\\Users\\kimms\\eclipse-workspace\\helloweb\\src\\main\\webapp\\assets";
  private static final int LIMIT_SIZE_BYTES = 1024 * 1024; // 1M

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    String actionName = request.getParameter("a");
    System.out.println("board:" + actionName);

    if ("list".equals(actionName)) {
      // 리스트 가져오기
      BoardDao dao = new BoardDaoImpl();

      String keyWord = "";
      if (request.getParameter("keyWord") != null) {
        keyWord = request.getParameter("keyWord");
      }
      System.out.println("keyWord : >" + keyWord + "<");

      int totalCount = dao.getTotalCount(keyWord);

      System.out.println("컨트롤러 > 게시물 전체 건수 : " + totalCount); // 전체 게시물 건수 출력
      int totalRecord = 0; // 전체레코드수
      int numPerPage = 5; // 페이지당 레코드 수
      int pagePerBlock = 5; // 블럭당 페이지수

      int totalPage = 0; // 전체 페이지 수
      int totalBlock = 0; // 전체 블럭수

      int nowPage = 1; // 현재페이지
      int nowBlock = 1; // 현재블럭

      int start = 0; // 디비의 select 시작번호
      int end = 0; // 시작번호로 부터 가져올 select 갯수

      int listSize = 0; // 현재 읽어온 게시물의 수  

      if (request.getParameter("nowPage") != null) {
        nowPage = Integer.parseInt(request.getParameter("nowPage"));
      }
      start = (nowPage * numPerPage) - numPerPage; // 0
      end = numPerPage; // 5

      totalRecord = totalCount; // 전체 게시물 건수 118
      totalPage = (int) Math.ceil((double) totalRecord / numPerPage); // 전체페이지수 24
      nowBlock = (int) Math.ceil((double) nowPage / pagePerBlock); // 현재블럭 계산 1

      totalBlock = (int) Math.ceil((double) totalPage / pagePerBlock); // 전체블럭계산 5

      int pageStart = (nowBlock - 1) * pagePerBlock + 1; // 하단 페이지 시작번호 1
      int pageEnd = ((pageStart + pagePerBlock) <= totalPage) ? (pageStart + pagePerBlock) - 1 : totalPage; // 5

//			List<BoardVo> list = dao.getList();
      List<BoardVo> list = dao.getList2(start, end, keyWord);

      System.out.println(list);

      // 리스트 화면에 보내기
      request.setAttribute("list", list);

      // 페이징 정보 화면에 보내기(pageStart, pageEnd, nowPage, totalpage, nowBlock)
      request.setAttribute("pageStart", pageStart);
      request.setAttribute("pageEnd", pageEnd);
      request.setAttribute("nowPage", nowPage);
      request.setAttribute("totalPage", totalPage);
      request.setAttribute("nowBlock", nowBlock);
      request.setAttribute("pagePerBlock", pagePerBlock);
      request.setAttribute("totalBlock", totalBlock);
      request.setAttribute("keyWord", keyWord);

      // WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");

      RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/view/board/list.jsp");
      rd.forward(request, response);

    } else if ("read".equals(actionName)) {
      // 게시물 가져오기
      int no = Integer.parseInt(request.getParameter("no"));
      BoardDao dao = new BoardDaoImpl();
      dao.upHit(no); // 조회수 증가
      BoardVo boardVo = dao.getBoard(no); // 게시물 정보 가져오기

      System.out.println(boardVo.toString());

      // 게시물 화면에 보내기
      request.setAttribute("boardVo", boardVo); // 게시물 정보 화면에 전달
      WebUtil.forward(request, response, "/WEB-INF/view/board/read.jsp");
    } else if ("modifyform".equals(actionName)) {
      // 게시물 가져오기
      int no = Integer.parseInt(request.getParameter("no"));
      BoardDao dao = new BoardDaoImpl();
      BoardVo boardVo = dao.getBoard(no);

      // 게시물 화면에 보내기
      request.setAttribute("boardVo", boardVo);
      WebUtil.forward(request, response, "/WEB-INF/view/board/modifyform.jsp");
    } else if ("modify".equals(actionName)) {
      // 게시물 가져오기
      String title = request.getParameter("title");
      String content = request.getParameter("content");
      int no = Integer.parseInt(request.getParameter("no"));

      BoardVo vo = new BoardVo(no, title, content);
      BoardDao dao = new BoardDaoImpl();

      dao.update(vo);

      WebUtil.redirect(request, response, "/helloweb/board?a=list");
    } else if ("writeform".equals(actionName)) {
      // 로그인 여부체크
      UserVo authUser = getAuthUser(request);
      if (authUser != null) { // 로그인했으면 작성페이지로
        WebUtil.forward(request, response, "/WEB-INF/view/board/write.jsp");
      } else { // 로그인 안했으면 리스트로
        WebUtil.redirect(request, response, "/helloweb/board?a=list");
      }

    } else if ("write".equals(actionName)) {
      System.out.printf("/board?a=write");
      // 파일 처리
      UserVo authUser = getAuthUser(request);
      response.setContentType("text/html; charset=UTF-8");
      request.setCharacterEncoding("utf-8");
      // PrintWriter out = response.getWriter();

      File attachesDir = new File(ATTACHES_DIR);

      DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
      // DiskFileItemFactory: 업로드된 파일을 저장할 저장소와 관련된 클래스

      fileItemFactory.setRepository(attachesDir);
      // setRepository()메서드는 업로드 된 파일을 저장할 위치를 File객체로 지정

      fileItemFactory.setSizeThreshold(LIMIT_SIZE_BYTES);
      // setSizeThreshold()메서드는 저장소에 임시파일을 생성할 한계 크기를 byte단위로 지정

      ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
      // ServletFileUpload 클래스: HTTP 요청에 대한 HttpServletRequest 객체로부터
      // multipart/form-data형식으로 넘어온 HTTP Body 부분을 다루기 쉽게 변환(parse)해주는 역할을 수행

      String title = null, content = null, fileName1 = null;
      int userNo = authUser.getNo();

      try {
        List<FileItem> items = fileUpload.parseRequest(request);
        // parseRequest()메서드를 수행시 FileItem이라는 형식으로 변환
        // FileItem: 사용자가 업로드한 File 데이터나 사용자가 input text에 입력한 일반 요청 데이터에 대한 객
        for (FileItem item : items) {
          if (item.isFormField()) { // 리턴 값이 true: text와 같은 일반 입력 데이터, 리턴 값이 false: 파일 데이터
            // 리턴 값이 true인 경우
            System.out.printf("파라미터 명: %s, 파라미터 값: %s\n", item.getFieldName(), item.getString("utf-8"));
            if (item.getFieldName().equals("title")) {
              title = item.getString("utf-8");
            } else if (item.getFieldName().equals("content")) {
              content = item.getString("utf-8");
            }

          } else {
            // 리턴 값이 false인 경우
            System.out.printf("파라미터 명: %s, 파라미터 값: %s, 파일 크기: %s bytes \n", item.getFieldName(), item.getName(),
                item.getSize());
            if (item.getSize() > 0) {
              String separator = File.separator;
              // File.separator: 운영체제별로 다른 파일 경로 구분자
              int index = item.getName().lastIndexOf(separator);
              String fileName = item.getName().substring(index + 1); // c:\DeskTop\abc.jpg
              File uploadFile = new File(ATTACHES_DIR + separator + fileName);
              item.write(uploadFile); // 업로드된 파일을 저장소 디렉터리에 저장

              if (item.getFieldName().equals("file1")) {
                fileName1 = item.getName();
              }
            }
          }
        }
        System.out.println("upload sucess");

      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("upload error");
      }

      BoardVo vo = new BoardVo(title, content, userNo, fileName1);
      BoardDao dao = new BoardDaoImpl();
      dao.insert(vo);

      WebUtil.redirect(request, response, "/helloweb/board?a=list");
    } else if ("delete".equals(actionName)) {
      int no = Integer.parseInt(request.getParameter("no"));

      BoardDao dao = new BoardDaoImpl();
      dao.delete(no);

      WebUtil.redirect(request, response, "/helloweb/board?a=list");

    } else if ("download".equals(actionName)) {
      // 파일 다운로드 처리
      request.setCharacterEncoding("UTF-8");
      
      String fileName = request.getParameter("filename");
      System.out.println(ATTACHES_DIR + File.separator + fileName);
      File file = new File(ATTACHES_DIR + File.separator + fileName);
      if (file.exists()) {
        OutputStream os = null;
        FileInputStream is = null;
        
        fileName = URLEncoder.encode(fileName, "UTF-8");
        
        response.setContentType( "application/download; UTF-8" );
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ";");
        response.setHeader("Content-Type", "application/octet-stream");                
        response.setHeader("Content-Transfer-Encoding", "binary;");
        response.setHeader("Pragma", "no-cache;");
        response.setHeader("Expires", "-1;");
        
        try {
          os = response.getOutputStream();
          is = new FileInputStream(file);
          int su = 0;
          while ((su = is.read()) != -1) {
            os.write(su);
          }
          System.out.println("down");
        } catch (IOException e) {
          System.out.println("error");
          e.printStackTrace();
        } finally {
          try {
            if (os != null) { os.close(); }
            if (is != null) { is.close(); }
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

    } else {
      WebUtil.redirect(request, response, "/helloweb/board?a=list");
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }

  // 로그인 되어 있는 정보를 가져온다.
  protected UserVo getAuthUser(HttpServletRequest request) {
    HttpSession session = request.getSession();
    UserVo authUser = (UserVo) session.getAttribute("authUser");

    return authUser;
  }

}
