<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
  <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<c:if test="${list.isEmpty()}">
  <p>Nomeals</p>
</c:if>

<c:if test="${!list.isEmpty()}">
  <div>
    <table border="3" style="border: black">
      <th colspan="3" style="text-align: center;">Meals</th>
      <tr id="title">
        <th>Date, Time</th>
        <th>Description</th>
        <th>Calories</th>
      </tr>
      <c:forEach var="mealTo" items="${list}">
        <jsp:useBean id="mealTo" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr class="${mealTo.excess ? "excess" : "notexcess"}" style="color: ${mealTo.excess ? 'red' : 'green'};">
          <td align="center">${dateTimeFormatter.format(mealTo.dateTime)}</td>
          <td align="center">${mealTo.description}</td>
          <td align="center">${mealTo.calories}</td>
        </tr>
      </c:forEach>
    </table>
  </div>
</c:if>
</body>
</html>