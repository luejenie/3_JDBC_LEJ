package edu.kh.emp.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.kh.emp.model.dao.EmployeeDAO;
import edu.kh.emp.model.vo.Employee;

// 화면용 클래스(입력(Scanner)/출력(print()))
public class EmployeeView {
	
	private Scanner sc = new Scanner(System.in);
	
	// DAO 객체 생성
	private EmployeeDAO dao = new EmployeeDAO();
	
	
	
	
	
	// 메인 메뉴
	public void displayMenu() {
		
		int input = 0;
		
		do {
			try {
				
				System.out.println("---------------------------------------------------------");
				System.out.println("[사원 관리 프로그램]");
				System.out.println("1. 새로운 사원 정보 추가");
				System.out.println("2. 전체 사원 정보 조회");
				System.out.println("3. 사번이 일치하는 사원 정보 조회");
				System.out.println("4. 사번이 일치하는 사원 정보 수정");
				System.out.println("5. 사번이 일치하는 사원 정보 삭제");
				System.out.println("6. 입력 받은 부서와 일치 모든 사원 정보 조회");  //selectDeptEmp()
				System.out.println("7. 입력 받은 급여 이상을 받는 모든 사원 정보 조회"); //selectSalaryEmp()
				System.out.println("8. 부서별 급여 합 전체 조회");
				// selectDeptTotalSalary()
	            // DB 조회 결과를 HashMap<String, Integer>에 옮겨 담아서 반환
	            // 부서코드, 급여 합 조회
	            				
				System.out.println("9. 주민등록번호가 일치하는 사원 정보 조회");
				
	            System.out.println("10. 직급별 급여 평균 조회");
	            // selectJobAvgSalary()
	            // DB 조회 결과를 HashMap<String, Double>에 옮겨 담아서 반환 
	            // 직급명, 급여 평균(소수점 첫째자리까지) 조회
				
				System.out.println("0. 프로그램 종료");
				
				System.out.print("메뉴 선택 >> ");
				input = sc.nextInt();
				
				System.out.println();
				
				
				switch(input) {
				case 1 : insertEmployee(); break;
				case 2 : selectAll(); break;
				case 3 : selectEmpId(); break;
				case 4 : updateEmployee(); break;	//이메일, 전화번호, 급여만 바꾸기
				case 5 : deleteEmployee(); break;   // 사번일치가 3,4,5번에서 사용되므로, 사번 일치하는 코드만 따로 생성하기
				case 6 : selectDeptEmp(); break;
				case 7 : selectSalaryEmp(); break;
				case 8 : selectDeptTotalSalary(); break;
				case 9 : selectEmpNo(); break;
				case 10 :selectJobAvgSalary(); break;
				case 0 : System.out.println("프로그램을 종료합니다."); break;
				default : System.out.println("메뉴에 존재하는 번호만 입력하세요.");
				}
				
				
				
				
				
				
			} catch(InputMismatchException e) {  //잘못 입력 시
				System.out.println("정수만 입력해주세요.");
				input = -1; // do~while 반복문 첫 번째 바퀴에서 잘못 입력하면 종료되는 상황을 방지
				sc.nextLine();  // 입력 버퍼에 남아있는 잘못 입력된 문자열을 제거해서 무한 반복 방지
			}
		} while(input != 0);

	}
	
	
	
	
	
	/** 
	 * 2. 전체 사원 정보 조회
	 */
	public void selectAll() {
		System.out.println("<전체 사원 정보 조회>");
		
		// DB에서 전체 사원 정보를 조회하여 List<Employee> 형태로 반환하는
		// dao.selectAll() 메서드 호출
		List<Employee> empList = dao.selectAll();
		
		printAll(empList);
		
		
	}
	
	
	
