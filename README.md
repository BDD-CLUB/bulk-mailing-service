## OverView

Spring-Batch-Bulk-Mailing-Service는 Spring Batch 프레임워크를 기반으로 만든 대량 메일 전송 시스템입니다.

많은 사용자에게 일괄적으로 동일한 내용의 메일을 보낼 때 사용하면 됩니다. 청크 단위로 사용자에게 메일을 보내며 예상치 못한 예외가 발생해도 Retry 기능을 통해 메일 전송을 재시도 합니다. 간단한 MD 포맷팅을
지원하고 양식 또한 깔끔하고 정돈되어 있습니다.

## Quick Start Guide

### 1. Docker를 사용해 MYSQL 설치

Spring-Batch는 데이터베이스에 로그를 남길 수 있는 테이블이 없으면 실행이 되지 않기 때문에 필수적으로 데이터베이스를 설치 해야 합니다.

~~~yml
version: "3"
services:
  db:
    image: mysql:8.0
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: 1234 # 사용자에 맞게 MYSQL ROOT 비밀번호 수정
      MYSQL_DATABASE: springBatch # 사용자에 맞게 이름 수정
      MYSQL_USER: user # 사용자에 맞게 이름 변경  
      MYSQL_PASSWORD: 1234 # 사용자에 맞게 비밀번호 변경
      TZ: Asia/Seoul
~~~

docker-compose.yml 파일 생성 후 docker-compose up 명령어를 사용해 도커 실행

### 2. 프로젝트 clone

~~~
https://github.com/BDD-CLUB/BDD-Bulk-Mailing-Service.git
~~~

### 3. application.yml 파일 추가

데이터베이스 연결과 이메일 연동, API 사용을 위한 패스워드를 설정하기 위해서 yml 파일을 추가합니다.

파일 경로는 spring-batch > src > main > resources > application.yml 입니다.

~~~yml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: # fix
    password: # fix
    properties:
      mail.smtp.starttls.enable: true
      mail.smtp.auth: true
    protocol: smtp
  datasource:
    url: jdbc:mysql://localhost:3306/{}?serverTimezone=Asia/Seoul&characterEncoding=UTF-8 # fix {}는 입력하고 제거할 것.
    username: user # fix
    password: 1234 # fix
    driver-class-name: com.mysql.cj.jdbc.Driver
  batch:
    jdbc:
      initialize-schema: always
    job:
      enabled: false
      name: ${job.name:NONE}
  jpa:
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.MySQL8Dialect
    defer-datasource-initialization: true
    properties:
      hibernate:
        format_sql: true
    show-sql: true

~~~

### 4. Database Table 생성

위의 설명대로 DB에 로그를 남기기 위한 테이블이 있어야 하며, 생성하기 위한 SQL 문은 아래와 같습니다.

