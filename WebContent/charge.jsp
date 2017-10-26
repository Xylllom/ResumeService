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
    	<h1 align="center">Chargement de l'archive de texte</h1><br/>
       		<p></p>
       		<h3> Choisir le fichier : </h3>
       		<p> Ici, vous pouvez transférer vos documents à résumer</p>
            <form action="charge" method="post" enctype="multipart/form-data">
                <input type="file" name="file" size="60" />
                <input type="submit" value="Upload" />
            </form>   
            
    </body>
</html>