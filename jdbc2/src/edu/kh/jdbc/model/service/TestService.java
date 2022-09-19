package edu.kh.jdbc.model.service;

			//__ TestService에서 JDBCTemplate 이거마저 줄이기 위해.
import static edu.kh.jdbc.common.JDBCTemplate.*;  
//import static 구문
//-> static이 붙은 필드, 메서드를 호출할 때
//클래스명을 생략할 수 있게 하는 구문

import java.sql.Connection;
import java.sql.SQLException;

import edu.kh.jdbc.model.dao.TestDAO;
import edu.kh.jdbc.model.vo.TestVO;

// Service : 비즈니스 로직(데이터 가공, 트랜잭션 제어) 처리
// -> 실제 프로그램이 제공하는 기능을 모아놓은 클래스

// 하나의 Service 메서드에서 n개의 DAO 메서드(지정된 SQL 수행)를 호출하여
// 이를 하나의 트랜잭션 단위로 취급하여
// 한번에 commit, rollback을 수행할 수 있다.

// * 여러 DML을 수행하지 않는 경우(단일 DML, SELECT 등)라도
//	 코드의 통일성을 지키기 위해서 Service에서 Connection 객체를 생성한다.

// -> Connection 객체가 commit, rollback 메서드 제공


public class TestService {
	
	private TestDAO dao = new TestDAO();
	
	

	/** 1행 삽입 서비스
	 * @param vo1
	 * @return result
	 */
	public int insert(TestVO vo1) throws SQLException {
		
		// 커넥션 생성  __항상 서비스 메서드 첫줄에는 커넥션 생성 
						// 트랜잭션을 처리하려면 커넥션 필수! -> Service에 쓸수밖에 없음
		Connection conn = getConnection();
						// static 호출 -> 클래스명.메서드명
		// 원래 Connection conn = JTBCTemplate.getConnection();   //위에 import한후 삭제
		
		
		// INSERT DAO 메서드를 호출하여 수행 후 결과 반환 받기
		// -> Service에서 생성한 Connection 객체를 반드시 같이 전달해야 한다!!!
		int result = dao.insert(conn, vo1);    //___예외 뜨는데 또 던지기 throws
		// result == SQL 수행 후 반영된 결과 행의 개수
		// __ result는 여기서 0 or 1
		
		
		// 트랜잭션 제어
		if(result > 0) commit(conn);
		else 		   rollback(conn);
		
		// 커넥션 반환(close)  __return 전에 반환하는 코드!
		close(conn);	
		
		// 결과 반환
		return result;
		
	}



	public int insert(TestVO vo1, TestVO vo2, TestVO vo3) throws Exception{
		// throws Exception
		// -> 아래 catch문에서 강제 발생된 예외를
		//    호출부로 던진다는 구문
		
		// 왜 예외를 강제 발생 시켰는가?
		// -> Run2에서 예외 상황에 대한 다른 결과를 출력하기 위해서
		
		

		// 1. Connection 생성 (무조건 1번!)
		Connection conn  = getConnection();
		
		int res = 0; // insert 3회 모두 성공 시 1, 아니면 0   _우리가 의미 부여
		
		
		try {
			// insert 중 오류가 발생하면 모든 insert 내용 rollback 
			// -> try-catch로 예외가 발생했다는 것을 인지함.
			
			int result1 = dao.insert(conn, vo1);  //1
			int result2 = dao.insert(conn, vo2);  //1 
			int result3 = dao.insert(conn, vo3);  //Exception
							//__ INSERT 3번 진행 -> 트랜잭션. 3번째에 실패 -> rollback
			
			
			//트랜잭션 제어
			if(result1 + result2 + result3 == 3) { //모두 insert 성공한 경우
				commit(conn);
				res = 1;
			
			} else {
				rollback(conn);
//				res = 0;   __기본값이 0이기 떄문에 굳이 안써도 됨.
			}
		
		} catch(SQLException e) {  // dao 수행 중 예외 발생 시 
			rollback(conn);
			
			// -> 실패된 데이터를 DB에 삽입하지 않음
			// -> DB에는 성공된 데이터만 저장이 된다
			//    == DB에 저장된 데이터의 신뢰도가 상승한다.
			
			e.printStackTrace();
			
			// Run2 클래스로 예외를 전달할 수 있도록 예외 강제 발생 __Run2로 예외 유지
			throw new Exception("DAO 수행 중 예외 발생");  //__이거에 대한 예외처리로 던짐-> throws Exception
		
		} finally {  // 무조건 conn 반환하기
			close(conn);			
			
		}

		return res;  // insert 3회 결과 반환
	}


	


	public int update(TestVO vo1) throws SQLException{
		Connection conn = getConnection();
		
		int result = dao.update(conn, vo1);
		
		if(result > 0) commit(conn);
		else		   rollback(conn);
		
		close(conn);
		
		return result;

	}
	

}