	/** 전달받은 사원 List 모두 출력
	 * @param empList
	 */
	public void printAll(List<Employee> empList) {
		if(empList.isEmpty()) {
			System.out.println("조회된 사원 정보가 없습니다.");
			
		} else {
			System.out.println(" 사번  |  이름   |   주민 등록 번호   |         이메일         |   전화 번호   |  부서  | 직책 | 급여 ");
			System.out.println("------------------------------------------------------------------------------------------------");
			for(Employee emp : empList) { 
				System.out.printf(" %2d  | %4s | %s | %20s | %s | %s | %s | %d\n",
						emp.getEmpId(), emp.getEmpName(), emp.getEmpNo(), emp.getEmail(), 
						emp.getPhone(), emp.getDepartmentTitle(), emp.getJobName(), emp.getSalary());
			}
		}
	}
	
	
	
	
	
	/**
	 * 3. 사번이 일치하는 사원 정보 조회    // 사번이 일치하는 사람은 1명뿐이다.
	 */
	public void selectEmpId() {
		System.out.println("<사번이 일치하는 사원 정보 조회>");
		
		// 사번 입력 받기
		int empId = inputEmpId();
		
		// 입력 받은 사번을 DAO의 selectEmpId() 메서드로 전달하여 
		// 조회된 사원 정보 반환 받기
		Employee emp = dao.selectEmpId(empId);   
		// _ 데이터베이스에서 딱 한명의 정보를 가지고 오는데, List는 여러명의 정보를 사용하기 때문에 적절하지 않음.
		// _ 그래서 vo 사용 -> VO(Value Object) : 값 저장용 객체 (== DB 조회 결과 "한 행"을 저장)
		
		printOne(emp);  //조회 결과 출력
		
		
	}
	
	
	/** 사번을 입력 받아 반환하는 메서드
	 * @return
	 */
	public int inputEmpId() {
		System.out.print("사번 입력 : ");
		int empId = sc.nextInt();
		sc.nextLine();  // 버어페 남은 개행문자 제거   __추후에 혹시 모를 경우를 위해		
		return empId;		
	}
	
	
	
	/** 사원 1명 정보 출력
	 * @param emp
	 */
	public void printOne(Employee emp) {
		
		if(emp == null) {
			System.out.println("조회된 사원 정보가 없습니다.");
			
		} else {
			System.out.println(" 사번  |  이름   |   주민 등록 번호   |         이메일         |   전화 번호   |  부서  | 직책 | 급여 " );
			System.out.println("------------------------------------------------------------------------------------------------");
			
				System.out.printf(" %2d  | %4s | %s | %20s | %s | %s | %s | %d\n",
						emp.getEmpId(), emp.getEmpName(), emp.getEmpNo(), emp.getEmail(), 
						emp.getPhone(), emp.getDepartmentTitle(), emp.getJobName(), emp.getSalary());
		}
		
	}
	
	
	
	/**
	 * 9. 주민등록번호가 일치하는 사원 정보 조회
	 */
	public void selectEmpNo() {
		System.out.println("<주민등록번호가 일치하는 사원 정보 조회>");
		
		System.out.print("주민등록번호 입력 : ");   //_사번과 다른 점은 숫자가 아닌 문자열임
		String empNo = sc.next();
		
		Employee emp = dao.selectEmpNO(empNo);   //_사원 한 명의 정보임
		
		printOne(emp);
		
	}
	
	
	
