package servlet;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beans.UserBean;
import dao.MessageDAO;
import dto.MessageDTO;


@WebServlet("/message")
public class Message extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		MessageDAO.init();
		MessageDTO dto =new MessageDTO();
		if(request.getSession()!=null&&request.getSession().getAttribute("LoginUser")!=null) {
        String text =request.getParameter("text");
        if(text!=null&&text!="") {
        dto.setName(((UserBean)request.getSession().getAttribute("LoginUser")).getName());
        dto.setText(text);
        dto.setDatetime(LocalDateTime.now());
        dto.setIP(request.getRemoteAddr());
        MessageDAO.insertMessageDTO(dto);
        }
		}
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(MessageDTO.class, dto);
		Gson gson = builder.create();
		String json = gson.toJson(MessageDAO.getLatest(30));
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(json);
		response.getWriter().flush();
	}

}
