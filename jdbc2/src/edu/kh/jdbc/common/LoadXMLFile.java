package edu.kh.jdbc.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.Scanner;

public class LoadXMLFile {
	public static void main(String[] args) {
		
		// XML 파일 읽어오기 (Properties, FileInputStream)
		
		try {
			Properties prop = new Properties(); // Map<String, String>
			
			// driver.xml 파일을 읽어오기 위한 InputStrem 객체 생성
			FileInputStream fis = new FileInputStream("driver.xml");
			
			// 연결된 driver.xml 파일에 있는 내용을 모두 읽어와
			// Properties 객체에 K:V 형식으로 저장
			prop.loadFromXML(fis);
			
			System.out.println(prop);  //__읽어는 왔는데 하나씩 꺼내 쓰지는 못함
			
			
			
			//__ 여러 개가 모여있는 객체에서 하나씩 꺼내쓰는 법
			// Property : 속성(데이터)
			// prop.getProperty("key") : key가 일치하는 속성(데이터)를 얻어옴.
			
			String driver = prop.getProperty("driver");
			String url = prop.getProperty("url");
			String user = prop.getProperty("user");
			String password = prop.getProperty("password");
			
			
			System.out.println();
			
			
			// driver.xml 파일에서 읽어온 값들을 이용해 Connection 생성
			Class.forName(driver);  //oracle.jdbc.driver.OracleDriver
			Connection conn = DriverManager.getConnection(url, user, password);  
														//__xml에서 얻어온 값들임
			
			System.out.print(conn);  
			//__ 실행하고 나서 oracle.jdbc.driver.T4CConnection@4009e306라고 뜨면 실제로 연결되지는 않았지만 연결 성공한 상태
			
			 /* 왜 XML파일을 이용해서 DB 연결 정보를 읽어와야 될까?
			  * 
			  * 1. 코드 중복 제거
			  * 
			  * 2. 별도 관리 용도 
			  * 	- 별도 파일을 이용해서 수정이 용이
			  * 
			  * 3. 재 컴파일을 진행하지 않기 위해서
			  * 	- 코드가 길수록 컴파일에 소요되는 시간이 크다
			  * 	  -> 코드 수정으로 인한 컴파일 소요시간을 없앰.
			  * 	     (파일의 내용을 읽어오는 코드만 작성해두면
			  * 		  Java 코드 수정 없이, 파일 내용만 수정하면
 			  * 		  재 컴파일은 수행되지 않는다.)
 			  * 
 			  * 4. XML 파일에 작성된 문자열 형태를 그대로 읽어오기 떄문에
 			  *    SQL 작성 시 좀 더 편리해진다.
			  * */
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
}
