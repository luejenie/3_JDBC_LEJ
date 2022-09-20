package edu.kh.jdbc.member.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.main.view.MainView;
import edu.kh.jdbc.member.model.service.MemberService;
import edu.kh.jdbc.member.vo.Member;

/** 회원 메뉴 입/출력 클래스
 * @author user1
 *
 */
/**
 * @author user1
 *
 */
public class MemberView {

//	3   -> 회원기능 메뉴까지 만든 후 MainView 가서 객체 생성
	private Scanner sc = new Scanner(System.in);
	
	// 회원 관련 서브시를 제공하는 객체를 생성
	private MemberService service = new MemberService();
	
	// 로그인 회원 정보 저장용 변수
	private Member loginMember = null;
	
	// 메뉴 번호를 입력 받기 위한 변수
	private int input = -1; //_memberMenu()에서 필드로 올려서 클래스 내에서 사용할 수 있게!
	
	
	/**
	 * 회원 기능 메뉴
	 * @param(전달인자) loginMember(로그인된 회원 정보)
	 */					          //_6 MainMenu에서 얻어옴. 
								  //_위에서 같은 이름으로 필드를 생성하고 이 정보를 위 필드에 넣어주기.
	public void memberMenu(Member loginMember) {
		// int input = -1;  // 필드로 이동
		
		// 전달 받은 로그인 회원 정보(this.loginMember)를 필드(loginMember)에 저장
		this.loginMember = loginMember;
		
		do {
			try {
				System.out.println("\n***** 회원 기능 *****\n");
				
				System.out.println("1. 내 정보 조회");
				System.out.println("2. 회원 목록 조회(아이디, 이름, 성별)");
				System.out.println("3. 내 정보 수정(이름, 성별)");
				System.out.println("4. 비밀번호 변경(현재 비밀번호, 새 비밀번호, 새 비밀번호 확인)");
				System.out.println("5. 회원 탈퇴");  //_현재 비밀번호 입력 -> 정말 탈퇴 Y/N -> Y:탈퇴 / N:비밀번호 불일치
				System.out.println("0. 메인메뉴로 이동");
				
				System.out.print("\n메뉴 선택 : ");
				input = sc.nextInt();
				sc.nextLine(); //입력 버퍼 개행문자 제거
				
				System.out.println();
				switch(input) {
				case 1: selectMyInfo(); break;
				case 2: selectAll(); break;
				case 3: updateMember(); break;
				case 4: updatePw(); break;
				case 5: secession(); break;
				case 0: System.out.println("[메인 메뉴로 이동합니다.]"); break;
				default: System.out.println("메뉴에 작성된 번호만 입력해주세요.");
				}
				System.out.println();
				
			} catch(InputMismatchException e) {
				System.out.println("\n<<입력 형식이 올바르지 않습니다.>>");
				sc.nextLine();
				e.printStackTrace();
			}
			
			
		} while(input!=0);
	}
	
	/** -- 7
	 * 1. 내 정보 조회
	 */
	private void selectMyInfo() {
		System.out.println("\n[내 정보 조회]\n");
		
											// 필드
		System.out.println("회원 번호 : " + loginMember.getMemberNo());
		System.out.println("아이디 : "    + loginMember.getMemberId());
		System.out.println("이름 : "     + loginMember.getMemberName());
		
		System.out.print("성별 : ");
		if(loginMember.getMemberGender().equals("M")) {
			System.out.println("남");
		} else {
			System.out.println("여");
		}
		
		System.out.println("가입일 : " 	+ loginMember.getEnrollDate());
	}
	
	
	
	/** 
	 * 2. 회원 목록 조회
	 */
	private void selectAll() {
		System.out.println("\n[회원 목록 조회]\n");
		
		// DB에서 회원 목록 조회(탈퇴 회원 미포함)
		// + 가입일 내림차순
		
		try {
			// 회원 목록 조회 서비스 호출 후 결과 반환 받기   //8
			List<Member> memberList = service.selectAll();
			
			// 조회 결과가 있으면 모두 출력
			// 없으면 "조회 결과가 없습니다." 출력
			
			if(memberList.isEmpty()) {  // 비어있는 경우
				System.out.println("\n[조회 결과가 없습니다.]\n");
				
			} else {
				//System.out.println(memberList.toString()); _ 우리 member에는 toString 오버라이딩 안 되어 있음.
		
				System.out.println("     아이디       이름      성별 ");
				System.out.println("--------------------------------");
				
				//향상된 for문 (하나씩 꺼낼 변수 : 배열 등)
				for(Member member : memberList) {  //_컬렉션에서 하나씩 꺼낼때는 향상된 for문
					System.out.printf("%10s    %5s    %3s \n",
							member.getMemberId(), member.getMemberName(), member.getMemberGender());
				}
			}
		} catch(Exception e) {  //_데이터베이스 외부 프로그램을 가져올 떄는 예외가 발생할 가능성이 큼.
			System.out.println("\n<<회원 목록 조회 중 예외 발생>>\n");
			e.printStackTrace();
		}
	}

	
	
