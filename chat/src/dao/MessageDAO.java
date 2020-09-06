package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Stack;
import java.util.TreeSet;
import java.util.function.Consumer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import dto.MessageDTO;

public class MessageDAO {
	private static Connection connection;
    static {
       init();
    }
    public static void init() {
    	if(!isValid()) {
    		connection = getConnection();
    		createTable();
    	}
    }
    private static Connection getConnection() {
    	try {
			Context c =new InitialContext();
			DataSource source=(DataSource)c.lookup("java:comp/env/jdbc/chat");
			return source.getConnection();
		} catch (NamingException e) {
            return null;
		} catch (SQLException e) {
            return null;
		}
    }
    public static boolean isValid() {
    	try {
			return connection!=null && !connection.isClosed();
		} catch (SQLException e) {
        throw new IllegalStateException();
		}
    }
    public static boolean createTable() {
       return executeUpdate(i->{
    	   if(i<0) {
    		   //Here is the processing when it fails
    	   }
       },"CREATE TABLE IF NOT EXISTS message (id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(15) NOT NULL, text VARCHAR(250),IP VARCHAR(15),datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL);"
    		   ) < 0 ? false:true;
    }
	public static TreeSet<MessageDTO> getLatest(int limit){
		ResultSet resultset = null;
		init();
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
    	init();
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
