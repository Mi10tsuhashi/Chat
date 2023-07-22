package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
*ログアウト時の処理を担うサーブレット
*/
@WebServlet("/logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;

/**
*明示的にセッションを終了し、IndexServletに戻る。
*@param request HTTPリクエスト
*@param response HTTPレスポンス
*/
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     request.getSession().invalidate();
     request.getRequestDispatcher("/").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
