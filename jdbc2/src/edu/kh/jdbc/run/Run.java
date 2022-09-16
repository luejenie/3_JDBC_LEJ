package edu.kh.jdbc.run;

import java.sql.SQLException;

import edu.kh.jdbc.model.service.TestService;
import edu.kh.jdbc.model.vo.TestVO;

public class Run {
	public static void main(String[] args) {
		
		TestService service = new TestService();
		
		// TB_TEST 테이블에 INSERT 수행
		TestVO vo1 = new TestVO(1, "제목1", "내용1");
		
		// TB_TEST 테이블에 INSERT를 수행하는 서비스 메서드를 호출 후
		// 결과를 반환 받기
		
		try {
			int result = service.insert(vo1); // 1/0  
			
			if(result > 0) { //1
				System.out.println("insert 성공");
			} else {
				System.out.println("insert 실패");
				  //_서브쿼리를 이용한 INSERT 시 실패할 가능성이 있음.
			}
		
			
			
		} catch (SQLException e) {  //__sql을 던져서 이리로 온것
			System.out.println("SQL 수행 중 오류 발생");
		
			e.printStackTrace();
		}  
		
		
	}

}
