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
SELECT COUNT(*) FROM "MEMBER"
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

















