package edu.kh.jdbc.run;

import edu.kh.jdbc.model.service.TestService;
import edu.kh.jdbc.model.vo.TestVO;

public class Run {
	public static void main(String[] args) {
		
		TestService service = new TestService();
		
		// TB_TEST 테이블에 INSERT 수행
		TestVO vo1 = new TestVO(1, "제목1", "내용1");
		
		// TB_TEST 테이블에 INSERT를 수행하는 서비스 메서드를 호출 후
		// 결과를 반환 받기
		
		int result = service.insert(vo1);
		
		
	}

}
