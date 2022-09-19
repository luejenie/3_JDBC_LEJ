package re.lej.jdbc.model.dao;

import static re.lej.jdbc.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class DAO {
	
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	private Properties prop = null;
	
	public DAO() {
		
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream("re-query.xml"));

		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public int idDupCheck(Connection conn, String memberId) throws Exception {
		
		int result = 0;
		
		try {
			String sql = prop.getProperty("idDupCheck");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
			}
		
		
		} finally {
			
		}
		return 0;
	}
	
	

}
