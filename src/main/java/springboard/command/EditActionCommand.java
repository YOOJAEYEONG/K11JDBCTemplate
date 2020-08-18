package springboard.command;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springboard.model.JDBCTemplateDAO;
import springboard.model.SpringBbsDTO;

/*
@Service
	:해당 어노테이션은 컴파일러에게 해당 클래스가 서비스 역할을 하는 
	클래스임을 표기하는 역할을 함
	@Override 처럼 반드시 있어야하는 것은 아니다.
 */
//붙여도 되고 안붙여도 된다.
@Service
public class EditActionCommand implements BbsCommandImpl {

	// 2차버전///////////////////////////
	JDBCTemplateDAO dao;
	@Autowired
	public void setDao(JDBCTemplateDAO dao) {
		this.dao = dao;
		System.out.println("viewCommand 자동주입(EditAction)");
	}
	////////////////////////////////////

	@Override
	public void execute(Model model) {

		Map<String, Object> map = model.asMap();
		HttpServletRequest req = (HttpServletRequest) map.get("req");

		// 폼값을 한번에 받아 저장한 커맨드객체를 model객체에서 가져옴
		SpringBbsDTO springBbsDTO = (SpringBbsDTO) map.get("springBbsDTO");

		// JDBCTemplateDAO dao = new JDBCTemplateDAO();

		dao.edit(springBbsDTO);

	}

}
