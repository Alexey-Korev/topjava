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
<a href="meals?action=create">Add Meal</a>
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
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
            <tr class="${meal.excess ? "excess" : "notexcess"}">
                <td>${dateTimeFormatter.format(meal.dateTime)}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}" class="rowButton">Edit</a></td>
                <td><a href="meals?action=delete&id=${meal.id}" class="rowButton">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>