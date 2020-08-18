package springboard.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import model.jdbcTemplateConst;
import springboard.command.BbsCommandImpl;
import springboard.command.DeleteActionCommand;
import springboard.command.EditActionCommand;
import springboard.command.EditCommand;
import springboard.command.ListCommand;
import springboard.command.ReplyActionCommand;
import springboard.command.ReplyCommand;
import springboard.command.ViewCommand;
import springboard.command.WriteActionCommand;
import springboard.model.JDBCTemplateDAO;
import springboard.model.SpringBbsDTO;



/*
@Autowired
	: 스프링 설정파일에서 생성된 빈을 자동으로 주입받을때
	사용하는 어노테이션
	-생성자, 필드(멤버변수), 메소드(setter)에 적용가능
	setter()의 형식이 아니어도 적용가능
	타입을 이용해 자동으로 프로퍼티 값을 설정
	-따라서 빈을 주입받을 객체가 존재하지 않거나, 같은 타입이 
	2개 이상 존재하면 예외가 발생됨
	-지역변수에서 사용하면 에러발생, 멤버변수에만 적용가능하다
	

폴더나 파일에 S마크 된것은 Spring 컨테이너가 관리하고 있다는 의미임

spring에서는 가능하면 new연산자대신 DI개념을 활용해서 
의존 주입을 이용하는것이 좋다.
주로 사용하는 DAO,DTO같은 것.
*/
@Controller
public class BbsController {

	//1차버전에서 사용
	//@Autowired : 여기에도 사용가능, 아래 코드와 동일한 역할을 하게됨
	//private JdbcTemplate template;
	
	
	//2차버전에서 사용
	//생성된 dao는 서블릿 컨텍스트에서 생성한것만 가져다가 사용할수있도록 하였다.
	JDBCTemplateDAO dao;
	@Autowired
	public void setDao(JDBCTemplateDAO dao) {
		this.dao = dao;
		System.out.println("JDBCTemplateDAO 자동주입(컨트롤러)");
	}
	/*
	Spring에서는 new를 통해 객체를 생성하지 않고 스프링컨테이너에 의해
	미리 생성된 빈(객체) 를 주입(DI)받아서 사용하게 된다.
	따라서 기본클래스가 아닌 개발자가 직접 정의하여 자주사용되는 객체들은
	아래와 같이 자동주입 받아서 사용하는 것이 스프링 개발방법론에 적절한 방법이다.
	*/
	@Autowired
	ListCommand listCommand;	//2차버전
	@Autowired
	ViewCommand viewCommand;	//2차버전
	@Autowired
	WriteActionCommand writeActionCommand;
	@Autowired
	EditCommand editCommand;
	@Autowired
	DeleteActionCommand deleteActionCommand;
	@Autowired
	EditActionCommand editActionCommand;
	@Autowired
	ReplyCommand replyCommand;
	@Autowired
	ReplyActionCommand replyActionCommand;

	/*
	BbsCommandImpl 타입의 멤버변수 선언.
	멤버변수이므로 클래스내에서 전역적으로 사용
	해당클래스의 모든 command객체는 위 인터페이스를 구현하여 정의하게 된다.
	*/
	BbsCommandImpl command = null;
	
	
	
	/*
	//1차버전에서 사용
	//스프링 애플리케이션이 구동될때 미리 생성된 JDBCTemplate타입의
	//빈을 자동으로 주입받게 된다.
	@Autowired
	public void setTemplate(JdbcTemplate template) {
		this.template = template;
		System.out.println("@Autowired->JDBCTemplate 연결성공");
		jdbcTemplateConst.template = this.template;
	}
	*/
	
	
	//게시판 리스트
	@RequestMapping("/board/list.do")
	public String list(Model model, HttpServletRequest req) {
		
		/*
		사용자로부터 받은 모든요청은 HttpServletRequest객체에 저장되고
		이를 커맨드객체로 전달하기위해 model에 저장후 매개변수로 전달한다.
		 */
		model.addAttribute("req", req);
		
		/*
		컨트롤러는 사용자의 요청을 분선한 후 해당 요청에 맞는 서비스 객체만
		호추라고, 실제 DAO의 호출이나 비즈니스 로직은 아래 Command객체가 처리하게 된다.
		 */
		//command = new ListCommand();//1차버전
		command = listCommand;	//2차버전
		command.execute(model);
		
		return "07Board/list";
	}
	
	
	
