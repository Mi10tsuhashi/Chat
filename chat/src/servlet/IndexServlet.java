package servlet;

import java.io.IOException;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.UserBean;










@WebServlet("/index.jsp")
@WebListener
public class IndexServlet extends HttpServlet{





	private static final long serialVersionUID = 1L;





	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/top.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String A = "\\u3000";
    	String B = "\\u00A0";
    	String C = "\\u0020";
        if(name!=null&&!name.equals("")&&!name.matches(A+"*"+C+"*"+B+"*"+A+"*"+C+"*"+B+"*"+A+"*")) {
        	UserBean user = new UserBean(name);
        	ServletContext context =request.getServletContext();
        	@SuppressWarnings("unchecked")
        	//UserSetの値はSet<UserBean>でセットしているため型安全
			TreeSet<UserBean> userset =(context.getAttribute("UserSet")!=null)?(TreeSet<UserBean>)context.getAttribute("UserSet"):new TreeSet<UserBean>();
        	if(!userset.contains(user)) {
        		userset.add(user);
        		context.setAttribute("UserSet", userset);
        		request.getSession().setAttribute("LoginUser", user);
        		request.getSession().removeAttribute("ErrorMessage");
        		request.getRequestDispatcher("/top.jsp").forward(request, response);
        	} else {
        		request.getSession().setAttribute("ErrorMessage", "すでに入室中のユーザーと被っています。");
        		request.getRequestDispatcher("/top.jsp").forward(request, response);
        	}
        } else {
        	request.getSession().setAttribute("ErrorMessage", "不適切な名前です。");
    		request.getRequestDispatcher("/top.jsp").forward(request, response);
        }

	}

}
