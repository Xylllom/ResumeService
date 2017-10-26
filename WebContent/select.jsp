<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Resume Service</title>
		<link rel="icon" type="image/png" href="icon.png"/>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
	</head>
	<body>
	<h1 align="center">Sélection de(s) corpus à résumer</h1>
		<p>Veuillez sélectionner le(s) ID de corpus qui seront résumé :</p>
 		<form action="select" method="post">		
			<input id="all" type="checkbox" onclick="coche('s')" />Tout sélectionner
			<c:forEach var="num" items="${sessionScope.nomCorpus}" varStatus="var">
			<li><input type="checkbox" class="s" name="choix" value="${var.index}">ID CORPUS N°${var.index}</li>
			</c:forEach>
			<p> <input type="submit" value="suivant"> </p>
		</form>
	</body>
<script type="text/javascript" >
function coche(element){
    tab = document.getElementsByClassName(element);
    if (document.getElementById('all').checked){
        for (var i = 0; i < tab.length; i++){
            tab[i].checked = true;
        }
    }
    else {
        for (var i = 0; i < tab.length; i++){
            tab[i].checked = false;
        }
    }
}
</script>
</html>