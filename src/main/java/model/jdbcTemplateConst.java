package model;

import org.springframework.jdbc.core.JdbcTemplate;


public class jdbcTemplateConst {

	/*
	JDBCTemplate을 웹어플리케이션 어디에서나 사용할수 있도록
	하기위해 static(정적)변수로 생성한다.
	*/
	public static JdbcTemplate template;
}
