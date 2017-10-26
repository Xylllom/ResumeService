<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Resume Service</title>
        <link rel="icon" type="image/png" href="icon.png"/>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    </head>
    <body>
    	<h1 align="center">Résultat de votre opération</h1>
    	<p>Merci d'avoir utilisé le service résumé automatique du LIASD</p>
    	<form method="get" action="result" id="r">
           <p>Cliquez sur le bouton pour récupérer votre résumé 
           <button type="button" onclick="changeToPost()">Télécharger</button><!-- <input type="submit" value="Télécharger"/>--></p>
		</form>
		<br/>
		<p>
    </body>
<script type="text/javascript">
function changeToPost() {
	document.getElementById("r").submit();
	$("form").attr("action","${pageContext.request.contextPath}/");
}
</script>
</html>