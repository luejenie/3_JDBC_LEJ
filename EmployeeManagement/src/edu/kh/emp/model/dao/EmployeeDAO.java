package edu.kh.emp.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;

import edu.kh.emp.model.vo.Employee;

// DAO(Data/Database Access Object, 데이터 접근 객체)
// -> 데이터베이스에 접근(연결)하는 객체
// --> JDBC 코드 작성
public class EmployeeDAO {
	
	// JDBC 객체 참조 변수로 필드 선언(class 내부에서 공통 사용)
	private Connection conn; // 필드(Heap, 변수가 비어있을 수 없음) 
	private Statement stmt;      // -> JVM이 지정한 기본값으로 초기화
	private ResultSet rs; 		 // -> 참조형의 기본값은 null;
								 // -> 별도 초기화 안해도 된다!
	
	private PreparedStatement pstmt;
	// Statement의 자식으로 향상된 기능을 제공
	// -> ? 기호(placeholder, 위치홀더)를 이용해서
	//	  SQL에 작성되어지는 리터럴을 동적으로 제어함.
	
	// SQL ?기호에 추가되는 값은
	// 숫자인 경우 '' 없이 대입
	// 문자열인 경우 ''가 자동으로 추가되어 대입.
	
	
	
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private String user = "kh_lej";
	private String pw = "kh1234";

	

/* [차이점 구분]	
 * 	private Connection conn; // 필드(Heap, 변수가 비어있을 수 없음) 
 * 							    -> JVM이 지정한 기본값으로 초기화
 * 								-> 참조형의 기본값은 null;
 *   							-> 별도 초기화 안해도 된다!  (=null 안해도 됨)
 * 
	public void method() {
		Connection conn2; // 지역변수(Stack, 변수가 비어있을 수 있음)
	}
*/	
	
	
	
	
	/** 전체 사원 정보 조회 DAO
	 * @return empList
	 */
	public List<Employee> selectAll() {
		// TODO Auto-generated method stub //from EmployeeView
		// 1. 결과 저장용 변수 선언
		List<Employee> empList = new ArrayList<>();
		
		try {
			
			//2. JDBC 참조 변수에 객체 대입
			//-> conn, stmt, rs에 객체 대입
			Class.forName(driver);  // 오라클 jdbc 드라이버 객체 메모리 로드  __드라이버를 객체로 만든다. Class.를 외치면 객체로 만들어주는..
			conn = DriverManager.getConnection(url, user, pw);
			// 오라클 jdbc 드라이버 객체를 이용하여 DB 접속 방법 생성
			
			
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, PHONE, \r\n"
					+ "	   NVL(DEPT_TITLE, '부서없음') DEPT_TITLE, JOB_NAME, SALARY\r\n"
					+ "FROM EMPLOYEE \r\n"
					+ "LEFT JOIN DEPARTMENT ON (DEPT_ID = DEPT_CODE)\r\n"
					+ "JOIN JOB USING (JOB_CODE)"
					+ "ORDER BY EMP_ID";
			
			
			// Statement 객체 생성
			stmt = conn.createStatement();
						
			// SQL을 수행 후 결과(ResultSet) 반환 받음
			rs = stmt.executeQuery(sql);
			
			
			// 3. 조회 결과를 얻어와 한 행씩 접근하여 
			// Employee 객체 생성 후 컬럼 값 옮겨 담기
			// -> List 추가
			while(rs.next()) {
				
				int empId = rs.getInt("EMP_ID");
				// EMP_ID 컬럼은 문자열 컬럼이지만
				// 저장된 값들이 모두 숫자 형태
				// -> DB에서 자동으로 형변환 진행해서 얻어옴
				
				String empName = rs.getString("EMP_NAME");
				String empNo = rs.getString("EMP_NO");
				String email = rs.getString("EMAIL");
				String phone = rs.getString("PHONE");
				String departmentTitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				int salary = rs.getInt("SALARY");
				
				
				//__ 위에서 불러온 것을 Employee 객체에 담아주고
				Employee emp = new Employee(empId, empName, empNo, email, phone, 
										departmentTitle, jobName, salary);
				empList.add(emp); // List 담기
				
			}  // while 종료   //__ 23번(전체개수) 돌고 empList에 담게 될 것
			
	
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
			// 4. JDBC 객체 자원 반환
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				
				
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		// 5. 결과 반환   ->_ view의 printAll로 반환됨
		return empList;
	}



	/** 사번이 일치하는 사원 정보 조회 DAO
	 * @param empId   // 전달받은 값(전달인자): PARAMETER , 전달하는 값 : ARGUMENT
	 * @return emp
	 */
	public Employee selectEmpId(int empId) {
		// TODO Auto-generated method stub 자동완성으로 만듦. view에서.
		
		// 결과 저장용 변수를 선언
		Employee emp = null;
		// 만약 조회 결과가 있으면 Employee 객체를 생성해서 emp에 대입
		// 만약 조회 결과가 없으면 emp에 아무것도 대입하지 않음(null)
		// __ 위에서 이 자리에 list 선언했음
		
		try {   // __ 드라이버 불러오는 코드가 계속 반복될 것. 그렇지 않으면 아무것도 하지 않은 상태에서도 데이터베이스가 계속 불러와져서 문제 발생
			
			Class.forName(driver); // 오라클 JDBC 드라이버 메모리 로드
			conn = DriverManager.getConnection(url, user, pw);  // 커넥션 생성해서 얻어오기
			
			// SQL 작성
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, PHONE,"
					+ "	   NVL(DEPT_TITLE, '부서없음') DEPT_TITLE, JOB_NAME, SALARY"
					+ " FROM EMPLOYEE"
					+ " LEFT JOIN DEPARTMENT ON (DEPT_ID = DEPT_CODE)"
					+ " JOIN JOB USING (JOB_CODE)"
					+ " WHERE EMP_ID = " + empId;
											// View에서 입력받은 사번(파라미터)
			
			
			// Statement 생성
			stmt = conn.createStatement();
					
			// SQL 수행 후 결과 반환 받기
			rs = stmt.executeQuery(sql);
			
			
			// ** 조회 결과가 최대 1행인 경우  			 _== 우리가 결과가 하나라는 것을 확실하게 알고 있을 떄
			//    불필요한 조건 검사를 줄이기 위해 if문 사용 권장 **     __ 원래 while문의 자리. while은 조건을 두 번 검사해서 불필요.
			if(rs.next()) { // 조회 결과가 있을 경우
				
				// int empId = rs.getInt("EMP_ID");   // -> 파라미터(전달받은값)와 같은 값이므로 불필요 __이미 위에서 받았음.
				String empName = rs.getString("EMP_NAME");
				String empNo = rs.getString("EMP_NO");
				String email = rs.getString("EMAIL");
				String phone = rs.getString("PHONE");
				String departmentTitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				int salary = rs.getInt("SALARY");
				
				// 조회 결과를 담은 Employee 객체 생성 후
				// 결과 저장용 변수 emp에 대입  ___위에서 선언
				emp = new Employee(empId, empName, empNo, email, phone, departmentTitle, jobName, salary);
					
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		
		} finally {
			
			try {
				
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			
		}		
		// 결과 반환
		return emp;
	}



	/** 9. 주민등록번호가 일치하는 사원 정보 조회 DAO
	 * @param empNo
	 * @return emp
	 */
	public Employee selectEmpNO(String empNo) {
		// TODO Auto-generated method stub
		
		// 결과 저장용 변수 선언
		Employee emp = null;
		
		try {
			// Connection 생성
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			
			// SQP 작성
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, PHONE,"
					+ "	   NVL(DEPT_TITLE, '부서없음') DEPT_TITLE, JOB_NAME, SALARY"
					+ " FROM EMPLOYEE"
					+ " LEFT JOIN DEPARTMENT ON (DEPT_ID = DEPT_CODE)"
					+ " JOIN JOB USING (JOB_CODE)"
					+ " WHERE EMP_NO = ?";
									// placeholder
			
			// Statement 객체 사용시 순서
			// SQL 작성 -> Statement 생성 -> SQL 수행 후 결과 반환(_이떄 매개변수로 SQL 사용)
			
			// PreparedStatement 객체 사용 시 순서
			// SQL 작성 
			// -> PreparedStatement 객체 생성(?가 포함된 SQL을 매개변수로 사용)  __기존과 다르게 SQL을 먼저 사용
			// -> ?에 알맞은 값 대입
			// -> SQL 수행 후 결과 반환
			
			
			
			
			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// ?에 알맞은 값 대입
			pstmt.setString(1, empNo);
			
			// SQL 수행 후 결과 반환
			rs = pstmt.executeQuery();
			// PreparedStatement는
			// 객체 생성 시 이미 SQL이 담겨져 있는 상태이므로
			// SQL 수행 시 매개변수로 전달할 필요가 없다!   __cf. rs = stmt.executeQuery(sql);
			// -> 실수로 SQL을 매개변수에 추가하면
			//   ?에 작성(추가)했던 값이 모두 사라져 수행 시 오류 발생
			
			
			
			if(rs.next()) {
				
				int empId = rs.getInt("EMP_ID"); 
				String empName = rs.getString("EMP_NAME");
//				String empNo = rs.getString("EMP_NO");  // -> 파라미터와 같은 값이므로 불필요
				String email = rs.getString("EMAIL");
				String phone = rs.getString("PHONE");
				String departmentTitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				int salary = rs.getInt("SALARY");
				
				emp = new Employee(empId, empName, empNo, email, phone, departmentTitle, jobName, salary);
					
				
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
	
		} finally {
			
			try {
			
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				
					
			} catch(Exception e) {
				e.printStackTrace();
				
			}
			
		}
		// 결과 반환
		return emp;
		
	}


	
	
	

	/** 1. 새로운 사원 정보 추가 DAO
	 * @param emp
	 * @return result(INSERT 성공한 행의 개수 반환)
	 */
	public int insertEmployee(Employee emp) {
		// TODO Auto-generated method stub
		
		// 결과 저장용 변수 선언
		int result = 0;
		
		try {
			
			//커넥션 생성
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			
			// ** DML 수행할 예정 **
			// - 트랜잭션에 DML 구문이 임시 저장
			// -> 정상적인 DML인지를 판별해서 개발자가 직접 COMMIT, ROLLBACK을 수행
			
			// 하지만.. Connection 객체 생성 시
			// AutoCommit이 활성화되어 있는 상태이기 때문에
			// 이를 해제하는 코드를 추가!!!!!!!!!!!!!!!!!!★★★★★
			conn.setAutoCommit(false);  // AutoCommit 비활성화
			// -> AutoCommit 비활성화해도
			//    conn.close() 구문이 수행되면 자동으로 Commit이 수행됨.
			// --> close() 수행 전에 트랜잭션 제어 코드를 작성해야 함!
			
			
			// SQL 작성
			String sql 
			= "INSERT INTO EMPLOYEE VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, NULL, DEFAULT)";
			// 퇴사여부 컬럼의 DEFAULT == 'N'                                   //입사일,  퇴사일, 퇴사(디폴트값 = 'N')
			
			
			// PreaparedStatement 객체 생성(매개변수에 SQL 추가)
			pstmt = conn.prepareStatement(sql);
			
			// ?(placeholder)에 알맞은 값 대입
			
			 pstmt.setInt(1, emp.getEmpId());   //_메서드 매개변수 emp -> emp.get~~
	         pstmt.setString(2, emp.getEmpName());
	         pstmt.setString(3, emp.getEmpNo());
	         pstmt.setString(4, emp.getEmail());
	         pstmt.setString(5, emp.getPhone());
	         pstmt.setString(6, emp.getDeptCode());
	         pstmt.setString(7, emp.getJobCode());
	         pstmt.setString(8, emp.getSalLevel());
	         pstmt.setInt(9, emp.getSalary());
	         pstmt.setDouble(10, emp.getBonus());
	         pstmt.setInt(11, emp.getManagerId());
			
			// SQL 수행 후 결과 반환
			result = pstmt.executeUpdate();
			// executeUpdate(): DML(INSERT, UPDATE, DELETE) 수행 후 결과 행 '개수' 반환
			// executeQuery(): SELECET 실행 후 ResultSet 반환
			
			
			// *** 트랜잭션 제어 처리 ***
			// -> DML 성공 여부에 따라서 COMMIT, ROLLBACK 제어
			if(result > 0) conn.commit();    // DML 성공 시 commit
			else		   conn.rollback();  // DML 실패 시 rollback
			
			
			
		
		} catch(Exception e) {
			e.printStackTrace();
		
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();			
				
			} catch(Exception e) {
				e.printStackTrace();
			}	
		}
		
		// 결과 반환
		return result;
	}



	/** 4. 사원이 일치하는 사원 정보 수정(이메일, 전화번호, 급여)
	 * @param emp
	 * @return result
	 */
	public int updateEmployee(Employee emp) {
		// TODO Auto-generated method stub
		
		int result = 0; // 결과 저장용 변수
		
		
		try {
			// 커넥션 생성
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			conn.setAutoCommit(false);  // AutoCommint 비활성화
			
			String sql = "UPDATE EMPLOYEE SET"
					+ " EMAIL = ?, PHONE = ?, SALARY = ?"
					+ " WHERE EMP_ID = ?";
			
			// PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);
					
			// ?에 알맞은 값을 세팅
			pstmt.setString(1, emp.getEmail());
			pstmt.setString(2, emp.getPhone());
			pstmt.setInt(3, emp.getSalary());
			pstmt.setInt(4, emp.getEmpId());
			
			result = pstmt.executeUpdate();  // 반영된 행의 개수 반환
			
			// 트래잭션 제어 처리
			if(result == 0)  conn.rollback();
			else	         conn.commit();
			
						
			
		} catch(Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				//JDBC 객체 자원 반환
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return result; // 결과 반환
	}



	
	
	
	
	
	/** 5. 사번이 일치하는 사원 정보 삭제
	 * @param emp
	 * @return result
	 */
	public int deleteEmployee(int empId) {
		// TODO Auto-generated method stub
		
		int result = 0;
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			conn.setAutoCommit(false);
			
			String sql = "DELETE FROM EMPLOYEE WHERE EMP_ID = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);   //선생님 풀이에 empId만 썼는데.. 뭐가 다른거지..?
			
			result = pstmt.executeUpdate();
			
			if(result > 0)  conn.commit();
			else		    conn.rollback();
		
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
			try { 
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				
			} catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
		
		return result;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
