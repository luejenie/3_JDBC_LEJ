-- SYS 관리자 계정   

--// 계정방식을 쉽게 쓸 수 있게 하는 명령어
ALTER SESSION SET "_ORACLE_SCRIPT" = TRUE;

-- 사용자 계정 생성    // 아이디 / 비밀번호
CREATE USER member_lej IDENTIFIED BY member1234; 

-- 생성한 사용자 계정에 권한 부여  
GRANT CONNECT, RESOURCE, CREATE VIEW TO member_lej;

-- 테이블 스페이스 할당   //무제한으로 주는 것. 10GB정도.
ALTER USER member_lej DEFAULT 
TABLESPACE SYSTEM QUOTA UNLIMITED ON SYSTEM;


------------------------------------------------------

-- member_lej 계정

-- 회원 테이블
-- 회원 번호, 아이디, 비밀번호, 이름, 성별, 가입일, 탈퇴여부
CREATE TABLE "MEMBER"(
	MEMBER_NO NUMBER PRIMARY KEY, -- //문자열보다 숫자 비교가 속도가 빠름 -> 기본키 아이디 아닌 숫자로.
	MEMBER_ID VARCHAR2(30) NOT NULL,
	MEMBER_PW VARCHAR2(30) NOT NULL,
	MEMBER_NM VARCHAR2(30) NOT NULL,
	MEMBER_GENDER CHAR(1) CHECK( MEMBER_GENDER IN ('M', 'F') ),
	ENROLL_DATE DATE DEFAULT SYSDATE,
	SECESSION_FL CHAR(1) DEFAULT 'N' CHECK( SECESSION_FL IN ('Y', 'N'))
);

COMMENT ON COLUMN "MEMBER".MEMBER_NO IS '회원 번호';
COMMENT ON COLUMN "MEMBER".MEMBER_ID IS '회원 아이디';
COMMENT ON COLUMN "MEMBER".MEMBER_PW IS '회원 비밀번호';
COMMENT ON COLUMN "MEMBER".MEMBER_NM IS '회원 이름';
COMMENT ON COLUMN "MEMBER".MEMBER_GENDER IS '회원 성별';
COMMENT ON COLUMN "MEMBER".ENROLL_DATE IS '회원 가입일';
COMMENT ON COLUMN "MEMBER".SECESSION_FL IS '탈퇴여부(Y/N)';


-- 회원 번호 시퀀스 생성
CREATE SEQUENCE SEQ_MEMBER_NO 
START WITH 1	 -- // 1부터 시작해서 1씩 증가
INCREMENT BY 1   --//START WITH, INCREMENT 둘다 없어도 됨. 기본값이 1
NOCYCLE			 --// 반복없음
NOCACHE;		 


-- 회원 가입 INSERT
INSERT INTO "MEMBER"
VALUES( SEQ_MEMBER_NO.NEXTVAL, 'user01', 'pass01', '유저일', 'M', DEFAULT, DEFAULT);

INSERT INTO "MEMBER"
VALUES( SEQ_MEMBER_NO.NEXTVAL, 'user04', 'pass04', '유저사', 'F', DEFAULT, DEFAULT);

INSERT INTO "MEMBER"
VALUES( SEQ_MEMBER_NO.NEXTVAL, 'user03', 'pass03', '유저삼', 'F', DEFAULT, DEFAULT);

SELECT * FROM "MEMBER";

COMMIT;

SELECT * FROM "MEMBER";

-- 아이디 중복 확인
-- (중복되는 아이디가 입력되어도 탈퇴한 계정이면 중복 X라고 판별)
SELECT COUNT(*), COUNT(*)+1 FROM "MEMBER"
WHERE MEMBER_ID = 'user01'
AND SECESSION_FL = 'N';
--> ID가 user01 이면서 탈퇴하지 않은 회원 조회

-- 중복이면 1, 아니면 0 조회
SELECT * FROM "MEMBER";


-- 로그인(아이디, 비밀번호 일치 && 탈퇴X 회원)
SELECT MEMBER_NO, MEMBER_ID, MEMBER_NM, MEMBER_GENDER,
	   TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일" HH24:MI:SS') ENROLL_DATE 
FROM "MEMBER"
WHERE MEMBER_ID = 'user01'
AND MEMBER_PW = 'pass01'
AND SECESSION_FL = 'N';


-- 회원 목록 조회(아이디, 이름, 성별)
-- 탈퇴 회원 미포함
-- 가입일 내림차순 
	--> MEMBER_NO 내림차순(나중에 가입한 회원의 번호가 더 큼)
