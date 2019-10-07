<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<!-- <meta charset="utf-8"> -->
<meta name="viewport" content="width=device-width,initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
<link rel="stylesheet" type="text/css" href="css/main.css">

<title>P3 EJZ360 FOAMS</title>
</head>
<body>
	<h1><a href="/P3_EJZ360_FOAM/">Welcome to FOAMS!</a></h1>
	<p>${welcome}</p>
	<table class="table">
		<thead>
			<tr>
				<th>National ID</th>
				<th>Last Name</th>
				<th>First Name</th>
				<th>Date of Birth</th>
				<th>Age</th>
				<th>Eligible</th>
				<th colspan="2">Options</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="athlete" items="${roster}">
				<tr>
					<td><c:out value="${athlete.nationalID}" /></td>
					<td><c:out value="${athlete.lastName}" /></td>
					<td><c:out value="${athlete.firstName}" /></td>
					<td><c:out value="${athlete.dateOfBirth}" /></td>
					<td><c:out value="${athlete.age}" /></td>
					<td><c:out value="${athlete.eligible}" /></td>
					<td><a href='/edit?id="<c:out value="${athlete.nationalID}" />"'>edit</a></td>
					<td><a href='/delete?id="<c:out value="${athlete.nationalID}" />"'>delete</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="${!empty errMsg ? 'alert alert-danger' : '' }" role="alert"><c:out value="${errMsg}" /></div>

	<form name="form-add" id="form-add" action="add.jsp" method="post">
		<input type="submit" value="Add athlete" id="submit-add" />
		
	</form>

</body>
</html>