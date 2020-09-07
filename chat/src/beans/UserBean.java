package beans;

import java.io.Serializable;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

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
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
       ServletContext servlet =event.getSession().getServletContext();
       UserBean bean = (UserBean)event.getValue();
       @SuppressWarnings("unchecked")
       //UserSetの値はTreeSet<UserBean>でセットしているため型安全
	   TreeSet<UserBean> userset=(TreeSet<UserBean>)servlet.getAttribute("UserSet");
       if(userset!=null&&bean!=null) {
       userset.remove(bean);
       }
	}
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof UserBean)) {
			return false;
		}
		return this.name.equals(((UserBean)obj).getName());
	}
	@Override
	public int compareTo(UserBean o) {

		return this.name.compareTo(o.getName());
	}

}
