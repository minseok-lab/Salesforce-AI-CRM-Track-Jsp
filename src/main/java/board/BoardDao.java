package board;

import java.util.List;
import board.BoardVo;

public interface BoardDao {
	public List<BoardVo> getList();                     // 게시물 전체 목록 조회
	public BoardVo getBoard(int no);                    // 게시물 상세 조회
	public int insert(BoardVo vo);                      // 게시물 등록
	public int delete(int no);                          // 게시물 삭제
	public int update(BoardVo vo);                      // 게시물 수정
	public int getTotalCount(String keyWord);           // 게시물 전체 건수 조회
	public List<BoardVo> getList2(int start, int end, String keyWord);  // 게시물 목록 조회(페이징)
	public int upHit(int no);                           // 게시물 조회 건수 증가
}
