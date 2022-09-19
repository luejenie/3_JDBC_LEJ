package re.lej.jdbc.view;



import java.util.InputMismatchException;
import java.util.Scanner;

import re.lej.jdbc.model.service.Service;
import re.lej.jdbc.vo.VO;

public class View {
	
	private Scanner sc = new Scanner(System.in);
	private Service service = new Service();
	
	
	private VO loginMember = null;
	
	
	public void mainMenu() {
		
		int input = -1;
		
		do {
			try {
				
				System.out.println("***** 회원제 게시판 프로그램(복습) *****");
				System.out.println("1. 로그인");
				System.out.println("2. 회원 가입");
				System.out.println("0. 프로그램 종료");
				
				System.out.println("\n번호 선택 : ");
				input = sc.nextInt();
				sc.nextLine();
				System.out.println();
				
				switch(input) {
				case 1: break;
				case 2: break;
				case 0: System.out.print("프로그램 종료"); break;
				default: System.out.print("화면에 존재하는 번호를 입력해 주세요.");
				}
				
				
			} catch(InputMismatchException e) {
				System.out.println("\n<<입력 형식이 올바르지 않습니다.>>");
				sc.nextLine();
				e.printStackTrace();
			}
		} while(input != 0);
	}
	
	

	/**
	 * 2. 회원 가입 화면
	 */
	public void signUp() {
		System.out.println("[회원 가입]");
		
		String memberId = null;
		String memberPw1 = null;
		String memberPw2 = null;
		String memberName = null;
		String memberGender = null;
		
		try {
			// 아이디 입력
			while(true) {
			
				System.out.print("아이디 : ");   // 중복 확인
				memberId = sc.next();
				
				int result = service.idDupCheck(memberId);
				
				
				
			
			} 
			
			
			System.out.print("비밀번호 : ");
			memberPw1 = sc.next();
			
			System.out.print("비밀번호 확인 : ");
			memberPw2 = sc.next();
			
			System.out.print("이름 : ");
			memberName = sc.next();
			
			System.out.print("성별(M/F) : ");
			memberGender = sc.next();
			
			
			
			
			
			
			
			
			
			
			
		} catch(Exception e) {
			System.out.println("\n<<회원 가입 중 예외 발생>>");
			e.printStackTrace();
		}
		
		
		
	}
	
	

}
