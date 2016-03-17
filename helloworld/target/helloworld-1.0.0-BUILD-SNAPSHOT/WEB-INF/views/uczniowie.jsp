<%-- 
    Document   : uczniowie
    Created on : 2016-03-08, 11:00:38
    Author     : Agata
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Lista uczniow!</h1>
        
        <table>
            <tr>
                <th>ID</th><th>Imie</th><th>Nazwisko</th>
            </tr>
            
            <c:forEach var="user" items="${users}">
                <tr>
                    <td>
                        <c:out value="${user.getId()}"></c:out>
                    </td>
                    <td>
                        <c:out value="${user.getImie()}"></c:out>
                    </td>
                    <td>
                        <c:out value="${user.getNazwisko()}"></c:out>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
