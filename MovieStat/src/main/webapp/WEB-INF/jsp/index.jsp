<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="main.css">
<title>MovieStat</title>
</head>
<body>
  <h1>Movies in Theaters Now!</h1>
  <table>
    <thead>
      <tr>
        <th>Title</th>
        <th>Release Date</th>
        <th>Actors</th>
      </tr>
    </thead>
    <c:forEach items="${movies}" var="movie">
      <tr>
        <td>${movie.getTitle()}</td>
        <td>
          <fmt:formatDate value="${movie.getReleaseDate().getTime()}" type="date" dateStyle="short" />
        </td>
        <td>
          <ul>
            <i>Average age of cast: 
              <fmt:formatNumber value="${movie.getAverageAgeCast()}" maxFractionDigits="1" />
            </i>
            <c:forEach items="${movie.getCast()}" var="actor">
              <li>${actor.getName()}
                  (${actor.getAge()})</li>
            </c:forEach>
          </ul>
      </tr>
    </c:forEach>
  </table>
</body>
</html>