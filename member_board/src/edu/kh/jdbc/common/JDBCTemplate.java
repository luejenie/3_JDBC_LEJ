package edu.kh.jdbc.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTemplate {
	
	/* DB 연결(Connection 생성), 
	 * 자동 커밋 off, 
	 * 트랜잭션 제어, 
	 * JDBC 객체 자원 반환(close)
	 * 
	 * 이러한 JDBC에서 반복 사용되는 코드를 모아둔 클래스
	 * 
	 * * 모든 필드, 메서드가 static *
	 *  -> 어디서든지 클래스명.필드명 / 클래스명.메서드명
	 *     으로 호출 가능 (별도 객체 생성x)
	 * 
	 * */
	
	//(필드)
	private static Connection conn = null;  // static과 heap 영역 구분
	//-> static 메서드에서 필드를 사용하기 위해서는
	//   필드도 static 필드이어야 한다.
	
					
	/** DB 연결 정보를 담고 있는 Connection 객체 생성 및 반환 메서드
	 * @return conn
	 */             //java.sql
	public static Connection getConnection() {
		
		//_ JDBC구문은 모두 예외발생함
		try {
			
			// 현재 커넥션 객체가 없을 경우 -> 새 커넥션 객체를 생성
			if(conn == null || conn.isClosed()) {
				
				// conn.isClosed() : 커넥션이 close 상티이면 true 반환
				
				Properties prop = new Properties();
				// Map<String, String> 형태의 객체, XML 입출력에 특화
				
				
				// driver.xml 파일 읽어오기
				prop.loadFromXML ( new FileInputStream("driver.xml"));
				// -> XML 파일에 작성된 내용이 Properties 객체에 모두 저장됨.
				
				// XML에서 읽어온 값을 모두 String 변수에 저장
				String driver = prop.getProperty("driver");  //_ key가 driver인 것의 value
				String url = prop.getProperty("url");
				String user = prop.getProperty("user");
				String password = prop.getProperty("password");
												   //_오타나지 않도록 주의! 에러로 직접적으로 뜨지 않음.
		
				// 커넥션 생성
				Class.forName(driver);  // Oracle JDBC Driver 객체 메모리 로드
				
				// DriverManager를 이용해 Connection 객체 생성
				conn = DriverManager.getConnection(url, user, password);
				
				// 개발자가 직접 트랜잭션을 제어할 수 있도록
				// 자동 커밋 비활성화
				conn.setAutoCommit(false);
			}
			
			
		} catch(Exception e) {
			System.out.println("[Connection 생성 중 예외 발생]");
			e.printStackTrace();
			
		}
		
		return conn;
	}
	
	// conn = JDBCTemplate.getConnection()  -> 이제 이렇게 쓰면 됨.
	
	
	
	
	
	/** Connection 객체 자원 반환 메서드
	 * @param conn
	 */							// Connection 객체를 매개변수로, 받겠다.
	public static void close(Connection conn) {
		try {
			
			// 전달 받은 conn이
			// 참조하는 Connection 객체가 있고  (!= null)
			// 그 Connection 객체가 close 상태가 아니라면, (!conn.isClosed)
			if(conn != null && !conn.isClosed()) conn.close();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	// __ ↑↓ 매개변수가 다른 close라는 동일한 이름의 함수 사용 
	// __  - Overloading (과적) : 하나의 이름으로 다른 함수 사용
	
	
	
	/** Statement(부모), PreparedStatement(자식) 객체 자원 반환 메서드
	 * (다형성, 동적바인딩)
	 * @param stmt
	 */							
	public static void close(Statement stmt) {  //_오버로딩 적용
		try {
			if(stmt != null && !stmt.isClosed()) stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	
	
	/** ResultSet 객체 자원 반환 메서드
	 * @param rs
	 */
	public static void close(ResultSet rs) {  //_오버로딩 적용
		try {
			if(rs != null && !rs.isClosed()) rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	/** 트랜잭션 Commit 메서드
	 * @param conn
	 */
	public static void commit(Connection conn) {
		try {
			// 연결되어있는 Connection 객체일 경우에만 Commit 진행
			if(conn != null && !conn.isClosed())  conn.commit();
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
	}

	
	
	/** 트랜잭션 Rollback 메서드
	 * @param conn
	 */
	public static void rollback(Connection conn) {
		try {
			// 연결되어있는 Connection 객체일 경우에만 rollback 진행
			if(conn != null && !conn.isClosed())  conn.rollback();
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
		
		
	}
	
	
	//_ static 사용 : 공유 메모리 영역, 정적 메모리 영역
	
	
	
}