	@RequestMapping("/board/write.do")
	public String write(Model model) {
		return "07Board/write";
	}
	
	
	
	
	//글쓰기처리:post로 전송되므로 두가지 속성을 모두 사용하여 매핑
	@RequestMapping(value = "/board/writeAction.do", method = RequestMethod.POST)
	public String writeAction(Model model,
			HttpServletRequest req, SpringBbsDTO springBbsDTO) {
		
		//request객체는 기본으로 받고 나머지 필요한것을 받는다.
		model.addAttribute("req", req);
		
		//view에서 전송한 폼값을 한번에 저장한 커맨드객체로DTO를 저장
		model.addAttribute("springBbsDTO", springBbsDTO);
		
		command = writeActionCommand;
		command.execute(model);
		
		//href와 같은 페이지 이동이 된다.
		//글쓰기 처리 완료후 list.do로 로케이션(이동) 된다.
		return "redirect:list.do?nowPage=1";
	}
	
	
	
	@RequestMapping("/board/view.do")
	public String view(Model model, HttpServletRequest req) {
		
		model.addAttribute("req", req);
		//command = new ViewCommand();	//1차버전
		command = viewCommand;
		command.execute(model);
		
		return "07Board/view";
	}



	@RequestMapping("/board/password.do")
	public String password(Model model, HttpServletRequest req) {
		
		model.addAttribute("idx", req.getParameter("idx"));
		return "07Board/password";
	}


	@RequestMapping("/board/passwordAction.do")
	public String passwordAction(Model model, HttpServletRequest req) {
		
		String modePage = null;
		
		String mode = 	req.getParameter("mode");
		String idx = 	req.getParameter("idx");
		String nowPage =req.getParameter("nowPage");
		String pass = 	req.getParameter("pass");
		
		//JDBCTemplateDAO dao = new JDBCTemplateDAO();
		
		int rowExist = dao.password(idx,pass);
		if(rowExist<=0) {
			model.addAttribute("isCorrMsg", "패스워드가 일치하지 않습니다");
			model.addAttribute("idx", idx);
			modePage = "07Board/password";
		}else {
			System.out.println("검증완료");
			if(mode.equals("edit")) {
				//수정이면 수정폼으로 이동
				model.addAttribute("req", req);
				//command = new EditCommand();
				command = editCommand;
				command.execute(model);
				modePage = "07Board/edit";
			}else if(mode.equals("delete")) {
				//패스워드 검증에 문제가 없다면 즉시 삭제처리한다
				model.addAttribute("req",req);
				
				
				//command = new DeleteActionCommand();
				command = deleteActionCommand;
				command.execute(model);
				
				
				/*
				컨트롤러에서 뷰의 경로를 반환할때 아래와 같이 redirect하게 되면
				list.do?nowPage=10 과 같이 URL을 자동으로 조립해서 로케이션시킨다.
				 */
				model.addAttribute("nowPage", req.getParameter("nowPage"));
				modePage = "redirect:list.do";
				
			}
		}
		
		return modePage;
	}
	
	//수정처리
	@RequestMapping("/board/editAction.do")
	public String editAction(HttpServletRequest req,
			Model model, SpringBbsDTO springBbsDTO) {
		
		model.addAttribute("req", req);
		model.addAttribute("springBbsDTO",springBbsDTO);
		
		//command = new EditActionCommand();
		command = editActionCommand;
		command.execute(model);
		
		
		/*
		게시물 수정 후 상세보기 페이지로 돌아가기 위해서는 idx값이 반드시 필요하다.
		이동시 쿼리스트링 형태로 redirect 하지 않고
		model 객체에 필요한 파라미터를 저장하면 자동으로 URL을 조립하여 View를 호출해준다.
		*/
		model.addAttribute("idx", req.getParameter("idx"));
		model.addAttribute("nowPage", req.getParameter("nowPage"));
		
		
		
		return "redirect:view.do";
	}

	
	
	//답변글쓰기
	@RequestMapping("/board/reply.do")
	public String reply(HttpServletRequest req, Model model) {
		System.out.println("reply() 호출");
		
		model.addAttribute("req",req);
		//command = new ReplyCommand();
		command = replyCommand;
		command.execute(model);
		
		model.addAttribute("idx", req.getParameter("idx"));
		
		
		return "07Board/reply";
	}


	//답변글쓰기처리
	@RequestMapping("/board/replyAction.do")
	public String replyAction(HttpServletRequest req,
			Model model, SpringBbsDTO springBbsDTO) {
		
		//작성된 내용은 커맨드 객체를 통해 한번에 폼값 받음
		model.addAttribute("springBbsDTO", springBbsDTO);
		model.addAttribute("req", req);
		
		//command = new ReplyActionCommand();
		command = replyActionCommand;
		command.execute(model);
		
		//답글쓰기 완료후 리스트 페이지로 이동함
		model.addAttribute("nowPage", req.getParameter("nowPage"));
		return "redirect:list.do";
	}
}


















