package edu.kh.emp.run;

import edu.kh.emp.view.EmployeeView;

// 실행용 클래스
public class EmployeeRun {
	public static void main(String[] args) {
		
		new EmployeeView().displayMenu();
		
	}

}


//_ Run - View - Service - DAO - VO 순서로 코드짜기
//_ Run 실행 전 View - Service - DAO 사이에서 뻉뺑 돌다가 가끔 VO 사용하고 그럼.
