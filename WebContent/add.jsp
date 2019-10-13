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

<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/main.css">

<title>P3 EJZ360 FOAMS: add athlete</title>
</head>
<body>

	<form class="form" name="form-new" id="form-new" action="add"
		method="get">
		<div class="form-group">
			<label for="nationalId">National ID</label> <input id="nationalId"
				name="newId" placeholder="national ID" pattern="[A-Za-z0-9]+"
				required="true" type="text" value="${param.newId}" />
		</div>
		<div class="form-group">
			<label for="form-new-lastName">Last name</label> <input
				id="form-new-lastName" name="newLast" placeholder="last name"
				pattern="[A-Za-z]+" required="true" type="text"
				value="${param.newLast}" />
		</div>
		<div class="form-group">
			<label for="form-new-firstName">First name <input
				id="form-new-firstName" name="newFirst" placeholder="fist name"
				pattern="[A-Za-z]+" required="true" type="text"
				value="${param.newFirst}" /></label>
		</div>
		<div class="form-group">
			<label for="form-new-dob">Date of birth</label> <input
				class="form-control ${errDob == null ? null: errDob ? 'is-invalid' : 'is-valid'}"
				type="date" id="form-new-dob" name="newDob" value="${param.newDob}"
				min="1900-01-01" max="2020-07-25">
			<div class="${!errDob ? 'valid-feedback' : 'invalid-feedback'}">${feedbackDobMessage}</div>
		</div>
		<div>
			<input type="hidden" name="action" value="create-new">
		</div>
		<div class="${!empty errMsg ? 'alert alert-danger' : '' }"
			role="alert">
			<c:forEach var="err" items="${errMsg}">
				<p>
					<span><c:out value="${err.key}" /> :</span> 
					<c:out value="${err.value}" />
				</p>
			</c:forEach>

		</div>
		<div>
			<input type="submit" value="Add athlete" id="submit-new" />
		</div>

	</form>


</body>
</html>