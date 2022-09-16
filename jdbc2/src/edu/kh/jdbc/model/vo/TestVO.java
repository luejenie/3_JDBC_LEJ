package edu.kh.jdbc.model.vo;

public class TestVO {
	private int testNo;
	private String testTitle;
	private String testContent;
	
	// 기본생성자
	public TestVO() {}
	
	// 매개변수 생성자(모든 필드 초기화)
	public TestVO(int testNo, String testTitle, String testContent) {
		super();
		this.testNo = testNo;
		this.testTitle = testTitle;
		this.testContent = testContent;
	}
	
	
	// getter/setter
	public String getTestTitle() {
		return testTitle;
	}

	public void setTestTitle(String testTitle) {
		this.testTitle = testTitle;
	}

	public String getTestContent() {
		return testContent;
	}

	public void setTestContent(String testContent) {
		this.testContent = testContent;
	}

	public int getTestNo() {
		return testNo;
	}

	
	
	// toString() 오버라이딩   _Object에서 상속받음
	@Override
	public String toString() {
		return "TestVO [testNo=" + testNo + ", testTitle=" + testTitle + ", testContent=" + testContent + "]";
	}
	

	
	


}
