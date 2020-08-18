package springboard.command;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springboard.model.JDBCTemplateDAO;
import springboard.model.SpringBbsDTO;

//BbsCommandImpl을 기반으로 하는 서비스이다 임을 의미함
//@Service("BbsCommandImpl")
@Service
public class ReplyCommand implements BbsCommandImpl {

	// 2차버전///////////////////////////
	JDBCTemplateDAO dao;
	@Autowired
	public void setDao(JDBCTemplateDAO dao) {
		this.dao = dao;
		System.out.println("JDBCTemplateDAO 자동주입(ReplyCommand)");
	}
	////////////////////////////////////

	@Override
	public void execute(Model model) {

		Map<String, Object> map = model.asMap();
		HttpServletRequest req = (HttpServletRequest) map.get("req");

		String idx = req.getParameter("idx");
		//JDBCTemplateDAO dao = new JDBCTemplateDAO();
		SpringBbsDTO dto = dao.view(idx);

		/*
		 * 기존 게시물을 가져와서 제목과 내용 부분에 아래와 같이 문자열처리를 한다. 내용의 경우 textarea에 표현해야 하므로
		 * <br>태그 대신 \n\r이 들어가야한다.
		 */
		dto.setTitle("[RE]" + dto.getTitle());
		dto.setContents("\n\r\n\r---[원본글]---\n\r" + dto.getContents());

		model.addAttribute("replyRow", dto);

	}

}
