<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Note Page</title>

    <meta http-equiv="X-Ua-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>


    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/styles.css">
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,700&amp;subset=latin-ext" rel="stylesheet">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

</head>

<body>

<jsp:include page="fragments/navbar.jspf"/>
<br><br><br><br>

<c:choose>
    <c:when test="${fragment eq 'register'}">
        <jsp:include page="fragments/register.jspf"/>
    </c:when>
    <c:when test="${fragment eq 'notes'}">
        <jsp:include page="fragments/notes.jspf"/>
    </c:when>
    <c:when test="${fragment eq 'add'}">
        <jsp:include page="fragments/add.jspf"/>
    </c:when>
    <c:when test="${fragment eq 'search'}">
        <jsp:include page="fragments/search.jspf"/>
    </c:when>
    <c:when test="${fragment eq 'account'}">
        <jsp:include page="fragments/account.jspf"/>
    </c:when>
    <c:when test="${fragment eq 'deleteAccount'}">
        <jsp:include page="fragments/deleteAccount.jspf"/>
    </c:when>
    <c:when test="${fragment eq 'resetPassword'}">
        <jsp:include page="fragments/resetPassword.jspf"/>
    </c:when>
    <c:otherwise>
        <jsp:include page="fragments/login.jspf"/>
    </c:otherwise>
</c:choose>


<jsp:include page="fragments/footer.jspf"/>

<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
    $(function () {
        $("#datepicker").datepicker();
    });
</script>
<script>
    $(document).ready(function(){
        $("#myInput").on("keyup", function() {
            var value = $(this).val().toLowerCase();
            $("#myTable div").filter(function() {
                $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
            });
        });
    });
</script>
</body>
</html>

