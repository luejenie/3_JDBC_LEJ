package edu.kh.jdbc.model.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.model.vo.TestVO;

public class TestDAO3 {
	
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private Properties prop;
	
	
	public TestDAO3() {
		
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream("testRun3-query.xml"));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}


	public int update(Connection conn, TestVO vo1) {
		
		//1. 결과 저장용 변수
		int result = 0;  
		
		try {
			
			String sql = prop.getProperty("update");
//			UPDATE TB_TEST 
//			SET TEST_TITLE = ?, TEST_CONTENT = ?
//			WHERE TEST_NUMBER = ?
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, vo1.getTestTitle());
			pstmt.setString(2, vo1.getTestContent());
			pstmt.setInt(3, vo1.getTestNo());
			
			
			result = pstmt.executeUpdate();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		
		} finally {
			JDBCTemplate.close(pstmt);
			
		}
		
		return result;
	}
	
	
	

}
