package re.lej.jdbc.common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class CreateXMLFile {
	
	public static void main(String[] args) {
		
		// XML(eXtensible Markup Language) : 단순화된 데이터 기술 형식
			//__ 어떤 글자를 태그 형태로 바꿔서 특정한 의미를 가진 언어로 만드는 것. 마크로 만드는 것.
			//__ Markdown 언어도 있음.
		
		// XML에 저장되는 데이터의 형식은 Key : Value 형식(Map)이다.
		// -> Key, Value 모두 String(문자열) 형식
		// -> Map<String, String>
		
		// XML 파일을 읽고, 쓰기 위한 IO 관련 클래스 필요
		
		// * Properties 컬렉션 객체 *    __XML을 편리하게 사용하기 위한 컬렉션
		//	- Map의 후손 클래스
		//	- Key, Value 모두 String(문자열) 형식
		//	- XML 파일을 읽고, 쓰는데 특화된 메서드 제공
		
		
		// __ 이 클래스를 실행하면 XML 파일이 만들어질 수 있도록.
		
		try {
			Scanner sc = new Scanner(System.in);
			
			// Properties 객체 생성
			Properties prop = new Properties();
					//_Properties 객체가 참조하는 참조변수
			
			System.out.print("생성할 파일 이름 : ");
			String fileName = sc.nextLine();
			
			// FileOutputStream 생성  __ 현재 입력받은 이름으로 확장자가 생성  (파일명.확장자)
			FileOutputStream fos = new FileOutputStream( fileName + ".xml");
			
			// Properties 객체를 이용해서 XML 파일 생성
			prop.storeToXML(fos, fileName + ".xml file");
			
			System.out.println(fileName + ".xml 파일 생성 완료");
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}



