SELECT MEMBER_ID, MEMBER_NM, MEMBER_GENDER
FROM "MEMBER"
WHERE SECESSION_FL = 'N'
ORDER BY MEMBER_NO DESC;
-- // 날짜 데이터는 정렬할 때 속도가 오래 걸림. 
-- // MEMBER_NO는 ENROLL_DATE와 비례하여 나중에 가입할 수록 번호가 커짐.
-- // 날짜보다 숫자가 속도가 빠르기 때문에 MEMBER_NO 숫자를 이용


-- 회원 정보 수정(이름, 성별)
UPDATE "MEMBER" SET
MEMBER_NM = '바꾼이름',  -- 입력
MEMBER_GENDER = 'F'    -- 입력
WHERE MEMBER_NO = '1'; -- loginMember.getMembetNo()

SELECT * FROM MEMBER;
ROLLBACK;

-- 비밀번호 변경
UPDATE "MEMBER" SET
MEMBER_PW = '새비밀번호'
WHERE MEMBER_NO = 1
AND MEMBER_PW = '현재비밀번호';

ROLLBACK;




-- 회원 탈퇴 (SECESSION_FL 컬럼의 값을 'Y'로 변경)
-- // (DELETE 아님)
UPDATE "MEMBER" SET
SECESSION_FL = 'Y'
WHERE MEMBER_NO = ?
AND MEMBER_PW = ?;


SELECT * FROM MEMBER;


INSERT INTO "MEMBER"
VALUES( SEQ_MEMBER_NO.NEXTVAL, 'user04', 'pass04', '유저사', 'F', DEFAULT, DEFAULT);
COMMIT;

DELETE FROM "MEMBER"
WHERE MEMBER_ID = 'user06';

COMMIT;


SELECT * FROM MEMBER;


--------------------------------------------------------------------

-- 게시판 테이블
CREATE TABLE "BOARD" (
	BOARD_NO NUMBER CONSTRAINT BOARD_PK PRIMARY KEY,
	BOARD_TITLE VARCHAR2(500) NOT NULL,
	BOARD_CONTENT VARCHAR2(4000) NOT NULL,
	CREATE_DT DATE DEFAULT SYSDATE,
	READ_COUNT NUMBER DEFAULT 0,   -- //DEFAULT를 안쓰면 처음 값에 NULL이 들어감
	DELETE_FL CHAR(1) DEFAULT 'N' CHECK( DELETE_FL IN ('N', 'Y')),   --//'B'추가해서 블라인드도 할 수 있음.
	MEMBER_NO NUMBER CONSTRAINT BOARD_WRITER_FK REFERENCES "MEMBER"
);

COMMENT ON COLUMN "BOARD".BOARD_NO IS '게시글 번호';
COMMENT ON COLUMN "BOARD".BOARD_TITLE IS '게시글 제목';
COMMENT ON COLUMN "BOARD".BOARD_CONTENT IS '게시글 내용';
COMMENT ON COLUMN "BOARD".CREATE_DT IS '게시글 작성일';
COMMENT ON COLUMN "BOARD".READ_COUNT IS '게시글 조회수';
COMMENT ON COLUMN "BOARD".DELETE_FL IS '게시글 삭제여부';
COMMENT ON COLUMN "BOARD".MEMBER_NO IS '작성자 회원 번호';


-- 게시판 번호 시퀀스
CREATE SEQUENCE SEQ_BOARD_NO NOCACHE;


-- 게시판 샘플 데이터
SELECT * FROM "MEMBER"
WHERE SECESSION_FL = 'N';

--//탈퇴 안한 회원 중 user03 유저삼 회원 선택 -> 작성자
INSERT INTO BOARD
VALUES(SEQ_BOARD_NO.NEXTVAL, '샘플 제목1', '샘플 내용 1 입니다.',
		TO_DATE('2022-09-20 10:30:12', 'YYYY-MM-DD HH24:MI:SS'),
		DEFAULT, DEFAULT, 3);
	
INSERT INTO BOARD
VALUES(SEQ_BOARD_NO.NEXTVAL, '샘플 제목2', '샘플 내용 2 입니다.',
		TO_DATE('2022-09-21 20:17:12', 'YYYY-MM-DD HH24:MI:SS'),
		DEFAULT, DEFAULT, 3);

INSERT INTO BOARD
VALUES(SEQ_BOARD_NO.NEXTVAL, '샘플 제목3', '샘플 내용 3 입니다.',
		DEFAULT, DEFAULT, DEFAULT, 3);

