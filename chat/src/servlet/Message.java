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

/**
*ログイン後のチャットデータの登録と、送信を担うサーブレット。
*/
@WebServlet("/message")
public class Message extends HttpServlet {
	private static final long serialVersionUID = 1L;

/**
*データがある場合は新規にメッセージを登録した後、コマンド処理を行い、最新のメッセージ群をJSON形式で返す。
*データがない場合は最新のメッセージ群をJSON形式で返すのみで、Ajaxで定期的に呼ばれる。
*@param request HTTPリクエスト。ここでは発言したメッセージ内容になる。
*@param response HTTPレスポンス。JSON形式でチャットデータを返す。
*/
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		MessageDAO dao = new MessageDAO(this);
		MessageDTO dto =new MessageDTO();
		if(request.getSession()!=null&&request.getSession().getAttribute("LoginUser")!=null) {
        String text =request.getParameter("text");
        if(text!=null&&text!="") {
        dto.setName(((UserBean)request.getSession().getAttribute("LoginUser")).getName());
        dto.setText(text);
        dto.setDatetime(LocalDateTime.now());
        dto.setIP(request.getRemoteAddr());
        MessageDAO.insertMessageDTO(dto);
/*
*コマンドの処理。
*チャット欄で　「/del」　と入力された場合は入力したユーザーが発言したメッセージを全て削除する。
*「/delall」　と入力された場合はメッセージを全て削除する。
*/

        if(text.startsWith("/del")&&!text.startsWith("/delall")) {
        dao.deleteMessage(((UserBean)request.getSession().getAttribute("LoginUser")).getName());
        }else if (text.startsWith("/delall")){
        	dao.deleteAllMessage();
        }
        }
		}
		//データベースに登録されているメッセージから30件取得し、JSONで返す。
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(MessageDTO.class, dto);
		Gson gson = builder.create();
		String json = gson.toJson(dao.getLatest(30));
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(json);
		response.getWriter().flush();
	}

}
