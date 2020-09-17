package springboard.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import model.jdbcTemplateConst;



/*
JDBCTemplate 관련 주요메소드
-List query(String sql, RowMapper rowMapper)
	: 여러개의 레코드를 반환하는 select계열의 쿼리문에 사용한다.
-List query(String sql, Object[] args, RowMapper rowMapper)
	: 인파라미터를 가진 여러개의 레코드를 반환하는 
	select계열의 쿼리문인 경우 사용한다

-int queryForInt(String sql) 혹은 queryForInt(String sql, Object[] args)
	: 쿼리문의 실행결과가 숫자를 반환하는 select 계열의 쿼리문에 사용한다.
-Object queryForObject(String sql, RowMapper rowMapper)
	혹은 Object queryForObject(String sql, Object[] args, RowMapper rowMapper)
	: 하나의 레코드를 반환하는 select계열의 쿼리문 실행시 사용된다.
	쿼리 결과 값이 없거나 2개 이상인경우 예외가 발생한다.

-int update(String sql)
	: 인파라미터가 없는 update/delete/insert 쿼리문을 처리할때 사용함
-int update(String sql, Object[] args)
	: 인파라미터가 있는 update/delete/insert 쿼리문을 처리할때 사용함
 */

/*
2차버전을 위해 추가한 어노테이션
@Repository 
	: 해당 클래스가 DAO(Model) 역할의 클래스임을 명시하는 어노테이션
*/
@Repository
public class JDBCTemplateDAO {

	JdbcTemplate template;
	
