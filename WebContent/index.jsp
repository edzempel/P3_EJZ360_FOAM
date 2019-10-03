<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<!-- <meta charset="utf-8"> -->
<meta name="viewport" content="width=device-width,initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" type="text/css" href="css/main.css">

<title>P3 EJZ360 FOAMS</title>
</head>
<body>
	<h1>Welcome to FOAMS!</h1>
	<p>${welcome}</p>
	<table>
		<thead>
			<tr>
				<th>National ID</th>
				<th>Last Name</th>
				<th>First Name</th>
				<th>Date of Birth</th>
				<th>Age</th>
				<th>Options</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="Zara,nuha,roshy" delims="," var="name">
			<tr>
				<td><c:out value="${name}" /></td>
			</tr>
			</c:forEach>
		</tbody>
	</table>

</body>
</html>