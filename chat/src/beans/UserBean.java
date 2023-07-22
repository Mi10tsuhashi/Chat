package beans;

import java.io.Serializable;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
/**
*現在ログインしているユーザーを表すJavaBean。
*/
public class UserBean implements Serializable , HttpSessionBindingListener , Comparable<UserBean>{
      private String name;
      private static final long serialVersionUID = 1L;
      public UserBean(String name) {
    	  this.setName(name);
      }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
 	*ログイン中のユーザーがログアウトし、セッションからバインド解除された時に、ログイン中の
  	*ユーザー群を集合からも削除している。
   	*@param event セッションを識別するイベント
	*/
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
       ServletContext servlet =event.getSession().getServletContext();
       UserBean bean = (UserBean)event.getValue();
       @SuppressWarnings("unchecked")
       //UserSetの値はSet<String>でセットしているため型安全
	   TreeSet<UserBean> userset=(TreeSet<UserBean>)servlet.getAttribute("UserSet");
       if(userset!=null&&bean!=null) {
       userset.remove(bean);
       }
	}
	/**
 	*ユーザー名を元にハッシュコードを計算するようにオーバーライド
	*/
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	/**
 	*ユーザー名を元に同値判定を行うようにオーバーライド
	*/
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof UserBean)) {
			return false;
		}
		return this.name.equals(((UserBean)obj).getName());
	}
	/**
 	*ユーザー名を元に比較するようにオーバーライド
	*/
	@Override
	public int compareTo(UserBean o) {

		return this.name.compareTo(o.getName());
	}

}