	/**
	 * 1. 새로운 사원 정보 추가
	 */
	public void insertEmployee() {
		System.out.println("<사원 정보 추가>");   //11개 정보
		
		
		int empId = inputEmpId();
				
		System.out.print("이름 : ");
		String empName = sc.next();
		
		System.out.print("주민등록번호 : ");
		String empNo = sc.next();
		
		System.out.print("이메일 : ");
		String email = sc.next();
		
		System.out.print("전화번호(-제외) : ");
		String phone = sc.next();
		
		System.out.print("부서코드(D1~D9) : ");
		String deptCode = sc.next();
		
		System.out.print("직급코드(J1~J7) : ");
		String jobCode = sc.next();
		
		System.out.print("급여등급(S1~S6) : ");
		String salLevel = sc.next();
		
		System.out.print("급여 : ");
		int salary = sc.nextInt();
		
		System.out.print("보너스 : ");
		double bonus = sc.nextDouble();
		
		System.out.print("사수번호 : ");
		int managerId = sc.nextInt();
		
		
		// 입력 받은 값을 Employee 객체에 담아서 DAO로 전달
		Employee emp = new Employee(empId, empName, empNo, email, phone, salary, deptCode, jobCode, salLevel, bonus, managerId);
		int result = dao.insertEmployee(emp);   //__ select였다면 list를 사용했을텐데, 이건 insert -> 
		// INSERT, UPDATE, DELETE 같은 DML 구문은
		// 수행 후 테이블에 반영된 행의 개수를 반환함.   __dbeaver에서 DML 하면 n행이 성공했습니다~ 이런식으로 나오는 걸 기억!
		// -> 조건이 잘못된 경우 반영된 행이 없으므로 0 반환
		
		if(result > 0) { // DML 구문 성공시
			System.out.println("사원 정보 추가 성공");
			
		} else { // DML 구문 실패 시
			System.out.println("사원 정보 추가 실패...");
				
		}
	}
	
	
	
	/**
	 * 4. 사원이 일치하는 사원 정보 수정(이메일, 전화번호, 급여)
	 */
	public void updateEmployee() {
		System.out.println("<사번이 일치하는 사원 정보 수정>");
		
		int empId = inputEmpId();
		
		System.out.print("이메일 : ");
		String email = sc.next();
		
		System.out.print("전화번호(- 제외) : ");
		String phone = sc.next();
		
		System.out.print("급여 : ");
		int salary = sc.nextInt();
		
		
		// 기본생성자로 객체 생성 후 setter를 이용해서 초기화
		Employee emp = new Employee();  // _ 기본생성자 생성, 초기화 안 된 상태!
		emp.setEmpId(empId);
		emp.setEmail(email);
		emp.setPhone(phone);
		emp.setSalary(salary);
		
		int result = dao.updateEmployee(emp);  // UPDATE(DML) -> 반영된 행의 개수 반환(int형)
		
		if(result > 0) {
			System.out.println("사원 정보가 수정되었습니다.");
		} else {
			System.out.println("사번이 일치하는 사원이 존재하지 않습니다.");
		}
				
	}
	
	
//	/**
//	 * 5. 사번이 일치하는 사원 정보 삭제
//	 */
//	public void deleteEmployee() {
//		System.out.println("<사번이 일치하는 사원 정보 삭제>");
//		
//		int empId = inputEmpId();  // 사번 입력
//			
//		// 사번 입력하면 사번이랑 이름 출력되도록.
//		Employee emp1 = dao.selectEmpId(empId); 
////		print2(emp1);
//		if(emp1 == null) {
//			System.out.println("조회되는 사원 정보가 없어 삭제할 수 없습니다.");
//						
//			
//		} else {
//			System.out.printf("[사번 :   %2d]  \n[이름 : %4s] \n", emp1.getEmpId(), emp1.getEmpName());
//						
//			System.out.print("\n정말 삭제하시겠습니까?(Y/N) ");
//			char input = sc.next().toUpperCase().charAt(0); // Y/N 대소문자 구분 없이 입력
//			// -> 모두 대문자로 변환됨
//			
//			if(input == 'Y') {
//				// 삭제를 수행하는 DAO 호출
//				// 성공 : "삭제되었습니다."
//				// 실패 : "사번이 일치하는 사원이 존재하지 않습니다." - 출력
//				
//				Employee emp = new Employee();
//				emp.setEmpId(empId);
//				
//				int result = dao.deleteEmployee(emp);
//				
//				if(result > 0) {
//					System.out.println("삭제되었습니다.");
//				} else {
//					System.out.println("사번이 일치하는 사원이 존재하지 않습니다.");
//				}
//				
//				
//			} else {
//				System.out.println("취소되었습니다.");
//			}
//		}
//	
//	}
//	
//	
//	
//	/** 사원 1명 정보 출력(사번, 이름)   // 사원 정보 삭제 시, 사번 입력하면 관련 정보 뜨게하고 싶어서 만들어 본 것 (수업x)
//	 * @param emp
//	 */
//	public void print2(Employee emp) {
//		
//		if(emp == null) {
//			System.out.println("조회된 사원 정보가 없습니다.");
//			
//			
//		} else {
//			System.out.printf("사번 : %2d  \n이름 : %4s \n", emp.getEmpId(), emp.getEmpName());
//						
//		}
//		
//	}
	
	
	
	
	/**
	 * 5. 사번이 일치하는 사원 정보 삭제
	 */
	public void deleteEmployee() {
		System.out.println("<사번이 일치하는 사원 정보 삭제>");
		
		int empId = inputEmpId();  // 사번 입력
			
		
		System.out.print("\n정말 삭제하시겠습니까?(Y/N) ");
		char input = sc.next().toUpperCase().charAt(0); // Y/N 대소문자 구분 없이 입력
		// -> 모두 대문자로 변환됨
		
		if(input == 'Y') {
			// 삭제를 수행하는 DAO 호출
			// 성공 : "삭제되었습니다."
			// 실패 : "사번이 일치하는 사원이 존재하지 않습니다." - 출력
			
			Employee emp = new Employee();
			emp.setEmpId(empId);
			
			int result = dao.deleteEmployee(empId);
			
			if(result > 0) {
				System.out.println("삭제되었습니다.");
			} else {
				System.out.println("사번이 일치하는 사원이 존재하지 않습니다.");
			}
			
			
		} else {
			System.out.println("취소되었습니다.");
		}
	}
						
	
	
