<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Resume Service</title>
		<link rel="icon" type="image/png" href="icon.png"/>
	</head>
	<body>
		<h1 align="center">Personnalisation des paramètres</h1><br/>
		<h3>Veuillez modifier les valeurs utilisées pour le résumé</h3>
		<p> Ces renseignements sont entièrement facultatifs </p>
		<p> Les valeurs par défaut seront utilisées si rien n'a été saisi </p>
		<form action="config" method="post">
		
		Multithreading <input type="checkbox" name="multi">
		
		<c:forEach var="valConfig" items="${requestScope.customVal}" varStatus="index">
		<p align="justify"> Option ${index.count} : ${requestScope.customName[index.index]} <input type="text" size="50" name="choix3" value="${valConfig}"></p>
		</c:forEach>
			<p><input type="submit" value="Suivant"></p>
		</form>
	</body>
</html>