	/*
	setter메소드 생성 : Spring-JDBC 2차버전
	servlet-context.xml에서 생성된 dao빈을 자동으로 주입받기위해 생성한 setter
	 */
	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}
	
	public JDBCTemplateDAO() {
		
		/*
		컨트롤러에서 @Autowired를 통해 자동 주입 받았던 빈을
		정적변수인 jdbcTemplateConst.template을 통해 가져온다. 
		즉, DB연결 정보를 웹어플리케이션 어디에서든 사용할 수 있다.
		 */
		//Spring-JDBC 1차버전에서 사용함.(new로 DAO객체 생성)
		//this.template = jdbcTemplateConst.template;
		System.out.println("JDBCTemplateDAO() 생성자 호출");
	}
	public void close() {
		//JDBCTemplate에서는 사용하지 않음
	}
	
	
	//게시판 리스트(페이지처리 없음)
	public ArrayList<SpringBbsDTO> list(Map<String, Object> map){
		
		String sql = "SELECT * FROM springboard ";
		if(map.get("Word")!=null) {
			sql += " WHERE "+map.get("Column")+
					" LIKE '%"+map.get("Word")+"%' ";
		}
		//sql += " ORDER BY idx DESC"; //답변글 사용하지 않을경우
		sql += " ORDER BY bgroup DESC, bstep ASC";//답변글 적용시
		
		
		/*
		query메소드의 반환타입은 List계열의 컬렉션이므로 제네릭부분만
		필요한 DTO객체로 대체하면된다. 나머지는 RowMapper객체가 
		자동으로 해준다.
		 */
		return (ArrayList<SpringBbsDTO>)
				template.query(sql,
						new BeanPropertyRowMapper<SpringBbsDTO>(SpringBbsDTO.class));
	}

	
	//게시판 리스트(페이지처리 있음)
	public ArrayList<SpringBbsDTO> listPage(Map<String, Object> map) {

		int start = Integer.parseInt(map.get("start").toString());
		int end = Integer.parseInt(map.get("end").toString());

		String sql = "" + "SELECT * FROM ("
				+ "    SELECT Tb.*, rownum rNum FROM ("
				+ "        SELECT * FROM springboard ";
		if (map.get("Word") != null) {
			sql += " WHERE " + map.get("Column") + " " + " LIKE '%"
					+ map.get("Word") + "%' ";
		}
		sql += " ORDER BY bgroup DESC, bstep ASC" + "    ) Tb" + ")"
				+ " WHERE rNum BETWEEN " + start + " and " + end;

		return (ArrayList<SpringBbsDTO>) template.query(sql,
				new BeanPropertyRowMapper<SpringBbsDTO>(SpringBbsDTO.class));
	}
	
	
	public int getTotalCount(Map<String, Object> map) {
		
		String sql = "SELECT COUNT(*) FROM springboard ";
		 
		//오브젝트로 반환되는 타입을 Integer.class 타입으로 정의함 
		return template.queryForObject(sql, Integer.class);
	}

	
	//글쓰기처리1
	public void write(final SpringBbsDTO springBbsDTO) {
		
		template.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {

				/*
				답변형게시판에서 원본글인경우에는 
				idx(일련번호)와 bgroup(그룹번호)가 반드시 일치해야한다.
				또한 nextVal은 한문장에서 여러번 사용해도 같은 시퀀스값을 반환한다.
				 */
				String sql = "INSERT INTO springboard ("
						+ " idx, name, title, contents, hits, bgroup, bstep, bindent, pass) "
						+ " VALUES ( "
						+ " springboard_seq.NEXTVAL, ?,?,?,0, "
						+ " springboard_seq.NEXTVAL,0,0,?)";
				
				PreparedStatement psmt = con.prepareStatement(sql);
				psmt.setString(1, springBbsDTO.getName());
				psmt.setString(2, springBbsDTO.getTitle());
				psmt.setString(3, springBbsDTO.getContents());
				psmt.setString(4, springBbsDTO.getPass());
				
				return psmt;
			}
		});
	}
	
	/*
	매개변수로 전달되는 idx를 아래 익명클래스내부에서 사용하기위해서는
	반드시 final로 선언해야 사용가능하다. java의 규약이다.
	*/
	public void updateHit(final String idx) {
		
		String sql = "UPDATE springboard SET "
				+ " hits = hits+1 "
				+ " WHERE idx =? ";
		template.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {

				ps.setInt(1, Integer.parseInt(idx));
			}
		});
	}
	//상세보기처리
	public SpringBbsDTO view(String idx) {
		
		//조회수증가
		updateHit(idx);
		
		SpringBbsDTO dto = new SpringBbsDTO();
		String sql = "SELECT * FROM springboard "
				+ " WHERE idx="+idx;
		
		
		/*
		queryForObject()메소드는 반환결과가 0개이거나 2개 이상인 경우
		예외가 발생하므로 반드시 예외처리를 해주는 것이 좋다.
		 */
		try {template.queryForObject(sql, new BeanPropertyRowMapper<SpringBbsDTO>());
			dto = template.queryForObject(sql, 
					new BeanPropertyRowMapper<SpringBbsDTO>(SpringBbsDTO.class));
			
		} catch (Exception e) {
			System.out.println("View()실행 예외");
			e.printStackTrace();
		}
		
		
		return dto;
	}
	
	
	
	//패스워드 검증
	public int password(String idx, String pass) {

		int retNum = 0;
		
		String sql = "SELECT * FROM springboard "
				+ " WHERE pass="+pass+" AND idx="+idx;
		
		/*
		만약 패스워드가 틀린 경우라면 반환되는 행이 0개이므로
		예외처리를 하고있다. 
		queryForObject()는 반환되는 행이 1개일때만 정상 동작한다.
		 */
		try {
			SpringBbsDTO dto = 
					template.queryForObject(sql,
							new BeanPropertyRowMapper<SpringBbsDTO>(SpringBbsDTO.class));
			/*
			idx와 pass에 해당하는 게시물이 정상적으로 가져와졌을경우
			해당 idx값을 반환값으로 사용한다.
			 */
			retNum = dto.getIdx();
		} catch (Exception e) {
			/*
			만약 일치하지 않아 예외가 발생할경우 0을 반환한다. 일련번호는 시퀀스를 사용하므로
			항상 0보다 큰 값을 가지게 된다.
			 */
			System.out.println("password()예외");
			e.printStackTrace();
		}
		return retNum;
	}
	
	//수정처리
	public void edit(final SpringBbsDTO dto) {

		
		/*
		해당게시판에서 패스워드는 변경이 대상이 아니라 검증의 대상으로만 사용됨.
		따라서 set절이 아니라 where 절에 삽입된다.
		 */
		String sql = "UPDATE springboard "
				+ " SET name=?, title=?, contents=? "
				+ " WHERE idx=? AND pass=?";
		
		
		
		/*
		매개변수 dto 객체를 아래 익명클래스 내부에서 사용해야 하므로
		반드시 final을 붙여 줘야 한다.
		 */
		template.update(sql,new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {

				ps.setString(1, dto.getName());
				ps.setString(2, dto.getTitle());
				ps.setString(3, dto.getContents());
				ps.setInt(4, dto.getIdx());
				ps.setString(5, dto.getPass());
			}
		});
		
	}
	

	public void delete(final String idx, final String pass) {

		String sql = "DELETE FROM springboard "
				+ " WHERE idx=? AND pass=?";
		
		template.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {

				ps.setString(1, idx);
				ps.setString(2, pass);
			}
		});
	}
	
	
	
	
	/*
	답변글 입력전 레코드 일괄 업데이트(step을 뒤로 밀어주기위한 로직)
	 */
	private void replyPrevUpdate(final int bgroup, final int bstep) {
		/*
		bstep이 2,2,2 이런식으로 중복되는 경우가 생기는데
		새 답글이 추가되는 경우 기존의 답글의 순서대로 출력을 위하여 step을 +1 적용한다.
		
		bgroup	bstep	
		10		0		(새로 추가된 답글)
		10		1		(기존답글 3번째)
		10		2		(기존답글 2번째)
		10		3		(기존답글 1번째)
		 */
		String sql = "UPDATE springboard "
				+ " SET bstep = bstep +1 "
				+ " WHERE bgroup =? AND bstep > ?";
		
		template.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {

				ps.setInt(1, bgroup);
				ps.setInt(2, bstep);
			}
		});
	}
	public void reply(final SpringBbsDTO dto) {
		
		/*
		오라클은 레코드 입력된 순서를 보장하지 않는다 따라서
		replyPrevUpdate()로직 없이 답글이 여러개 추가되면 
		 답글들의 순서가 두서없이 출력된다. 
		 */
		//답변글쓰기전 레코드 업데이트
		replyPrevUpdate(dto.getBgroup(),dto.getBstep());
		
		/*
		원본글의 경우 idx와  broup은 동일한 값을 입력함.
		답변글의 경우 원본글의 group번호를 그대로 가져와서 입력함.
		즉,  idx는 시퀀스를 통해 bgroup은 원본글과 동일하게 입력
		 */
		String sql = "INSERT INTO springboard "
				+ " (idx, name, title, contents, pass, "
				+ " bgroup, bstep, bindent) "
				+ " VALUES (springboard_seq.NEXTVAL, ?,?,?,?,"
				+ " ?,?,?)";
		
		template.update(sql, new PreparedStatementSetter() {
			
			
			/*
			답변글인 경우 원본글의 step+1, indent+1처리하여 입력한다.
			 */
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {

				ps.setString(1, dto.getName());
				ps.setString(2, dto.getTitle());
				ps.setString(3, dto.getContents());
				ps.setString(4, dto.getPass());
				ps.setInt(5, dto.getBgroup());
				ps.setInt(6, dto.getBstep()+1);
				ps.setInt(7, dto.getBindent()+1);
			}
		});
		
	}
	
}





