INSERT INTO BOARD
VALUES(SEQ_BOARD_NO.NEXTVAL, '샘플 제목4', '샘플 내용 4 입니다.',
		DEFAULT, DEFAULT, DEFAULT, 3);
	
	
	
	
SELECT * FROM "BOARD";

COMMIT;


--------------------------------------------------------

-- 댓글 테이블 생성
CREATE TABLE "COMMENT"(    --//COMMENT 주석에 사용중이기 때문에 반드시! "" 붙여야 함. 
	COMMENT_NO NUMBER,
	COMMENT_CONTENT VARCHAR2(900) NOT NULL,
	CREATE_DT DATE DEFAULT SYSDATE,
	DELETE_FL CHAR(1) DEFAULT 'N' CHECK(DELETE_FL IN ('N', 'Y')),
	MEMBER_NO NUMBER REFERENCES "MEMBER",
	BOARD_NO NUMBER REFERENCES "BOARD",	 --// 몇 번 게시글에 글을 남겼는지
	CONSTRAINT COMMENT_PK PRIMARY KEY(COMMENT_NO)
)

COMMENT ON COLUMN "COMMENT".COMMENT_NO IS '댓글 번호';
COMMENT ON COLUMN "COMMENT".COMMENT_CONTENT IS '댓글 내용';
COMMENT ON COLUMN "COMMENT".CREATE_DT IS '댓글 작성일';
COMMENT ON COLUMN "COMMENT".DELETE_FL IS '댓글 삭제 여부';
COMMENT ON COLUMN "COMMENT".MEMBER_NO IS '댓글 작성자 회원 번호';
COMMENT ON COLUMN "COMMENT".BOARD_NO IS '댓글이 작성된 게시글 번호';


-- 댓글 번호 시퀀스 생성
CREATE SEQUENCE SEQ_COMMENT_NO NOCACHE;

-- 댓글 샘플 데이터 삽입  (3번 회원의 3번 게시글)
SELECT * FROM "BOARD"
WHERE DELETE_FL = 'N';

INSERT INTO "COMMENT"
VALUES(SEQ_COMMENT_NO.NEXTVAL, '댓글 샘플 1번', DEFAULT, DEFAULT, 3, 3);

INSERT INTO "COMMENT"
VALUES(SEQ_COMMENT_NO.NEXTVAL, '댓글 샘플 2번', DEFAULT, DEFAULT, 3, 3);

INSERT INTO "COMMENT"
VALUES(SEQ_COMMENT_NO.NEXTVAL, '댓글 샘플 3번', DEFAULT, DEFAULT, 3, 3);


SELECT * FROM "COMMENT";
COMMIT;