	/**
	 * 6. 입력 받은 부서와 일치 모든 사원 정보 조회
	 */
	public void selectDeptEmp() {
		System.out.println("<입력 받은 부서와 일치하는 모든 사원 정보 조회>");
		
		System.out.print("부서 입력 : ");
		String dept = sc.next();
		
		List<Employee> empList = dao.selectDeptEmp(dept);
		printAll(empList);
				
				
	}
	

	
	
	/**
	 * 7. 입력 받은 급여 이상을 받는 모든 사원 정보 조회
	 */
	public void selectSalaryEmp() {
		System.out.println("<입력 받은 급여 이상을 받는 모든 사원 정보 조회>");
		System.out.print("급여 입력 : ");
		int salary = sc.nextInt();
		
		List<Employee> empList = dao.selectSalaryEmp(salary);
		printAll(empList);

	}
	
	
	/**
	 * 8. 부서별 급여 합 전체 조회
	 *  // selectDeptTotalSalary()
    	// DB 조회 결과를 HashMap<String, Integer>에 옮겨 담아서 반환
    	// 부서코드, 급여 합 조회
	 */
	public void selectDeptTotalSalary() {
		System.out.println("<부서별 급여 합 전체 조회>");
		
		Map<String, Integer> map = dao.selectDeptTotalSalary();

//		System.out.println(map.toString());  
	
		 // 세로로 출력해보기....
		for(String key : map.keySet()) {
			System.out.println(key + " : " + map.get(key));
		}
		
	}
	
	
	
	
	
	/**
	 * 10. 직급별 급여 평균 조회
	 *  // DB 조회 결과를 HashMap<String, Double>에 옮겨 담아서 반환 
		// 직급명, 급여 평균(소수점 첫째자리까지) 조회
	 */
	public void selectJobAvgSalary() {
		
		System.out.println("<직급별 급여 평균 조회>");
		
		Map<String, Double> map = dao.selectJobAvgSalary();
		
//		System.out.println(map);
		
		for(String key : map.keySet()) {
			System.out.println(key + " : " + map.get(key));
		}
		
		
	}
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	

}
