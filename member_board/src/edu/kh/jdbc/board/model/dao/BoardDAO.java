package edu.kh.jdbc.board.model.dao;

import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.kh.jdbc.board.model.vo.Board;
import edu.kh.jdbc.board.model.vo.Comment;

public class BoardDAO {
	
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;

	private Properties prop;
	
	public BoardDAO() {  //기본생성자
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream("board-query.xml"));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	
	/** 게시글 목록 조회 DAO
	 * @param conn
	 * @return boardList
	 * @throws Exception
	 */
	public List<Board> selectAllBoard(Connection conn) throws Exception{
		// 결과 저장용 변수 선언    _-> NULL이 나올 수가 없다.
		List<Board> boardList = new ArrayList<>();
		
		
		try {
			// SQL~
			String sql = prop.getProperty("selectAllBoard");
			
			// Statement~
			stmt = conn.createStatement();
			
			// SQL 수행 후 ~ ResultSet~
			rs = stmt.executeQuery(sql);
			
			// ResultSet에 저장된 값을 List 옮겨 담기
			while(rs.next()) {  //_만약에 다음 행에 존재한다면?
				
				// BOARD_NO, BOARD_TITLE, MEMBER_NM, READ_COUNT, CREATE_DT, COMMENT_COUNT
				
				int boardNo = rs.getInt("BOARD_NO");
				String boardTitle = rs.getString("BOARD_TITLE");
				String memberName = rs.getString("MEMBER_NM");
				int readCount = rs.getInt("READ_COUNT");
				String createDate = rs.getString("CREATE_DT");
				int commentCount = rs.getInt("COMMENT_COUNT");
				
				
				Board board = new Board();
				board.setBoardNo(boardNo);
				board.setBoardTitle(boardTitle);
				board.setMemberName(memberName);
				board.setReadCount(readCount);
				board.setCreateDate(createDate);
				board.setCommentCount(commentCount);
				
				boardList.add(board);
				
			}
			
		} finally {
			close(rs);
			close(stmt);
			
		}
		// 조회 결과 반환
		return boardList;
	}



	/** 게시글 상세 조회 DAO
	 * @param conn
	 * @param boardNo
	 * @return board
	 * @throws Exception
	 */
	public Board selectBoard(Connection conn, int boardNo) throws Exception{
			//_ 반환형 Board 객체는 결과로 null이 나올 수 있음. cf. List
		
		// 결과 저장용 변수 선언  
		Board board = null;
		
		try {
			String sql = prop.getProperty("selectBoard");  // SQL 얻어오기
			
			pstmt = conn.prepareStatement(sql);  // PreparedStatement 생성
			pstmt.setInt(1, boardNo); // ?에 알맞은 값 대입
			
			rs = pstmt.executeQuery(); // SQL(SELECT) 수행 후 결과(ResultSet) 반환 받기
			
			//BOARD_NO, BOARD_TITLE, BOARD_CONTENT, 
			//   MEMBER_NO, MEMBER_NM, READ_COUNT, CREATE_DT
			
			if(rs.next()) { // 조회 결과가 있을 경우
				
				board = new Board(); // Board 객체 생성 == board는 null이 아님
				board.setBoardNo(rs.getInt("BOARD_NO"));
				board.setBoardTitle(rs.getString("BOARD_TITLE"));
				board.setBoardContent(rs.getString("BOARD_CONTENT"));
				board.setMemberNo(rs.getInt("MEMBER_NO"));
				board.setMemberName(rs.getString("MEMBER_NM"));
				board.setReadCount(rs.getInt("READ_COUNT"));
				board.setCreateDate(rs.getString("CREATE_DT"));
				
				//같은 형태
				// board.setBoardNo(rs.getInt("BOARD_NO")) 
				//  == int boardNo = rs.getInt("BOARD_NO")
				//	   board.setBoardNo(boardNo);
			}
		} finally {
			close(rs);
			close(pstmt);
			
		}
		return board;  // 조회 결과 반환
	}



	
	/** 조회 수 증가 DAO
	 * @param conn
	 * @param boardNo
	 * @return result
	 * @throws Exception
	 */
	public int increaseReadCount(Connection conn, int boardNo) throws Exception {
		
		int result = 0;
		
		try {
			String sql = prop.getProperty("increaseReadCount");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
		    result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
			
		}
		
		return result;
	}