-- 게시글 목록 조회(댓글 수 포함 -> 상관쿼리(스칼라쿼리)  //메인쿼리 해석 후 서브쿼리 해석)
SELECT BOARD_NO, BOARD_TITLE, MEMBER_NM, READ_COUNT, 
--
	 CASE 
		 WHEN SYSDATE - CREATE_DT < 1/24/60   --// 현재 시간과 게시글 작성시간이 1분 미만인 경우
		 THEN FLOOR((SYSDATE - CREATE_DT) * 24 * 60 * 60) || '초 전'
		 WHEN SYSDATE - CREATE_DT < 1/24  
		 THEN FLOOR((SYSDATE - CREATE_DT) * 24 * 60) || '분 전'
		 WHEN SYSDATE - CREATE_DT < 1  
		 THEN FLOOR((SYSDATE - CREATE_DT) * 24) || '시간 전'
		 ELSE TO_CHAR(CREATE_DT, 'YYYY-MM-DD')
	 END CREATE_DT, 
--
	 (SELECT COUNT(*) FROM "COMMENT" C
	  WHERE C.BOARD_NO = B.BOARD_NO) COMMENT_COUNT  
FROM "BOARD" B
JOIN "MEMBER" USING(MEMBER_NO)
WHERE DELETE_FL = 'N'
ORDER BY BOARD_NO DESC;  --//최신 글부터. CREATE_DATE보다 빠른 속도.



/* 자바/SQL/자바스크립트 이용
 * ~초 전
 * ~분 전
 * ~시간 전
 */

-- 1일 11시간 = 1.47
-- 24시간 차이 == 1
-- 12시간 차이 == 0.5 == 1/24 * 12
-- 1시간 차이 == 1/24
-- 2시간 차이 == 1/24 * 2
-- 30분 차이 == 1/24 /60 * 30
-- 1분 차이 == 1/24/60
-- 1초 차이 == 1/24/60/60

-- 1일  == 1
-- 1시간 == 1/24
-- 1분  == 1/24/60
-- 1초  == 1/24/60/60

SELECT FLOOR((SYSDATE - TO_DATE('2022-09-22')) *24 *60 *60)
FROM DUAL;



-- 게시글 상세 조회(게시글 내용 조회)
SELECT BOARD_NO, BOARD_TITLE, BOARD_CONTENT, 
	   MEMBER_NO, MEMBER_NM, READ_COUNT,
	   TO_CHAR(CREATE_DT, 'YYYY-MM-DD HH24:MI:SS') CREATE_DT
FROM "BOARD"
JOIN "MEMBER" USING(MEMBER_NO)
WHERE DELETE_FL = 'N'
AND BOARD_NO = 3;  -- 바뀌는 부분


-- 특정 게시글의 댓글 목록 조회(작성일 오름차순)   // SNS-> 최신순 / 카페-> 오래된순
SELECT COMMENT_NO, COMMENT_CONTENT,
	   MEMBER_NO, MEMBER_NM, 
	   TO_CHAR(CREATE_DT, 'YYYY-MM-DD HH24:MI:SS') CREATE_DT
FROM "COMMENT"
JOIN "MEMBER" USING(MEMBER_NO)
WHERE DELETE_FL = 'N'
AND BOARD_NO = 3
ORDER BY COMMENT_NO ASC;


-- 상세 조회된 게시글의 조회 수 증가
UPDATE "BOARD" SET
READ_COUNT = READ_COUNT + 1   
WHERE BOARD_NO = 3;
--//오라클에서 =는 같다를 의미하지만, UPDATE에 한해서는 대입을 의미. 오른쪽을 왼쪽에 대입

COMMIT;




-- 댓글 등록
INSERT INTO "COMMENT"
VALUES(SEQ_COMMENT_NO.NEXTVAL, '댓글 샘플 3번', DEFAULT, DEFAULT, 3, 3);


SELECT * FROM "COMMENT";

-- 댓글 수정
UPDATE "COMMENT" SET
COMMENT_CONTENT = '댓글 수정 테스트'
WHERE COMMENT_NO = 6 ;

ROLLBACK;


SELECT * FROM "COMMENT";

-- 게시글 수정
UPDATE "BOARD" SET
BOARD_TITLE = ?,
BOARD_CONTENT = ?
WHERE BOARD_NO = ?;


--게시글 삭제
UPDATE "BOARD" SET
DELETE_FL = 'Y'
WHERE BOARD_NO = ?


-- 다음 게시글 번호 생성
SELECT SEQ_BOARD_NO.NEXTVAL FROM DUAL;  --하나씩 번호가 증가함.



-- 게시글 검색
-- 검색어가 포함되는 게시글의 목록을 조회
SELECT BOARD_NO, BOARD_TITLE, MEMBER_NM, READ_COUNT, 
	 CASE 
		 WHEN SYSDATE - CREATE_DT < 1/24/60   --// 현재 시간과 게시글 작성시간이 1분 미만인 경우
		 THEN FLOOR((SYSDATE - CREATE_DT) * 24 * 60 * 60) || '초 전'
		 WHEN SYSDATE - CREATE_DT < 1/24  
		 THEN FLOOR((SYSDATE - CREATE_DT) * 24 * 60) || '분 전'
		 WHEN SYSDATE - CREATE_DT < 1  
		 THEN FLOOR((SYSDATE - CREATE_DT) * 24) || '시간 전'
		 ELSE TO_CHAR(CREATE_DT, 'YYYY-MM-DD')
	 END CREATE_DT, 
	 (SELECT COUNT(*) FROM "COMMENT" C
	  WHERE C.BOARD_NO = B.BOARD_NO) COMMENT_COUNT  
FROM "BOARD" B
JOIN "MEMBER" USING(MEMBER_NO)
WHERE DELETE_FL = 'N'
-- 제목 검색  							  -- ? 양 옆에 홑따옴표 남기지 말기!!
AND BOARD_TITLE LIKE '%' || ? || '%'  -- 연결연산자 이용 <- setString하면 ''가 저절로 붙어서 오기 떄문에 '%'?'%' 이런 모양이 되어버림.
-- 내용 검색
AND BOARD_CONTENT LIKE '%' || ? || '%'
-- 제목 + 내용 검색
-- 제목 또는 내용
AND (BOARD_TITLE LIKE '%' || ? || '%'
OR BOARD_CONTENT LIKE '%' || ? || '%')
-- 작성자
AND MEMBER_NM LIKE '%' || ? || '%'
ORDER BY BOARD_NO DESC;


SELECT * FROM "BOARD";




































