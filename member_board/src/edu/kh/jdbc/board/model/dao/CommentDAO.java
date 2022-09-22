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

public class CommentDAO {

	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;

	private Properties prop;
	
	public CommentDAO() {
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream("comment-query.xml"));
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	
	
	/** 댓글 목록 조회 DAO
	 * @param conn
	 * @param boardNo
	 * @return commentList
	 * @throws Exception
	 */
	public List<Comment> selectCommentList(Connection conn, int boardNo) throws Exception{
		
		List<Comment> commentList = new ArrayList<>();
		
		try {
			String sql = prop.getProperty("selectCommentList");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			
			rs = pstmt.executeQuery();  // SELECT
			
			//COMMENT_NO, COMMENT_CONTENT,
			//   MEMBER_NO, MEMBER_NM,  CREATE_DT
			
			while(rs.next()) {
				
				Comment comment = new Comment();
				
				// 컬럼 순서로 얻어오는 것도 가능
				comment.setCommentNo(rs.getInt(1));
				comment.setCommentContent(rs.getString(2));
				comment.setMemberNo(rs.getInt(3));
				comment.setMemberName(rs.getString(4));
				comment.setCreateDate(rs.getString(5));
				
				// ==
//				comment.setCommentNo(rs.getInt("COMMENT_NO"));
//				comment.setCommentContent(rs.getString("COMMNET_CONTENT"));
//				comment.setMemberNo(rs.getInt("MEMBER_NO"));
//				comment.setMemberName(rs.getString("MEMBER_NM"));
//				comment.setCreateDate(rs.getString("CREATE_DT"));
				
				commentList.add(comment);
				
			}
		} finally {
			close(rs);
			close(pstmt);
			
		}
		
		return commentList;
	}
	
}
