<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User consent</title>
</head>
<body>
<!-- Present user info data based on the scope defined in the client original openid request -->
<!-- scope: 
openid -> OpenID authentication and ID token
email {email, email_verified}, 
phone {phone_number, phone_number_verified}, 
profile {name, family_name, given_name, middle_name, nickname, profile, picture, website, gender, birthdate, etc},
address {address} 
-->

<h3>Consent</h3>
	<h4>Personal details</h4>
	<p><font color="red">${message}</font></p>
	<p>First name: ${user.firstname}<br>
	   Last name: ${user.lastname}<br>
	   Mobile phone: ${user.mobilephone}</p>
	<br>
<h4><b>Allow DAT152BlogWebApp access to your:</b></h4>
<form method="post" action="authorizehelper">
<table>
	<c:forEach var="consent" items="${consents}">
		<c:if test = "${consent=='profile'}">
			<tr><td>profile</td><td><input type= "checkbox" id="profile" name="profile" value="profile" checked></td></tr>
		</c:if>
		<c:if test = "${consent=='email'}">
			<tr><td>email</td><td><input type= "checkbox" id="email" name="email" value="email" checked></td></tr>
		</c:if>
		<c:if test = "${consent=='phone'}">
			<tr><td>phone</td><td><input type= "checkbox" id="phone" name="phone" value="phone" checked></td></tr>
		</c:if>
		<c:if test = "${consent=='address'}">
			<tr><td>address</td><td><input type= "checkbox" id="address" name="address" value="address" checked></td></tr>
		</c:if>
	</c:forEach>

	<tr><td><input type="submit" name="submit" value="Allow"></td><td><input type="submit" name="submit" value="Deny"></td></tr>
</table>
</form>


</body>
</html>