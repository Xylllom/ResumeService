<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Resume Service</title>
        <link rel="icon" type="image/png" href="icon.png"/>
        <link rel="stylesheet" type="text/css" href="general.css">
    </head>
    <body>
    	<h1 align="center">Bienvenue sur le service de résumé automatique</h1><br/>
       		<p id=error>${requestScope.message}</p>
       		<p id=terminate>${requestScope.message}</p>
       		<p>Voici l'implémentation du service de résumé automatique développé dans le cadre du projet ASADERA</p>
       		<p>Cette interface permet de paramétrer la configuration de l'outil et concevoir un texte synthétique à partir d'une archive de texte</p>
       		<p>Pour débuter, cliquez sur le bouton ci-contre</p>
            	<form action="charge" method="get" >
                <button name="start" value="ok" type="submit">Démarrer le service</button>
            </form>   
    </body>
</html>