~~~
SELECT * FROM springBatch.BATCH_JOB_EXECUTION;CREATE TABLE `BATCH_JOB_EXECUTION` (
  `JOB_EXECUTION_ID` bigint NOT NULL,
  `VERSION` bigint DEFAULT NULL,
  `JOB_INSTANCE_ID` bigint NOT NULL,
  `CREATE_TIME` datetime(6) NOT NULL,
  `START_TIME` datetime(6) DEFAULT NULL,
  `END_TIME` datetime(6) DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `EXIT_CODE` varchar(2500) DEFAULT NULL,
  `EXIT_MESSAGE` varchar(2500) DEFAULT NULL,
  `LAST_UPDATED` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`JOB_EXECUTION_ID`),
  KEY `JOB_INST_EXEC_FK` (`JOB_INSTANCE_ID`),
  CONSTRAINT `JOB_INST_EXEC_FK` FOREIGN KEY (`JOB_INSTANCE_ID`) REFERENCES `BATCH_JOB_INSTANCE` (`JOB_INSTANCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `BATCH_JOB_EXECUTION_CONTEXT` (
  `JOB_EXECUTION_ID` bigint NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` text,
  PRIMARY KEY (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_CTX_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `BATCH_JOB_EXECUTION_PARAMS` (
  `JOB_EXECUTION_ID` bigint NOT NULL,
  `PARAMETER_NAME` varchar(100) NOT NULL,
  `PARAMETER_TYPE` varchar(100) NOT NULL,
  `PARAMETER_VALUE` varchar(2500) DEFAULT NULL,
  `IDENTIFYING` char(1) NOT NULL,
  KEY `JOB_EXEC_PARAMS_FK` (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_PARAMS_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `BATCH_JOB_EXECUTION_SEQ` (
  `ID` bigint NOT NULL,
  `UNIQUE_KEY` char(1) NOT NULL,
  UNIQUE KEY `UNIQUE_KEY_UN` (`UNIQUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `BATCH_JOB_INSTANCE` (
  `JOB_INSTANCE_ID` bigint NOT NULL,
  `VERSION` bigint DEFAULT NULL,
  `JOB_NAME` varchar(100) NOT NULL,
  `JOB_KEY` varchar(32) NOT NULL,
  PRIMARY KEY (`JOB_INSTANCE_ID`),
  UNIQUE KEY `JOB_INST_UN` (`JOB_NAME`,`JOB_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `BATCH_JOB_SEQ` (
  `ID` bigint NOT NULL,
  `UNIQUE_KEY` char(1) NOT NULL,
  UNIQUE KEY `UNIQUE_KEY_UN` (`UNIQUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `BATCH_STEP_EXECUTION` (
  `STEP_EXECUTION_ID` bigint NOT NULL,
  `VERSION` bigint NOT NULL,
  `STEP_NAME` varchar(100) NOT NULL,
  `JOB_EXECUTION_ID` bigint NOT NULL,
  `CREATE_TIME` datetime(6) NOT NULL,
  `START_TIME` datetime(6) DEFAULT NULL,
  `END_TIME` datetime(6) DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `COMMIT_COUNT` bigint DEFAULT NULL,
  `READ_COUNT` bigint DEFAULT NULL,
  `FILTER_COUNT` bigint DEFAULT NULL,
  `WRITE_COUNT` bigint DEFAULT NULL,
  `READ_SKIP_COUNT` bigint DEFAULT NULL,
  `WRITE_SKIP_COUNT` bigint DEFAULT NULL,
  `PROCESS_SKIP_COUNT` bigint DEFAULT NULL,
  `ROLLBACK_COUNT` bigint DEFAULT NULL,
  `EXIT_CODE` varchar(2500) DEFAULT NULL,
  `EXIT_MESSAGE` varchar(2500) DEFAULT NULL,
  `LAST_UPDATED` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`STEP_EXECUTION_ID`),
  KEY `JOB_EXEC_STEP_FK` (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_STEP_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `BATCH_STEP_EXECUTION_CONTEXT` (
  `STEP_EXECUTION_ID` bigint NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` text,
  PRIMARY KEY (`STEP_EXECUTION_ID`),
  CONSTRAINT `STEP_EXEC_CTX_FK` FOREIGN KEY (`STEP_EXECUTION_ID`) REFERENCES `BATCH_STEP_EXECUTION` (`STEP_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `BATCH_STEP_EXECUTION_SEQ` (
  `ID` bigint NOT NULL,
  `UNIQUE_KEY` char(1) NOT NULL,
  UNIQUE KEY `UNIQUE_KEY_UN` (`UNIQUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `mail` (
  `id` bigint NOT NULL,
  `message` varchar(2000) NOT NULL,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `mail_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `member` (
  `id` bigint NOT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `member_role` (
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `member_role_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `member_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

~~~

![테이블사진](https://github.com/02ggang9/02ggang9.github.io/blob/master/_posts/images/bdd/mail/reversing.png?raw=true)

### 5. Spring boot 실행 후 localhost:8080 접속

- 메인 페이지(/index.html)

![테이블사진](https://github.com/02ggang9/02ggang9.github.io/blob/master/_posts/images/bdd/mail/quickStart/step1.png?raw=true)

- 메일을 발송할 회원 저장(/member)

![테이블사진](https://github.com/02ggang9/02ggang9.github.io/blob/master/_posts/images/bdd/mail/quickStart/step2.png?raw=true)

- 저장된 회원 조회(/members)

![테이블사진](https://github.com/02ggang9/02ggang9.github.io/blob/master/_posts/images/bdd/mail/quickStart/step3.png?raw=true)

- 대규모 전송할 메일 제목과 본문 저장(/news-mail)

![테이블사진](https://github.com/02ggang9/02ggang9.github.io/blob/master/_posts/images/bdd/mail/quickStart/step4.png?raw=true)

~~~json
{
  "title": "Spring Batch Mailing Service Test 1",
  "text": "### BDD Mailing Service 1차 테스트\n\n안녕하세요. 부산 개발 동아리 BDD입니다.\n\nBDD 뉴스레터를 이용해주시는 선배님들께 진심으로 감사드립니다.\n\n아래는 2024년 1월 BDD의 활동 내역들입니다.\n\n### 메인페이지 시안 작성\n\n![메인페이지 시안](https://file.notion.so/f/f/aaedcf79-6b31-4898-9a1f-5e2ad8ae925e/572fb2d5-3374-4e26-bd5f-65c17b11986f/%ED%94%84%EB%A6%AC%EC%A0%A0%ED%85%8C%EC%9D%B4%EC%85%9814.png?id=1f34b78c-5b64-4797-bb6c-6bd69eeeb0c1&table=block&spaceId=aaedcf79-6b31-4898-9a1f-5e2ad8ae925e&expirationTimestamp=1704960000000&signature=01l0U7nCxzkknY-KQ0rQabOsdH3HVhDu8e3lA1cBMGE&downloadName=%ED%94%84%EB%A6%AC%EC%A0%A0%ED%85%8C%EC%9D%B4%EC%85%9814.png)\n\n### 팀 페이지 디자이닝\n\n![팀 페이지 디자이닝](https://file.notion.so/f/f/aaedcf79-6b31-4898-9a1f-5e2ad8ae925e/e7925170-bd44-4099-8360-8a767d29c407/%ED%94%84%EB%A6%AC%EC%A0%A0%ED%85%8C%EC%9D%B4%EC%85%9813.png?id=7517ebbd-d7cb-4992-af1f-7f6e623257e3&table=block&spaceId=aaedcf79-6b31-4898-9a1f-5e2ad8ae925e&expirationTimestamp=1704960000000&signature=f0LAF0CeWRpJ7CEwgDg562kWvFzfIU0EiZjaVDVTkSY&downloadName=%ED%94%84%EB%A6%AC%EC%A0%A0%ED%85%8C%EC%9D%B4%EC%85%9813.png)\n\n앞으로도 저희 BDD를 많이 사랑해주세요. \n\n감사합니다."
}
~~~

~~~md
### BDD Mailing Service 1차 테스트

안녕하세요. 부산 개발 동아리 BDD입니다.

BDD 뉴스레터를 이용해주시는 선배님들께 진심으로 감사드립니다.

아래는 2024년 1월 BDD의 활동 내역들입니다.

### 메인페이지 시안 작성

![메인페이지 시안](https://file.notion.so/f/f/aaedcf79-6b31-4898-9a1f-5e2ad8ae925e/572fb2d5-3374-4e26-bd5f-65c17b11986f/%ED%94%84%EB%A6%AC%EC%A0%A0%ED%85%8C%EC%9D%B4%EC%85%9814.png?id=1f34b78c-5b64-4797-bb6c-6bd69eeeb0c1&table=block&spaceId=aaedcf79-6b31-4898-9a1f-5e2ad8ae925e&expirationTimestamp=1704960000000&signature=01l0U7nCxzkknY-KQ0rQabOsdH3HVhDu8e3lA1cBMGE&downloadName=%ED%94%84%EB%A6%AC%EC%A0%A0%ED%85%8C%EC%9D%B4%EC%85%9814.png)

### 팀 페이지 디자이닝

![팀 페이지 디자이닝](https://file.notion.so/f/f/aaedcf79-6b31-4898-9a1f-5e2ad8ae925e/e7925170-bd44-4099-8360-8a767d29c407/%ED%94%84%EB%A6%AC%EC%A0%A0%ED%85%8C%EC%9D%B4%EC%85%9813.png?id=7517ebbd-d7cb-4992-af1f-7f6e623257e3&table=block&spaceId=aaedcf79-6b31-4898-9a1f-5e2ad8ae925e&expirationTimestamp=1704960000000&signature=f0LAF0CeWRpJ7CEwgDg562kWvFzfIU0EiZjaVDVTkSY&downloadName=%ED%94%84%EB%A6%AC%EC%A0%A0%ED%85%8C%EC%9D%B4%EC%85%9813.png)

앞으로도 저희 BDD를 많이 사랑해주세요. 

감사합니다.
~~~

- 저장된 메일들을 조회(/news-mails) 후 title 클릭시 발송될 Mail Preview 제공

![테이블사진](https://github.com/02ggang9/02ggang9.github.io/blob/master/_posts/images/bdd/mail/quickStart/step5.png?raw=true)

저장된 메일들은 수정과 삭제 메일 발송기능을 제공합니다.

- 최종 발송된 메일

![테이블사진](https://github.com/02ggang9/02ggang9.github.io/blob/master/_posts/images/bdd/mail/quickStart/step6.png?raw=true)

## Customizing

### 메일 상단의 배너 수정 방법

1. springbatch > bdd > email > entity > MdFormatConvert 이동
2. "<img" 키워드 검색 (Command + F) 9번째 <img 태그의 src를 수정

### 하단의 이미지 클릭시 이동되는 경로 수정

1. springbatch > bdd > email > entity > MdFormatConvert 이동
2. "<img" 키워드 검색 (Command + F) 10, 11, 12 번째 <img 태그의 src를 수정

## Performance

### v1.0.1

ItemReader Performance

| ItemReader 종류        | DB 데이터 개수 | 걸린시간 |
|----------------------|-----------|------|
| JpaPagingItemReader  | 100,000   | 60s  |
| JdbcCursorItemReader | 100,000   | 5s   |

## License

Spring-Batch-Bulk-Mailing-Service는 MIT 라이센스에 따라 사용할 수 있습니다. 자세한 내용은 LICENSE 파일을 참조해주세요.

## Update

### 2023.12.30

MVP 모델 업로드 및 README 작성

### 2024.01.10 (v1.0.0)

1. 마크다운 Heading 3, img 마크다운 태그를 지원하도록 수정

2. 여러가지의 이미지를 직접 삽입할 수 있도록 수정

### 2024.01.11 (v1.0.1)

1. mailItemReader를 JpaPagingItemReader에서 JdbcCursorItemReader로 수정 (데이터 10만개 기준으로 성능 12배 상승)

