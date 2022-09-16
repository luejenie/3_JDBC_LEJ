package edu.kh.jdbc.model.dao;

import static edu.kh.jdbc.common.JDBCTemplate.close;  //* 해도 됨.

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.model.vo.TestVO;


// DAO(Data Access Object) : 데이터가 저장된 DB에 접근하는 객체
//							-> SQL 수행, 결과 반환 받는 기능을 수행 
public class TestDAO {

	// JDBC 객체를 참조하기 위한 참조변수 선언   __ connection은 Service에서 만든 걸 가져오자.
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	
	// XML로 SQL을 다룰 예정 -> Properties 객체 사용
	private Properties prop;
	
	// -> 기본 생성자
	public TestDAO() {
		
		// TestDAO 객체 생성 시    __ new TestDAO();라는 형태로 객체 생성시 아래 내용이 불려감.
		// test-query.xml 파일의 내용을 읽어와
		// Properties 객체에 저장
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream("test-query.xml"));
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
	}
	
	
	
	/**
	 * @param conn
	 * @param vo1
	 * @return result
	 */
	public int insert(Connection conn, TestVO vo1) throws SQLException {
		// throws SQLException 
		// -> 호출한 곳으로 발생한 SQLException을 던짐     __ 난 모름. 네가 처리해
		// -> 예외 처리를 모아서 진행하기 위해서.
		
		
		// 1. 결과 저장용 변수 선언
		int result = 0;
		
		try {
			// 2. SQL 작성(test_query.xml에 작성된 SQL 얻어오기)
			//				--> prop이 저장하고 있음!!
			
			String sql = prop.getProperty("insert");
//			INSERT INTO TB_TEST
//			VALUES(?, ?, ?)  
//			이 모양으로 들어와있음
			
			
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//_다른 예외처리 방식 사용! -> throws 예외 처리 사용
			
			// 4. ?(위치홀더)에 알맞은 값 세팅
			pstmt.setInt(1, vo1.getTestNo());
			pstmt.setString(2, vo1.getTestTitle());
			pstmt.setString(3, vo1.getTestContent());
			
			
			// 5. SQL(INSERT) 수행 후 결과 반환 받기
//			pstmt.executeQuery(); // -> SELECT 수행, ResultSet 반환
//			pstmt.executeUpdate(); // -> DML 수행, 반영된 행의 개수(int) 반환
			result = pstmt.executeUpdate(); 
				         //__ SQLException 오류 발생 가능성 있음
						 //     -> catch 대신 throws 사용함.
						 // catch 사용 안하면 try~catch 왜 사용?
						 //  -> finally의 close(pstmt)를 사용해야하기 때문!
					     //     finally는 try~catch구문이 수행되어야 수행됨.
			
				
			
		} finally {
			// 6. 사용한 JDBC 객체 자원 반환 ( close() )
			close(pstmt);
			
		}
		
		
		// 7. SQL 수행 결과 반환
		close(pstmt);
		
		return result;
	}

}







