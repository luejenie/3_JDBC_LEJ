package re.lej.jdbc.model.service;

import static re.lej.jdbc.common.JDBCTemplate.*;

import java.sql.Connection;

import re.lej.jdbc.model.dao.DAO;

public class Service {
	
	private DAO dao = new DAO();

	/** 아이디 중복 검사 서비스
	 * @param memberId
	 * @return result
	 */
	public int idDupCheck(String memberId) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.idDupCheck(conn, memberId);
		close(conn);
				
		return result;
	}
	
	
	
	
	
	
	

}
