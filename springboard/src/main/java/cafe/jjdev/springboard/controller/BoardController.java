package cafe.jjdev.springboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cafe.jjdev.springboard.service.BoardService;
import cafe.jjdev.springboard.vo.Board;

@Controller
public class BoardController {
	@Autowired BoardService boardService;
	
	// 입력 페이지 화면을 요청
	@GetMapping(value="/boardAdd")
	public String boardAdd() {
		// 입력 화면 (C)도착확인
		System.out.println("(C)Get : /boardAdd(입력화면)");
		// 입력화면으로 이동하기 위한 리턴
		return "boardAdd";
	}
	
	// 글 등록을 요청
	@PostMapping(value="/boardAdd")
	public String boardAdd(Board board) {
		// 입력 처리 (C)도착확인
		System.out.println("(C)Post : /boardAdd(입력처리)");
		// Service를 통한 글 등록처리
		boardService.addBoard(board);
		// 등록처리후 글 리스트로 이동하기 위한 리턴
		return "redirect:/boardList";
	}
	
	// 글 리스트을 요청
	@GetMapping(value="/boardList")
	public String boardList(Model model, @RequestParam(value="currentPage", required=false, defaultValue="1") int currentPage) {
		// 글 리스트 화면 (C)도착확인
		System.out.println("(C)Get : /boardList(리스트) 페이지 수 :"+currentPage);
	
		// 전체 리스트 정보를 Service의 selectBoardList메서드를 통해 처리한다.(입력 데이터는 int형의 currentPage)
		model.addAttribute("map", boardService.selectBoardList(currentPage));
		// 현재 페이지의 위치 정보를 사용하기 위해서 model에 할당한다.
		model.addAttribute("currentPage",currentPage);
		// 리스트 화면으로 이동하기 위한 리턴
		return "boardList";
	}
	
	// 글 상세보기
	@GetMapping(value="/boardDetail")
	public String boardDetail(Model model, @RequestParam(value="boardNo", required=false, defaultValue="1") int boardNo, @RequestParam(value="currentPage") int currentPage) {
		// 글 상세보기 화면 (C)도착확인
		System.out.println("(C)Get : /boardDetail(상세보기) 글 No :"+boardNo);
		
		// 글 정보를 Service의 getBoard메서드를 통해 가져와서 model에 할당.
		model.addAttribute("list", boardService.getBoard(boardNo));
		// 현재 페이지의 위치 정보를 사용하기 위해서 model에 할당한다.
		model.addAttribute("currentPage",currentPage);
		// 글 정보들을 가지고 글 상세보기로 이동하기 위한 리턴
		return "boardDetail";
	}
	
	// 글 수정화면
	@GetMapping(value="boardUpdate")
	public String boardUpdate(Model model, @RequestParam(value="boardNo", required=false, defaultValue="1") int boardNo, @RequestParam(value="currentPage") int currentPage) {
		// 글 수정화면 (C)도착확인
		System.out.println("(C)Get : /boardUpdate(수정화면) 글 No :"+boardNo);
		
		// 글 정보를 Service의 getBoard메서드를 통해 가져와서 model에 할당.
		model.addAttribute("list", boardService.getBoard(boardNo));
		// 현재 페이지의 위치 정보를 사용하기 위해서 model에 할당한다.
		model.addAttribute("currentPage",currentPage);
		// 글 수정화면으로 이동하기 위한 리턴
		return "boardUpdate";
	}
	
	// 글 수정처리
	@PostMapping(value="boardUpdate")
	public String boardUpdate(Board board) {
		// 글 수정화면 (C)도착확인
		System.out.println("(C)Post : /boardUpdate(수정처리) 글 No :"+board.getBoardNo());
		// Service를 통한 글 수정처리를 위해서 작성
		boardService.modifyBoard(board);
		// 수정처리후 글 리스트로 이동하기 위한 리턴
		return "redirect:/boardList";
	}
	
	// 글 삭제화면
	@GetMapping(value="boardDelete")
	public String boardDelete(Model model, @RequestParam(value="boardNo", required=false, defaultValue="1") int boardNo, @RequestParam(value="currentPage") int currentPage) {
		// 글 삭제화면 (C)도착확인
		System.out.println("(C)Get : /boardDelete(삭제화면) 글 No :"+boardNo);
		
		// 글 정보를 Service의 getBoard메서드를 통해 가져와서 model에 할당.
		model.addAttribute("list", boardService.getBoard(boardNo));
		// 현재 페이지의 위치 정보를 사용하기 위해서 model에 할당한다.
		model.addAttribute("currentPage",currentPage);
		// 글 삭제화면으로 이동하기 위한 리턴
		return "boardDelete";
	}
	
	// 글 삭제처리
	@PostMapping(value="boardDelete")
	public String boardDelete(Board board, @RequestParam(value="currentPage", required=false, defaultValue="1") int currentPage) {
		// 글 삭제처리 (C)도착확인
		System.out.println("(C)Post : /boardDelete(삭제처리) 글 No :"+board.getBoardNo());
		// Service를 통한 글 삭제처리를 위해서 작성
		boardService.removeBoard(board);
		// 삭제처리후 글 리스트로 이동하기 위한 리턴
		return "redirect:/boardList?currentPage="+ currentPage;
	}

	
}
