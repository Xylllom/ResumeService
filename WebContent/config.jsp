<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Resume Web Service</title>
		<link rel="icon" type="image/png" href="icon.png"/>
	</head>
	<body>
		<h1 align="center">Choix de la configuration</h1><br/>
			<h3>Veuillez sélectionner votre configuration</h3>
		<form action="config" method="get">
			<select name="choix2">
				<c:forEach var="nomConfig" items="${sessionScope.dispo}" varStatus="index">
				<option value="${nomConfig}">${nomConfig}</option>
				</c:forEach>
			</select>
			<p><input type="submit" value="suivant"></p>
		</form>
	</body>
</html>