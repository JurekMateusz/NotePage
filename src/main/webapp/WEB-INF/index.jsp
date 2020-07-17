<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Note Page</title>

    <meta http-equiv="X-Ua-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/styles.css">
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,700&amp;subset=latin-ext" rel="stylesheet">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <!--Fontawesome CDN-->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">
</head>

<body>

<jsp:include page="fragments/navbar.jspf"/>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger text-center" role="alert">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            <h3 ><strong><c:out value="${errorMessage}"/></strong></h3>
        </div>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success text-center " role="alert">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            <h3 ><strong><c:out value="${successMessage}"/></strong></h3>
        </div>
    </c:if>


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

<%--<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">--%>
<%--<script src="https://code.jquery.com/jquery-1.12.4.js"></script>--%>
<%--<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>--%>
<%--<script>--%>
<%--    $(function () {--%>
<%--        $("#datepicker").datepicker();--%>
<%--    });--%>
<%--// </script>--%>

<%----%>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js" integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI" crossorigin="anonymous"></script>

<script>
    $(document).ready(function(){
        $("#myInput").on("keyup", function() {
            var value = $(this).val().toLowerCase();
            $("#notes div").filter(function() {
                $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
            });
        });
    });
</script>

</body>
</html>

