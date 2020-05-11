<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Note Page - Sign In</title>

    <meta http-equiv="X-Ua-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>


    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/styless.css" >
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,700&amp;subset=latin-ext" rel="stylesheet">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
</head>

<body>

<jsp:include page="WEB-INF/fragments/navbar.jspf"/>

<div class="container">
    <br><br><br><br>
    <div class="col-sm-6 col-md-4 col-md-offset-4">
        <br><br><br><br>
        <form class="form-signin" action="#" method="post">
            <h2 class="form-signin-heading">Log in</h2>
            <input name="j_username" type="text" class="form-control" placeholder="user name*" required autofocus>
            <input name="j_password" type="password" class="form-control" placeholder="password*" required>
            <button class="btn btn-lg btn-primary btn-block" type="submit">Log in</button>
            <div class="form-group">
                <a href="#" class="btnForgetPwd" value="Login">Forget Password?</a>
                <br>
                Don't have account ? <a href="#">Register</a>
            </div>
        </form>
    </div>
</div>


<jsp:include page="WEB-INF/fragments/footer.jspf"/>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>


</body>
</html>