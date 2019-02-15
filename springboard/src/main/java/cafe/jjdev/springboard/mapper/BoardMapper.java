package cafe.jjdev.springboard.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import cafe.jjdev.springboard.vo.Board;

@Mapper
public interface BoardMapper {
	
	int insertBoard(Board board);
	/*
	 *  1. 글 등록 처리 insertBoard()
	 *   Service의 addBoard()
	 */
	
	Board selectBoard(int boardNo);
	/*
	 *  2. 글 수정화면 및 상세보기 selectBoard()
	 *   Service의 getBoard()
	 */
	
	List<Board> selectBoardList(Map<String, Integer> map);
	/*
	 *  3. 글 리스트 selectBoardList()
	 *   Service의 selectBoardList()
	 */
	
	int selectBoardCount();
	/*
	 *  4. 글의 갯수 selectBoardCount()
	 *   Service의 getBoardCount()
	 */
	
	int updateBoard(Board board);
	/*
	 *  5. 글 수정 처리 updateBoard()
	 *   Service의 modifyBoard()
	 */
	
	int deleteBoard(Board board);
	/*
	 *  6. 글 삭제 처리 deleteBoard()
	 *   Service의 removeBoard()
	 */
}