<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="chat.js"></script>
<title>トップページ</title>
<c:if test="${not empty sessionScope.LoginUser}">
<c:out value="${sessionScope.LoginUser.name}"/>でログイン中
<a href="logout">ログアウト</a>
<div id="chatform">
<textarea name="text" rows="4" cols="50"></textarea>
<button type="button" name="chat" onclick="chat()">発言</button>
</div>
<div id="messages"></div>
</c:if>


<c:if test="${empty sessionScope.LoginUser}">
not login
<form action="./" method="post">
<label for="name">名前:</label>
<input type="text" name="name" maxlength="20" size="40"></input>
<input type="submit" value="入室"/><br/>
<div class="note">入室中の人と被らない名前である必要があります。</div>
</form>
</c:if>

<c:if test="${not empty sessionScope.ErrorMessage}">
<div class="error"><c:out value="${sessionScope.ErrorMessage}"/></div>
</c:if>
</head>
<body>

</body>
</html>