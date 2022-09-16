package edu.kh.jdbc.run;

import java.sql.SQLException;
import java.util.Scanner;

import edu.kh.jdbc.model.service.TestService;
import edu.kh.jdbc.model.vo.TestVO;

public class Run3 {
	public static void main(String[] args) {
		
		// 번호, 제목, 내용을 입력받아
		// 번호가 일치하는 행의 제목, 내용 수정
		
		// 수정 성공 시 -> 수정되었습니다.
		// 수정 실패 시 -> 일치하는 번호가 없습니다.
		// 예외 발생 시 -> 수정 중 예외가 발생했습니다.
		
		
		TestService service = new TestService();
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("TEST_NO : ");
		int testNo = sc.nextInt();
		
		System.out.print("TEST_TITLE : ");
		String testTitle = sc.next();
		
		System.out.print("TEST_CONTENT :");
		String testContent = sc.next();
		
		
		TestVO vo1 = new TestVO(testNo, testTitle, testContent);
		
		
		try {
			int result = service.update(vo1);
			
			if(result > 0) {
				System.out.println("update 성공");
			} else {
				System.out.println("update 실패");
			}
		
		} catch (SQLException e) {
			System.out.println("SQL 수행 중 오류 발생");
			
			e.printStackTrace();
		}
		
		
	}

}