	/** 게시판 수정 DAO
	 * @param conn
	 * @param board
	 * @return result
	 * @throws Exception
	 */
	public int updateBoard(Connection conn, Board board) throws Exception{
		int result = 0;
		
		try {
			
			String sql = prop.getProperty("updateBoard");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, board.getBoardTitle());
			pstmt.setString(2, board.getBoardContent());
			pstmt.setInt(3, board.getBoardNo());
			
			result = pstmt.executeUpdate();

		} finally {
			close(pstmt);
			
		}
		return result;
	}



	/** 게시글 삭제 DAO
	 * @param conn
	 * @param boardNo
	 * @return result
	 * @throws Exception
	 */
	public int deleteBoard(Connection conn, int boardNo) throws Exception {
		int result = 0;
		
		try {
			String sql = prop.getProperty("deleteBoard");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
			
		}
		return result;
	}



	/** 게시글 등록 DAO
	 * @param conn
	 * @param board
	 * @return result
	 * @throws Exception
	 */
	public int insertBoard(Connection conn, Board board) throws Exception {
		
		int result = 0;
		//_ try 안에 result를 선언하면 지역변수가 되어서 return 반환에 문제가 생김.
		
		//_ try~finally 왜 쓰는가. try가 예외가 발생할 것 같은 구문을 작성할 때 사용.
		// 
		try {
			String sql = prop.getProperty("insertBoard");
			// Properties 는 key, value가 <String, String>으로 제한됨.
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, board.getBoardNo());
			
			pstmt.setString(2, board.getBoardTitle());
			pstmt.setString(3, board.getBoardContent());
			pstmt.setInt(4, board.getMemberNo());
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
			
		}
		
		return result;
	}



	/** 다음 게시글 번호 생성 DAO
	 * @param conn
	 * @return boardNo
	 * @throws Exception
	 */
	public int nextBoardNo(Connection conn) throws Exception{
		int boardNo = 0;   //result 아님
		
		try {
			String sql = prop.getProperty("nextBoardNo");
			
			//_ stmt / pstmt 모두 쓸 수 있음. (? 없어도)
			pstmt = conn.prepareStatement(sql);  //_ ? 없으면 아래 set구문 작성 안하면 됨.
			
			rs = pstmt.executeQuery();   //SELECT 수행 -> ResultSet으로 가져오기.
			
			if(rs.next()) { // 조회 결과 1행 밖에 없음
				boardNo = rs.getInt(1); // 첫 번째 컬럼값을 얻어와 boardNo에 저장
			}
			
		} finally {
			close(rs);
			close(pstmt);
			
		}
		return boardNo;
	}



	/** 게시글 검색 DAO
	 * @param conn
	 * @param condition
	 * @param query
	 * @return boardList
	 * @throws Exception
	 */
	public List<Board> searchBoard(Connection conn, int condition, String query) throws Exception {
		
		List<Board> boardList = new ArrayList<>(); //_다형성 제너릭스. 타입 추론.
		
		try{
			String sql = prop.getProperty("searchBoard1")
					 	+ prop.getProperty("searchBoard2_" + condition)
					 	+ prop.getProperty("searchBoard3");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, query);  //검색어
			
			// 3번(제목+내용)은 ?가 2개 존재하기 때문에 추가 세팅 구문 작성해야 함.
			if(condition == 3) {
				pstmt.setString(2, query);
			}

			rs = pstmt.executeQuery();
			
			// ResultSet에 저장된 값을 List 옮겨 담기
			while(rs.next()) {  //_만약에 다음 행에 존재한다면?
				
				// BOARD_NO, BOARD_TITLE, MEMBER_NM, READ_COUNT, CREATE_DT, COMMENT_COUNT
				
				int boardNo = rs.getInt("BOARD_NO");
				String boardTitle = rs.getString("BOARD_TITLE");
				String memberName = rs.getString("MEMBER_NM");
				int readCount = rs.getInt("READ_COUNT");
				String createDate = rs.getString("CREATE_DT");
				int commentCount = rs.getInt("COMMENT_COUNT");
				
				
				Board board = new Board();
				board.setBoardNo(boardNo);
				board.setBoardTitle(boardTitle);
				board.setMemberName(memberName);
				board.setReadCount(readCount);
				board.setCreateDate(createDate);
				board.setCommentCount(commentCount);
				
				boardList.add(board);
				
			}
			
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return boardList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}













