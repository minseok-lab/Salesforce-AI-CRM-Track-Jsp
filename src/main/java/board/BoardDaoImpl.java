package board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DBConnectionMgr;
import board.BoardVo;

public class BoardDaoImpl implements BoardDao {
  
  private DBConnectionMgr pool;

  public BoardDaoImpl() {
    try {
      pool = DBConnectionMgr.getInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
	public List<BoardVo> getList() {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVo> list = new ArrayList<BoardVo>();

		try {
			conn = pool.getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "select b.no, b.title, b.hit, b.reg_date, b.user_no, u.name "
					     + " from board b, users u "
					     + " where b.user_no = u.no "
					     + " order by no desc";
			
			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");
				
				BoardVo vo = new BoardVo(no, title, hit, regDate, userNo, userName);
				list.add(vo);
			}
			
		} catch (Exception e) {
			System.out.println("error:" + e);
		} finally {
      pool.freeConnection(conn, pstmt, rs);
    }
		
		return list;

	}

	
	public BoardVo getBoard(int no) {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVo boardVo = null;
		
		try {
		  conn = pool.getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "select b.no, b.title, b.content, b.hit, b.reg_date, b.user_no, u.name, b.filename1 "
					         + "from board b, users u "
					         + "where b.user_no = u.no "
					         + "and b.no = ?";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			
			rs = pstmt.executeQuery();
			// 4.결과처리
			if(rs.next()) {
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");
				String fileName1 = rs.getString("filename1");
				
				//boardVo = new BoardVo(no, title, content, hit, regDate, userNo, userName, fileName1);
				boardVo = new BoardVo();
				boardVo.setNo(no);
				boardVo.setTitle(title);
				boardVo.setContent(content);
				boardVo.setHit(hit);
				boardVo.setRegDate(regDate);
				boardVo.setUserNo(userNo);
				boardVo.setUserName(userName);
				boardVo.setFileName1(fileName1);
			}
			
		} catch (Exception e) {
			System.out.println("error:" + e);
		} finally {
      pool.freeConnection(conn, pstmt, rs);
    }
		System.out.println("BoardDaoImpl.getBoard() -> " + boardVo);
		return boardVo;

	}
	
	public int insert(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = pool.getConnection();
		  
		  System.out.println("vo.userNo : ["+vo.getUserNo()+"]");
      System.out.println("vo.title : ["+vo.getTitle()+"]");
      System.out.println("vo.content : ["+vo.getContent()+"]");
      System.out.println("vo.filename1 : ["+vo.getFileName1()+"]");
      
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "insert into board values (seq_board_no.nextval, ?, ?, 0, sysdate, ?, ?)";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setInt(3, vo.getUserNo());
			pstmt.setString(4, vo.getFileName1());
      
			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 등록");

		} catch (Exception e) {
			System.out.println("error:" + e);
		} finally {
      pool.freeConnection(conn, pstmt);
    }

		return count;
	}
	
	
	public int delete(int no) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = pool.getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "delete from board where no = ?";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 삭제");

		} catch (Exception e) {
			System.out.println("error:" + e);
		} finally {
      pool.freeConnection(conn, pstmt);
    }

		return count;
	}
	
	
	public int update(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = pool.getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "update board set title = ?, content = ? where no = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setInt(3, vo.getNo());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 수정");

		} catch (Exception e) {
			System.out.println("error:" + e);
		} finally {
      pool.freeConnection(conn, pstmt);
    }

		return count;
	}

  @Override
  public int getTotalCount(String keyWord) {
    // 0. import java.sql.*;
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    int count = 0;
    
    try {
      conn = pool.getConnection();
      
      String query = "";
      // 3. SQL문 준비 / 바인딩 / 실행
      if("".equals(keyWord) || keyWord == null) {
        query = "select count(*) from board";
        pstmt = conn.prepareStatement(query);
      }else {
        query = " SELECT count(*) "
              + " FROM board b, USERS u "
              + " WHERE b.USER_NO = u.NO"
              + " AND b.TITLE || b.CONTENT || u.NAME || TO_CHAR(b.REG_DATE, 'YYYY-MM-DD') LIKE ? ";
        pstmt = conn.prepareStatement(query);
        pstmt.setString(1, "%"+keyWord+"%");
      }
      
      rs = pstmt.executeQuery();
      // 4.결과처리
      if (rs.next()) {
        count = rs.getInt(1);
      }
      
    } catch (Exception e) {
      System.out.println("error:" + e);
    } finally {
      pool.freeConnection(conn, pstmt, rs);
    }
    System.out.println("Dao > 게시물 전체 건수 : " + count);
    return count;

  }

  @Override
  public List<BoardVo> getList2(int start, int end, String keyWord) {

    // 0. import java.sql.*;
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<BoardVo> list = new ArrayList<BoardVo>();

    try {
      conn = pool.getConnection();

      // 3. SQL문 준비 / 바인딩 / 실행
      String query = "";
      // 3. SQL문 준비 / 바인딩 / 실행
      if("".equals(keyWord) || keyWord == null) {
        query = " SELECT * \r\n"
            + "   FROM(\r\n"
            + "        SELECT ROWNUM AS RNUM, A.*\r\n"
            + "          FROM ( select b.no, b.title, b.hit, b.reg_date, b.user_no, u.name from board b, users u WHERE b.USER_NO = u.NO order by b.NO DESC ) A\r\n"
            + "         WHERE ROWNUM <= ?+?\r\n"
            + "            )\r\n"
            + "  WHERE RNUM > ?";
        pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, start);
        pstmt.setInt(2, end);
        pstmt.setInt(3, start);
      }else {
        query = "  SELECT * \r\n"
            + "   FROM(\r\n"
            + "        SELECT ROWNUM AS RNUM, A.*\r\n"
            + "          FROM ( select b.no, b.title, b.hit, b.reg_date, b.user_no, u.name \r\n"
            + "         from board b, users u \r\n"
            + "         WHERE b.USER_NO = u.NO \r\n"
            + "         AND   b.TITLE || b.CONTENT || u.NAME || TO_CHAR( b.REG_DATE, 'YYYY-MM-DD')  LIKE ?\r\n"
            + "         order by b.NO DESC ) A\r\n"
            + "         WHERE ROWNUM <= ?+?\r\n"
            + "            )\r\n"
            + "  WHERE RNUM > ?";
        pstmt = conn.prepareStatement(query);
        pstmt.setString(1, "%"+keyWord+"%");
        pstmt.setInt(2, start);
        pstmt.setInt(3, end);
        pstmt.setInt(4, start);
      }

      rs = pstmt.executeQuery();
      // 4.결과처리
      while (rs.next()) {
        int no = rs.getInt("no");
        String title = rs.getString("title");
        int hit = rs.getInt("hit");
        String regDate = rs.getString("reg_date");
        int userNo = rs.getInt("user_no");
        String userName = rs.getString("name");
        
        BoardVo vo = new BoardVo(no, title, hit, regDate, userNo, userName);
        list.add(vo);
      }
      
    } catch (Exception e) {
      System.out.println("error:" + e);
    } finally {
      pool.freeConnection(conn, pstmt, rs);
    }
    return list;
  }

  @Override
  public int upHit(int no) {
    // 0. import java.sql.*;
    Connection conn = null;
    PreparedStatement pstmt = null;
    int count = 0;

    try {
      conn = pool.getConnection();

      // 3. SQL문 준비 / 바인딩 / 실행
      String query = "update board set hit = hit + 1 where no = ?";
      pstmt = conn.prepareStatement(query);

      pstmt.setInt(1, no);

      count = pstmt.executeUpdate();

      // 4.결과처리
      System.out.println(no + "게시물 조회 건수 증가");

    } catch (Exception e) {
      System.out.println("error:" + e);
    } finally {
      pool.freeConnection(conn, pstmt);
    }

    return count;
  }
	
}
