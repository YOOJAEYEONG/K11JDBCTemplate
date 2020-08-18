<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Home</title>
</head>
<body>

	<!--오라클 드라이버를 사용한다 => spring표현 의존한다.  -->
	
	
	
	<!--[Spring-JDBC를 구현하기위한 절차]-->
	
	<!--  
	1. pom.xml > Spring-JDBC를 사용하기 위한 의존설정
	오라클 JDBC 원격 레파지토리(저장소)
	<repositories>
		<repository>
			<id>oracle</id>
			<name>ORACLE JDBC Repository</name>
			<url>https://code.lds.org/nexus/content/groups/main-repo</url>
		</repository>
	</repositories>	
	ojdbc6(오라클드라이버)의존설정
		<dependency>
			<groupId>com.oracle</groupId>
			<artifactId>ojdbc6</artifactId>
			<version>11.2.0.3</version>
		</dependency>
		spring jdbc(JDBCTemplate)를 사용하기위한 의존설정
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-jdbc</artifactId>
			<version>4.1.4.RELEASE</version>
		</dependency>
	-->
	
	<!--  
	2. servlet-context.xml 에서 빈을 생성한다.
		2-1. dataSource : DB연결정보를 가진 빈
		2-2. template : JdbcTemplate 타입의 빈. 이를 통해 Spring-JDBC를 구현
		
		<beans:bean name="dataSource8" 
			class="org.springframework.jdbc.datasource.DriverManagerDataSource">
			<beans:property name="driverClassName" value="oracle.jdbc.OracleDriver" />
			<beans:property name="url" value="jdbc:oracle:thin:@localhost:1521:orcl" />
			<beans:property name="username" value="kosmo" />
			<beans:property name="password" value="1234" />
		</beans:bean>
		
		<beans:bean name="template" 
			class="org.springframework.jdbc.core.JdbcTemplate">
			<beans:property name="dataSource" ref="dataSource8" />
		</beans:bean>
	-->
	
	<!--  
	3. 요청명을 결정하고 , 컨트롤러를 생성한다.
		servlet-context.xml에서 기본패키지를 아래와 같이 추가함(옵션사항)
		<context:component-scan base-package="springboard" />
	-->
	
	<!--  
	4. Service객체, DAO객체를 생성한다.
		Service는 컨트롤러와 모델 사이에서 중재역할을 하는 객체로써
		컨트롤러의 모든요청을 서비스로 넘겨주기위해 '커맨드객체'를 사용하게 된다.
		Request객체를 Model객체에 저장후 서비스객체로 model을 매개변수로 전달한다.
		서비스객체에서는 model.asMap() 메소드를 통해 Map컬렉션으로 변환한 후 
		요청들을 처리하고 모델을 호출한다.
	-->
	
	
	<!--  
	5.게시판 구현에 필요한 객체들을 new를 통해 생성하지 않고
		주입(DI)을 통해서 구현한다. 이때 @Autowired를 사용하게 된다.
	-->
	<h2>스프링 MVC 시작하기</h2>
	<h3>Spring 답변형 비회원제 게시판 제작</h3>
	<li><a href="board/list.do" target="_blank">SPRING
			JDBC(JDBCTemplate)을 이용한 게시판</a></li>

</body>
</html>
