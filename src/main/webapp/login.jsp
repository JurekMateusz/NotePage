<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Note Page - Sign In</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" type="text/css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/styless.css" type="text/css" rel="stylesheet">
</head>

<body>

<jsp:include page="/WEB-INF/fragments/navbar.jspf" />

<div class="container">
    <br><br><br><br>
    <div class="col-sm-6 col-md-4 offset-md-4">
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


<jsp:include page="/WEB-INF/fragments/footer.jspf" />

<script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
<script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
<script src="resources/js/bootstrap.js"></script>
</body>
</html>