package edu.kh.jdbc.member.model.dao;

import static edu.kh.jdbc.common.JDBCTemplate.*;  //_ 서비스에도 복사

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.kh.jdbc.member.vo.Member;


public class MemberDAO {
	
//	1 DAO 세팅 (필드, 생성자)
	
	//필드(==멤버 변수)
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private Properties prop;
	
	// 기본 생성자
	public MemberDAO() { 
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream("member-query.xml"));
		} catch(Exception e) {  //__외부에서 가져올 때 예외 있을 확률이 높음
			e.printStackTrace();
		}
	}

	
//	10
	/**  
	 * @param conn
	 * @return memberList
	 * @throws Exception
	 */
	public List<Member> selectAll(Connection conn) throws Exception {
		// 결과 저장용 변수 선언
		List<Member> memberList = new ArrayList<>();
		
		try {
			// SQL 얻어오기
			String sql = prop.getProperty("selectAll");
			
			// Statement 객체 생성
			stmt = conn.createStatement();
			
			// SQL(SELECT) 수행 후 결과(ResultSet) 반환 받기
			rs = stmt.executeQuery(sql);
			
			// 반복문(while)을 이용해서 조회 결과의 각 행에 접근 후     <- 여러줄 반환 while
			// 컬럼 값을 얻어와 Member 객체에 저장 후 List에 추가
			
			while(rs.next()) {
			//아이디, 이름, 성별
								
				String memberId = rs.getString("MEMBER_ID");
				String memberName = rs.getString("MEMBER_NM");
				String memberGender = rs.getString("MEMBER_GENDER");
				
				// 풀이1  매개변수 만들어서.
//				Member member = new Member(memberId, memberName, memberGender);
				
				// 풀이2 _ 더 많이 사용. 기본 생성자 객체 생성하고 setter로.
				Member member = new Member();
				member.setMemberId(memberId);
				member.setMemberName(memberName);
				member.setMemberGender(memberGender);
				
				
				memberList.add(member);
			}
			
			
		} finally {
			// JDBC 객체 자원 반환
			close(rs);
			close(stmt);
		}
		
		// 조회 결과를 옮겨 담은 List 반환
		return memberList;
	}


	
	
	/** 회원 정보 수정 DAO
	 * @param conn
	 * @param member
	 * @return result
	 * @throws Exception
	 */
	public int updateMember(Connection conn, Member member) throws Exception {
		// 결과 저장용 변수 생성
		int result = 0; // UPDATE 반영 결과 행의 개수(정수형)를 저장하기 위한 변수
		
		try {
			// SQL 얻어오기
			String sql = prop.getProperty("updateMember");
			
			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// ?에 알맞은 값 대입
			pstmt.setString(1, member.getMemberName());
			pstmt.setString(2, member.getMemberGender());
			pstmt.setInt(3, member.getMemberNo());
			
			// SQL 수행 후 결과 반환 받기
			result = pstmt.executeUpdate();
			
		} finally {
			// JDBC 객체 자원 반환
			close(pstmt);
		}

		// 수정 결과 반환
		return result;
	}


	
	/** 비밀번호 변경 DAO
	 * @param conn
	 * @param currentPw
	 * @param newPw1
	 * @param memberNo
	 * @return result
	 * @throws Exception
	 */
	public int updatePw(Connection conn, String currentPw, String newPw1, int memberNo) throws Exception{
		// 결과 저장용 변수 생성
		int result = 0; // UPDATE 반영 결과 행의 개수(정수형)를 저장하기 위한 변수
		
		try {
			// SQL 얻어오기
			String sql = prop.getProperty("updatePw");
			
			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// ?에 알맞은 값 대입
			pstmt.setString(1, newPw1);
			pstmt.setInt(2, memberNo);
			pstmt.setString(3, currentPw);
			
			// SQL 수행 후 결과 반환 받기
			result = pstmt.executeUpdate();
			
		} finally {
			// JDBC 객체 자원 반환
			close(pstmt);
		}

		// 수정 결과 반환
		return result;
	}


	/** 회원 탈퇴 DAO
	 * @param conn
	 * @param memberPw
	 * @param memberNo
	 * @return result
	 * @throws Exception
	 */
	public int secession(Connection conn, String memberPw, int memberNo) throws Exception{
		
		int result = 0;
		
		try {
			String sql = prop.getProperty("secession");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, memberNo);
			pstmt.setString(2, memberPw);
			
			result = pstmt.executeUpdate();	

		} finally {
			close(pstmt);
		}
		
		return result;
	}
	

}
