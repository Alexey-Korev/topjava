<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meal Form</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/styles/styles.css'/>">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>

<form action="${pageContext.request.contextPath}/meals" method="post">
    <input type="hidden" name="action" value="${empty meal ? 'create' : 'edit'}">
    <c:if test="${not empty meal}">
        <input type="hidden" name="id" value="${meal.id}">
    </c:if>
    <label for="datetime">Date/Time:</label>
    <input type="datetime-local" id="datetime" name="datetime" required value="${empty meal ? '' : meal.dateTime.format(dateTimeFormatter)}">
    <br>
    <label for="description">Description:</label>
    <input type="text" id="description" name="description" required value="${empty meal ? '' : meal.description}">
    <br>
    <label for="calories">Calories:</label>
    <input type="number" id="calories" name="calories" required value="${empty meal ? '' : meal.calories}">
    <br>
    <button type="submit">${empty meal ? 'Add Meal' : 'Edit Meal'}</button>
    <button onclick="history.back()" type="button">Back</button>
</form>

</body>
</html>
