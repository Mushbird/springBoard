package cafe.jjdev.springboard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cafe.jjdev.springboard.mapper.BoardMapper;
import cafe.jjdev.springboard.vo.Board;

@Service
@Transactional	// 하나라도 실패시 실행중단
public class BoardService {
	@Autowired private BoardMapper boardMapper;
	
	public int addBoard(Board board) {
	/*
	 * 	1. 글 등록처리 addBoard()
	 * 	입력 매개변수 : Board타입 board
	 * 	리턴 타입 : int
	 *  Mapper의 insertBoard()
	 */
		return boardMapper.insertBoard(board);
	}
	
	public int modifyBoard(Board board) {
	/*
	 * 	2. 글 수정처리 modifyBoard()
	 * 	입력 매개변수 : Board타입 board 
	 * 	리턴 타입 : int
	 *  Mapper의 updateBoard()
	 */
		return boardMapper.updateBoard(board);
	}

	public int removeBoard(Board board) {
	/*
	 * 	3. 글 삭제처리 removeBoard()
	 * 	입력 매개변수 : Board타입 board 
	 * 	리턴 타입 : int(성공 1, 실패 0)
	 *  Mapper의 selectBoard(), deleteBoard()
	 */
		
		// 1. 입력 받은 비밀번호와 DB비밀번호가 일치작업을 위한 DB글 정보 가져오기
		
		// 받아온 board 참조변수에 담긴 주소값을 찾아가서 현재 해당되는 글의 번호를 가져온다.
		String boardPw = board.getBoardPw();
		// DB에 있는 글 정보를 리턴으로 넘기기 위해 Board객체를 사용(객체생성)
		Board dbBoard = new Board();
		// Mapper를 통해 쿼리 처리후 DB에 있는 글 정보 가져오기
		dbBoard = boardMapper.selectBoard(board.getBoardNo());
		String dbBoardPw = dbBoard.getBoardPw();
		// 입력받은 PW와 DB에서 가져온 PW값을 잘 받았는지 확인
		System.out.println("입력Pw :" +boardPw);
		System.out.println("dbPw :" +dbBoardPw);
		
		// 2. IF문을 통한 입력한 비밀번호와 DB 비밀번호 일치 확인
		if(boardPw.equals(dbBoardPw)) {
			
			// 3. 일치한다면 삭제처리 후 리턴
			int result = boardMapper.deleteBoard(board);
			System.out.println("삭제 처리 완료 / 비밀번호 일치");
			return result;
		}else {
			
			// 4. 일치하지 않는다면 삭제처리하지 않고 리턴
			int result = 0;
			System.out.println("삭제 처리 불가 / 비밀번호 불일치");
			return result;
		}
	}
	
	public Board getBoard(int boardNo) {
	/*
	 * 	4. 수정화면 및 상세보기 getBoard()
	 * 	입력 매개변수 : int타입 boardNo 
	 * 	리턴 타입 : Board
	 * 	Mapper의 selectBoard()
	 */
		
		// 글 정보들을 리턴으로 넘기기 위해 Board객체를 사용(객체생성)
		Board board = new Board(); 
		// Mapper를 통해 쿼리 처리후 글 정보를 board에 담기	
		board = boardMapper.selectBoard(boardNo);
		
		// 리턴 데이터는 글정보가 담긴 board 참조변수
		return board;
	}
	
	public int getBoardCount() {
	/*
	 * 	5. 글의 갯수 getBoardCount()
	 * 	입력 매개변수 : 없음
	 * 	리턴 타입 : int
	 *  Mapper의 selectBoardCount(), selectBoardList()
	 */
		return boardMapper.selectBoardCount();	
	}
	
	public Map<String, Object> selectBoardList(int currentPage) {
	/*
	 * 	6. 글 리스트 조회 selectBoardList()
	 * 	입력 매개변수 : int타입 currentPage
	 * 	리턴 타입 : Map<String, Object>
	 *  Mapper의 selectBoardCount(), selectBoardList()
	 */
		
		// 메서드 호출이 되면 출력문을 통한 확인
		/* System.out.println("Service의 selectBoardList 시작!"); */ 
		
		// 한페이지에 10개의 글을 보여주기 위해 상수로 선언
		final int ROW_PER_PAGE = 10;
		// 현재 페이지 -1 * 10개의 글의 수에 대한 계산값을 변수에 담는다.
		int calPage = (currentPage-1)*ROW_PER_PAGE;
		// Map을 사용하기 위해 생성자로 객체 생성과 참조변수 선언
		Map<String, Integer> map = new HashMap<String, Integer>();
		// map참조변수에 할당
		map.put("currentPage", currentPage);
		map.put("rowPerPage", ROW_PER_PAGE);
		map.put("calPage", calPage);
		
		// 글의 갯수 정보 가져오기(Mapper를 통한 쿼리문 실행)
		int boardCount = boardMapper.selectBoardCount();
		/* System.out.println("Service의 selectBoardList / selectBoardCount() 완료!"); */
		
		// 마지막 페이지수의 정보를 lastPage에 담는다. (ceil은 올림 함수)
		int lastPage = (int)((Math.ceil(boardCount/ROW_PER_PAGE))+1);
		/* System.out.println("Service의 selectBoardList / lastPage 계산 완료!"); */
		
		// 리스트에 관련된 값들을 리턴으로 넘기기 위해 Map을 사용(객체생성)
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 글의 정보와 리스트 갯수, 마지막 페이지수를 참조변수 returnMap에 할당
		returnMap.put("list", boardMapper.selectBoardList(map));
		/* System.out.println("Service의 selectBoardList / selectBoardList(map) 결과 세팅완료 !"); */
		
		returnMap.put("boardCount", boardCount);
		returnMap.put("lastPage", lastPage);
		// 모두 완료되었으면 출력문을 통한 확인
		/* System.out.println("Service의 selectBoardList 끝!"); */
		
		// 리턴 데이터는 한 페이지에 보여줄 'ROW_PER_PAGE'(10)개의 글의 정보
		return returnMap;
	}
}