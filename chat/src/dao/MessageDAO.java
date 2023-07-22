package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Stack;
import java.util.TreeSet;
import java.util.function.Consumer;

import javax.servlet.http.HttpServlet;

import dto.MessageDTO;
/**
*データベースとの直接のやりとりを隠蔽するクラス
*/
public class MessageDAO {
	private static Connection connection;
	private  HttpServlet servlet;
/**
*データベースとテーブルを利用する準備をするコンストラクタ
*@param s エラー時のログをとるために利用するサーブレットインスタンス
*/
    public   MessageDAO(HttpServlet s) {
    	if(!isValid()) {
    		servlet=s;
    		connection = getConnection();
    		createTable();
    	}
    }
/**
*環境変数経由でデータベースに接続するための情報を取得し、コネクションを返す。
*コメントアウト部分はHerokuを使わず、生のJavaEEで管理する時のもの。
*@return データベースを表すConnectionオブジェクト
*/
    private  Connection getConnection() {
    	try {
    		/* Plane Tomcat
			Context c =new InitialContext();
			DataSource source=(DataSource)c.lookup("java:comp/env/jdbc/chat");
			return source.getConnection();
            */

    		//heroku
    		String dburl = System.getenv("DATABASE_URL");
    		String user = dburl.split("//")[1].split(":")[0];
    		String pass = dburl.split("//")[1].split(":")[1].split("@")[0];
    		String host = dburl.split("@")[1].split("/")[0];
    		String db = dburl.split("@")[1].split("/")[1].split("\\?")[0];
    		String url = "jdbc:mysql://"+host+":"+3306+"/"+db+"?characterEncoding=UTF-8&serverTimezone=JST&reconnect=true";
    		Class.forName("com.mysql.cj.jdbc.Driver");
			connection =  DriverManager.getConnection(url, user, pass);

		} catch (SQLException e) {
             servlet.log(e.getMessage());
             servlet.log(e.getCause().getMessage());
            return null;
		} catch (ClassNotFoundException e) {
			servlet.log(e.getMessage());
			servlet.log(e.getCause().getMessage());
		}
		return connection;
    }
/**
*
*/
    public static boolean isValid() {
    	try {
			return connection!=null && !connection.isClosed();
		} catch (SQLException e) {
        throw new IllegalStateException();
		}
    }
    public  boolean createTable() {
       return executeUpdate(i->{
    	   if(i<0) {
    		   //Here is the processing when it fails
    	   }
       },"CREATE TABLE IF NOT EXISTS message (id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(15) NOT NULL, text VARCHAR(250),IP VARCHAR(15),datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL);"
    		   ) < 0 ? false:true;
    }
	public  TreeSet<MessageDTO> getLatest(int limit){
		ResultSet resultset = null;
		if(isValid()) {
            try(
            PreparedStatement statement = connection.prepareStatement("SELECT * from message order by datetime DESC limit ?");
            ){
            	statement.setInt(1, limit);
            resultset = statement.executeQuery();
            return toMessageDTOSet(resultset);
            } catch(SQLException e){return null;}
            finally {
            	try {
            		if(resultset!=null&&!resultset.isClosed())
            	resultset.close();
            	}catch(SQLException e) {e.printStackTrace();}
            }
       	} else {
       		return null;
       	}
    }

/*
    private static <R> R executeQuery(Function<ResultSet,R> func,String SQL) {
    	if(isValid()) {
            try(
            Statement statement = connection.createStatement();
           		ResultSet rs = statement.executeQuery(SQL);
            ){

           	 return func.apply(rs);
            } catch(SQLException e){return null;}
       	} else {
       		return null;
       	}
    }
    */
    private static int executeUpdate(Consumer<Integer> c,String SQL) {
    	int i =-1;
    	if(isValid()) {
            try(
            Statement statement = connection.createStatement();
            ){
            	i=statement.executeUpdate(SQL);
                 c.accept(i);
                 return i;
            } catch(SQLException e){
            	c.accept(-1);
            	return -1;
            }
       	}
		return i;
    }
    public static boolean insertMessageDTO(MessageDTO m) {
    	if(isValid()) {
            try(
            PreparedStatement statement = connection.prepareStatement("INSERT INTO message (text,name,datetime,IP) VALUES (?,?,?,?);");
            ){
              statement.setString(1, m.getText());
              statement.setString(2, m.getName());
              statement.setTimestamp(3, Timestamp.valueOf(m.getDatetime()));
              statement.setString(4, m.getIP());
              return statement.executeUpdate() >0;
            } catch(SQLException e){
            	e.printStackTrace();
            }
       	}
    	return false;
    }
    public void deleteMessage(String user) {
    	if(!isValid()) {
    		connection = getConnection();
    	}
         try(PreparedStatement s = connection.prepareStatement("DELETE from message WHERE name = ?")){
        	 s.setString(1, user);
        	 s.executeUpdate();
         }catch(SQLException e) {
        	 e.printStackTrace();
         }

    }
    public void deleteAllMessage() {
    	if(!isValid()) {
    		connection = getConnection();
    	}
    	try(Statement s = connection.createStatement();){
    		s.executeUpdate("DELETE from message;");
    	} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    }
	private static TreeSet<MessageDTO> toMessageDTOSet(ResultSet rs) {
		TreeSet<MessageDTO> result = new TreeSet<MessageDTO>();
		Stack<MessageDTO> stack = new Stack<>();
		String text;
		String name;
		LocalDateTime datetime;
		String IP;
		try {
			while(rs.next()) {
				text =rs.getString("text");
				name =rs.getString("name");
				datetime =rs.getTimestamp("datetime").toLocalDateTime();
				IP =rs.getString("IP");
				stack.push(new MessageDTO(text,name,datetime,IP));
			}
            while(!stack.empty()) {
            	result.add(stack.pop());
            }
			return result;
		} catch (SQLException e) {return null;}
	}
}
