package edu.kh.jdbc.board.model.vo;

import java.util.List;

// 게시글 1개 정보를 저장하는 VO
/**
 * @author user1
 *
 */
public class Board { 

	private int boardNo;         // 게시글 번호
	private String boardTitle;   // 게시글 제목
	private String boardContent; // 게시글 내용
	private String createDate;   // 작성일
	private int readCount;	     // 조회수
	private int memberNo;        // 작성자 회원 번호
	private String memberName;   // 작성자 회원 이름
	private int commentCount;    // 댓글 수
	
	//_삭제 여부는 SELECT절에는 사용 안 해서 굳이 적지 않음. 필요하면 적으면 됨.
	//_private int deleteFlag 필요한 것만 적으면 됨.
	
	private List<Comment> commentList;  // 댓글 목록 _게시글 하나에 댓글이 여러개이기 떄문
	
	
	public Board() {}

	

	public int getBoardNo() {
		return boardNo;
	}


	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}


	public String getBoardTitle() {
		return boardTitle;
	}


	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}


	public String getBoardContent() {
		return boardContent;
	}


	public void setBoardContent(String boardContent) {
		this.boardContent = boardContent;
	}


	public String getCreateDate() {
		return createDate;
	}


	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}


	public int getReadCount() {
		return readCount;
	}


	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}


	public int getMemberNo() {
		return memberNo;
	}


	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}


	public String getMemberName() {
		return memberName;
	}


	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}


	public int getCommentCount() {
		return commentCount;
	}


	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}


	public List<Comment> getCommentList() {
		return commentList;
	}


	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
	
	
}
