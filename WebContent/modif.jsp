<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Configurateur</title>
<script src="modif.js"></script>
</head>
<body>
	<form action="modif" method="post">
	<select name="process" onChange="Refresh()">
		<c:forEach var="name" items="${requestScope.dispoProcess}" varStatus="index">
		<option value="${name}">${name}</option>
		</c:forEach>
	</select>
	<br/>
	<select name="scoring" onChange="Refresh()">
		<c:forEach var="name" items="${requestScope.dispoScoring}" varStatus="index">
		<option value="${name}">${name}</option>
		</c:forEach>
	</select>
	<br/>
	<select name="scoring" onChange="Refresh()">
		<c:forEach var="name" items="${requestScope.dispoSummarize}" varStatus="index">
		<option value="${name}">${name}</option>
		</c:forEach>
	</select>
	</form>
</body>
</html>