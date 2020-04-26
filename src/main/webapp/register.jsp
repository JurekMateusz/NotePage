<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Note Page - register</title>

    <meta http-equiv="X-Ua-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/styless.css" >
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,700&amp;subset=latin-ext" rel="stylesheet">

</head>

<body>

<jsp:include page="WEB-INF/fragments/navbar.jspf"/>

<div class="container">
    <div class="col-sm-6 col-md-4 offset-md-4">
        <form class="form-signin" method="post" action="#">
            <h2 class="form-signin-heading">Register</h2>
            <input name="inputEmail" type="email" class="form-control" placeholder="Email" required autofocus/>
            <input name="inputUsername" type="text" name="inputUsername" class="form-control"
                   placeholder="user name*" required autofocus/>
            <input name="inputPassword" type="password" class="form-control" placeholder="password*" required/>
            <input name="inputRepeatPassword" type="password" class="form-control" placeholder="repeat password*" required/>
            <button class="btn btn-lg btn-primary btn-block" type="submit">register</button>
        </form>
    </div>
</div>

<jsp:include page="WEB-INF/fragments/footer.jspf"/>

<script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
<script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
<script src="resources/js/bootstrap.js"></script>

</body>
</html>