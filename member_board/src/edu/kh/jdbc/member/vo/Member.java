package edu.kh.jdbc.member.vo;

// VO(Value Object) : 값을 저장하는 용도의 객체
// - JDBC에서는 테이블의 한 행의 조회 결과 또는 
//   (한 행의) 삽입, 수정을 위한 데이터를 저장하는 용도의 객체 
/**
 * @author user1
 *
 */
public class Member {
	
	private int memberNo;
	private String memberId;
	private String memberPw;
	private String memberName;
	private String memberGender;  //_char아닌 String -- DB의 CHAR == Java의 String
	private String enrollDate;   // 자바에서 date 아닌 String. -- DB에서 DATE가져올때 TO_CHAR 사용할 거ㅣㅇㅁ
	private String secessionFlag;
	
	// 기본 생성자
	public Member() {}

	// 매개변수 생성자   __회원가입에 입력받을 4개만!
	public Member(String memberId, String memberPw, String memberName, String memberGender) {
		super();
		this.memberId = memberId;
		this.memberPw = memberPw;
		this.memberName = memberName;
		this.memberGender = memberGender;
	}
	
	
	public Member(int memberNo, String memberId, String memberName, String memberGender, String enrollDate) {
		super();
		this.memberNo = memberNo;
		this.memberId = memberId;
		this.memberName = memberName;
		this.memberGender = memberGender;
		this.enrollDate = enrollDate;
	}
	
	
//	public Member(String memberId, String memberName, String memberGender) {
//		super();
//		this.memberId = memberId;
//		this.memberName = memberName;
//		this.memberGender = memberGender;
//	}

	
	
	public int getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberPw() {
		return memberPw;
	}

	public void setMemberPw(String memberPw) {
		this.memberPw = memberPw;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberGender() {
		return memberGender;
	}

	public void setMemberGender(String memberGender) {
		this.memberGender = memberGender;
	}

	public String getEnrollDate() {
		return enrollDate;
	}

	public void setEnrollDate(String enrollDate) {
		this.enrollDate = enrollDate;
	}

	public String getSecessionFlag() {
		return secessionFlag;
	}

	public void setSecessionFlag(String secessionFlag) {
		this.secessionFlag = secessionFlag;
	}
	
	
	//_ toString없이 디버그모드로.
	
	
	

}