	/**
	 * 회원 정보 수정
	 */
	private void updateMember() {
		try {  
			System.out.println("\n[회원 정보 수정]\n");
		
			System.out.print("변경할 이름 : ");
			String memberName = sc.next();
		
		
			String memberGender = null;
			while(true) {
				System.out.print("변경할 성별(M/F) : ");
				memberGender = sc.next().toUpperCase();
				
				if(memberGender.equals("M") || memberGender.equals("F")) {
					break;
				} else {
					System.out.println("M 또는 F만 입력해주세요.");
				}
			}
			
			// 서비스로 전달할 Member 객체 생성
			Member member = new Member();
			member.setMemberNo(loginMember.getMemberNo());
			member.setMemberName(memberName);
			member.setMemberGender(memberGender);
			
			// 회원 정보 수정 서비스 메서드 호출 후 결과 반환 받기
			int result = service.updateMember(member);

			if(result > 0) {
				// loginMember에 저장된 값과
				// DB에 수정된 값을 동기화하는 작업이 필요하다!    __ 하기 전에는 DB는 바뀌는데 Java는 바뀌지 않음
				loginMember.setMemberName(memberName);
				loginMember.setMemberGender(memberGender);
											//뒤에 값들로 DB를 수정
			    // 수정한 값을 loginMember로 넣어주기
				
				
				
				System.out.println("\n[회원 정보가 수정되었습니다.]\n");
			} else {
				System.out.println("\n[수정 실패]\n");
			}
			
		} catch(Exception e) {
			System.out.println("\n<<회원 정보 수정 중 예외 발생>>\n");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 비밀번호 변경
	 */
	private void updatePw() {
		System.out.println("\n[비밀번호 변경]\n");
		
		try {
			System.out.print("현재 비밀번호 : ");
			String currentPw = sc.next();
			
			String newPw1 = null;
			String newPw2 = null;
			
			while(true) {
				System.out.print("새 비밀번호 : ");
				newPw1 = sc.next();

				System.out.print("새 비밀번호 확인 : ");
				newPw2 = sc.next();
				
				
				if(newPw1.equals(newPw2)) {
					break;
				} else {
					System.out.println("\n새 비밀번호가 일치하지 않습니다. 다시 입력해주세요.\n");
				}
			} //while end
			
			
			// 서비스 호출 후 결과 반환 받기							// 앞에서 했던 것과 달리 따로따로 보내보기
			int result = service.updatePw(currentPw, newPw1, loginMember.getMemberNo());
											//현재비밀번호, 새비밀번호,  로그인 회원 번호
			
			if(result > 0) {
				System.out.println("\n[비밀번호가 변경되었습니다.]\n");
			} else {
				System.out.println("\n[현재 비밀번호가 일치하지 않습니다.]\n");
			}
		 	
			
			
		} catch(Exception e) {
			System.out.println("\n<<비밀번호 변경 중 예외 발생>>\n");
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 회원 탈퇴
	 */
	private void secession() {
		System.out.println("\n[회원 탈퇴]\n");
		
		try {
			System.out.print("현재 비밀번호 입력 : ");
			String memberPw = sc.next();
			
			while(true) {
				System.out.print("정말 탈퇴하시겠습니까(Y/N)? ");
				char ch = sc.next().toUpperCase().charAt(0);
				
				//_ char는 기본자료형이라 기본연산자로 비교할 수 있음.
				if(ch == 'Y') {
					// 서비스 호출 후 결과 반환 받기
					int result = service.secession(memberPw, loginMember.getMemberNo());
													// 입력받은 비밀번호,  회원 번호
					
					if(result > 0) {
						System.out.println("\n[탈퇴 되었습니다..]\n"); //로그아웃, 메인으로 돌아감
						
						input = 0;  // 메인 메뉴로 이동
						
						MainView.loginMember = null; //로그아웃
						
					} else {
						System.out.println("\n[비밀번호가 일치하지 않습니다.]\n");
					}
					 
					break; //while문 종료
					
				} else if(ch == 'N') {
					System.out.println("\n[취소되었습니다.]\n");
					break;
					
					
				} else {
					System.out.println("\n[Y 또는 N만 입력해 주세요.]\n");
				}
			}
			
			
		} catch(Exception e) {
			System.out.println("\n<<회원 탈퇴 중 예외 발생>>\n");
			e.printStackTrace();
		}
		
	}
	
	
	

}
