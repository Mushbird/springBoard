package cafe.jjdev.springboard.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cafe.jjdev.springboard.mapper.BoardFileMapper;
import cafe.jjdev.springboard.mapper.BoardMapper;
import cafe.jjdev.springboard.vo.Board;
import cafe.jjdev.springboard.vo.BoardRequest;
import cafe.jjdev.springboard.vo.Boardfile;

@Service
@Transactional	// 하나라도 실패시 실행중단
public class BoardService {
	@Autowired private BoardMapper boardMapper;
	@Autowired private BoardFileMapper boardFileMapper;
	
	public void insertBoard(BoardRequest boardRequest, String path) throws IllegalStateException, IOException {
	/*
	 * 	1. 글 등록처리(파일추가) insertBoard()
	 * 	입력 매개변수 : BoardRequest타입의 boardRequest, String타입의 path
	 * 	리턴 타입 : 없음
	 *  Mapper의 insertBoard()와 insertBoardRequest()
	 */
		
	 //	BoardRequest를 분리 : board, file, file 정보
		System.out.println("BoardRequest 분리 시작!");
		System.out.println("board 처리 시작!");
	 //	1. board -> board(Vo)
		Board board = new Board();
		board.setBoardTitle(boardRequest.getBoardTitle());
		board.setBoardPw(boardRequest.getBoardPw());
		board.setBoardContent(boardRequest.getBoardContent());
		board.setBoardUser(boardRequest.getBoardUser());
		
	 // 서브쿼리 실행후 insert된 쿼리의 No를 얻을수 있다.
		boardMapper.insertBoard(board);
		
		System.out.println("board 처리완료 / boardNo Get !");
		System.out.println("file 처리시작 !");
		System.out.println("boardNo"+board.getBoardNo());
	 //	2. file정보 -> boardFile(Vo)
		
	 // 받아온 파일들을 List로 저장
		List<MultipartFile> files = boardRequest.getFiles();
	 // 반복문을 통한 첨부된 파일 저장
		System.out.println("file / For문 처리시작 !");
		// if(files != null) {
			for(MultipartFile f : files) {
				System.out.println("파일 정보 추출 시작 !");
				// f -> boardfile
				Boardfile boardfile = new Boardfile();
				 // 얻어온 No 세팅
				boardfile.setBoardNo(board.getBoardNo());
				 // 자동 글자 생성(16진수)
				String fileName = UUID.randomUUID().toString();
				System.out.println("자동 글자 :"+fileName);
				 // 자동생성된 파일 이름으로 저장
				 // 각 요소별 세팅
				boardfile.setFileName(fileName);
				boardfile.setFileType(f.getContentType());
				System.out.println("타입 :"+f.getContentType());
				boardfile.setFileSize(f.getSize());
				System.out.println("사이즈 :"+f.getSize());
				 // 확장자를 구하기 위해 파일이름 자르고 세팅
				String originalFileName = f.getOriginalFilename();
				int i = originalFileName.lastIndexOf(".");
				String ext = originalFileName.substring(i+1);
				System.out.println("확장자 :"+ext);
				boardfile.setFileExt(ext);
				
				// 3. file -> path를 이용해서 물리적 장치 저장
				System.out.println("파일 물리적 저장 시작 !");
				f.transferTo(new File(path+"/"+fileName+"."+ext));
				System.out.println("파일 물리적 저장 완료 !");
				System.out.println("파일 정보 추출 완료 !");
				 // 파일정보 쿼리를 통해 등록
				boardFileMapper.insertBoardFile(boardfile);
			}	
		// }
		System.out.println("file / For문 처리완료 !");
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