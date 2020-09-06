package dto;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class MessageDTO implements JsonSerializer<MessageDTO>,JsonDeserializer<MessageDTO>, Serializable ,Comparable<MessageDTO> {
	public MessageDTO() {}
	public MessageDTO(String text, String name, LocalDateTime datetime, String IP) {
		this.text=text;
		this.name=name;
		this.datetime=datetime;
		this.IP=IP;
	}
	private static final long serialVersionUID = 1L;
    private String text;
    private String name;
    private LocalDateTime datetime;
    private String IP;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDateTime getDatetime() {
		return datetime;
	}
	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	@Override
	public JsonElement serialize(MessageDTO dto, Type arg1, JsonSerializationContext arg2) {
		JsonObject result = new JsonObject();
		result.add("text", new JsonPrimitive(dto.getText()));
		result.add("name", new JsonPrimitive(dto.getName()));
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		result.add("datetime", new JsonPrimitive(formatter.format(dto.getDatetime())));
		result.add("IP", new JsonPrimitive(dto.getIP()));
		return result;
	}
	@Override
	public MessageDTO deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2)
			throws JsonParseException {
		JsonObject obj=element.getAsJsonObject();
		String text = obj.get("text").getAsString();
		String name=obj.get("name").getAsString();
		String datetime = obj.get("datetime").getAsString();
		String IP=obj.get("IP").getAsString();
		DateTimeFormatter formatter =DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		LocalDateTime datetimeObj =formatter.parse(datetime, LocalDateTime::from);
		return new MessageDTO(text,name,datetimeObj,IP);
	}
	@Override
	public int compareTo(MessageDTO o) {
		return this.getDatetime().compareTo(o.getDatetime());
	}

}
