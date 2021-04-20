<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="chat.css"/>
<meta charset="UTF-8">
<meta name="description" content="チャットシステム">
<meta name="keywords" content="Ajax,chat,チャット,Java,Heroku">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="chat.js"></script>
<title>トップページ</title>
</head>
<body>
<c:if test="${not empty sessionScope.LoginUser}">
<c:out value="${sessionScope.LoginUser.name}"/>でログイン中
<a href="logout">ログアウト</a>
<div id="chatform">
  <textarea name="text" rows="4" cols="50"></textarea>
  <br/>
  <button type="button" name="chat" style="margin-top:1em" onclick="chat()">発言</button>
</div>
</c:if>


<c:if test="${empty sessionScope.LoginUser}">
  <form action="./" method="post">
  <div id ="enterform">
    <input type="text" name="name" placeholder="名前をいれてね" maxlength="20" size="40"></input>
    <br/>
    <input type="submit" value="入室"/><br/>
    <div class="note">入室中の人と被らない名前である必要があります。<br/>
    /delでログイン中と同名ユーザーの発言を全て削除。<br/>
    /delallで全てのユーザーのデータを削除。
    </div>
    </div>
  </form>
</c:if>

<c:if test="${not empty sessionScope.ErrorMessage}">
<div class="error"><c:out value="${sessionScope.ErrorMessage}"/></div>
</c:if>
<c:if test="${not empty sessionScope.LoginUser}">
  <div id="messages"></div>
  </c:if>
</body>
</html>