<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/styles/styles.css'/>">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<div id="commandpanel">
    <a href="mealform.jsp" class="addButton">Add Meal</a>
</div>
<div>
    <table>
        <th colspan="5" style="text-align: center;">Meals</th>
        <tr>
            <th>Date, Time</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach var="mealTo" items="${mealTos}">
            <jsp:useBean id="mealTo" type="ru.javawebinar.topjava.model.MealTo"/>
            <tr class="${mealTo.excess ? "excess" : "notexcess"}">
                <td>${dateTimeFormatter.format(mealTo.dateTime)}</td>
                <td>${mealTo.description}</td>
                <td>${mealTo.calories}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/meals?action=edit&id=${mealTo.id}" class="rowButton">Edit</a>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/meals?action=delete&id=${mealTo.id}" class="rowButton">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>