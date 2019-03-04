package cafe.jjdev.springboard.vo;

import org.springframework.stereotype.Component;

@Component
public class Boardfile {
	
	private int boardFileNo;
	private int boardNo;
	private String fileName;
	private String fileExt;
	private String fileType;
	private long fileSize;
	
	public int getBoardFileNo() {
		return boardFileNo;
	}
	public void setBoardFileNo(int boardFileNo) {
		this.boardFileNo = boardFileNo;
	}
	public int getBoardNo() {
		return boardNo;
	}
	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileExt() {
		return fileExt;
	}
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	
	@Override
	public String toString() {
		return "Boardfile [boardFileNo=" + boardFileNo + ", boardNo=" + boardNo + ", fileName=" + fileName
				+ ", fileExt=" + fileExt + ", fileType=" + fileType + ", fileSize=" + fileSize + "]";
	}
	
	
}
