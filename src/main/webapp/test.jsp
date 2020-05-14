<%@ page import="pl.mjurek.notepage.model.User" %>
<%@ page import="pl.mjurek.notepage.model.ImportantState" %>
<%@ page import="pl.mjurek.notepage.model.StatusNote" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<% User loggedUser = (User) request.getAttribute("loggedUser"); %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>TODO Note Page</title>

    <meta http-equiv="X-Ua-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>


    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/styless.css">
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,700&amp;subset=latin-ext" rel="stylesheet">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
    <!-- Latest compiled and minified JavaScript -->
</head>

<body>

<jsp:include page="WEB-INF/fragments/navbar.jspf"/>

<br><br><br><br>
<c:if test="${not empty requestScope.notesList}">
<c:forEach var="note" items="${requestScope.notesList}">

<div class="container">
    <c:choose>
    <c:when test="${note.importantState eq ImportantState.JUST_REMEMBER  && note.statusNote eq StatusNote.TODO}">
    <div class="row bs-callout bs-callout-default col-sm-offset-1">
        </c:when>
        <c:when test="${note.importantState eq ImportantState.IMPORTANT && note.statusNote eq StatusNote.TODO}">
        <div class="row bs-callout bs-callout-warning col-sm-offset-1">
            </c:when>
            <c:when test="${note.importantState eq ImportantState.VERY_IMPORTANT  && note.statusNote eq StatusNote.TODO}">
            <div class="row bs-callout bs-callout-danger col-sm-offset-1">
                </c:when>
                <c:when test="${note.statusNote eq StatusNote.MADE}">
                <div class="row bs-callout bs-callout-success col-sm-offset-1">
                    </c:when>
                    <c:otherwise>
                    <div class="row bs-callout bs-callout-primary col-sm-offset-1">
                        </c:otherwise>
                        </c:choose>
                        <div class="col  col-md-11 col-sm-10">
                            <h6><medium>Added: <fmt:formatDate value="${note.date.dateStickNote}" pattern="dd/MM/YYYY"/>,
                                <strong>Deadline: <fmt:formatDate value="${note.date.dateDeadlineNote}" pattern="dd/MM/YYYY"/></strong>
                                <c:if test="${not empty note.date.dateUserMadeTask}">
                                    Made: <fmt:formatDate value="${note.date.dateUserMadeTask}" pattern="dd/MM/YYYY"/>
                                </c:if>
<%--                                <p> Status: <c:out value="${note.importantState}"/></p>--%>
                            </medium>
                            </h6>
                            <p><c:out value="${note.description}"/></p>
                        </div>
                        <div class="col col-md-1 col-sm-2">
                            <a href="${pageContext.request.contextPath}/note?note_id=${note.id}&note=DONE"
                               class="btn btn-block btn-primary btn-success"><span
                                    class="glyphicon glyphicon-ok-circle"></span> </a>

                            <a href="${pageContext.request.contextPath}/note?note_id=${note.id}&note=WRENCH"
                               class="btn btn-block btn-primary btn-warning"><span
                                    class="glyphicon glyphicon-wrench"></span> </a>

                            <a href="${pageContext.request.contextPath}/note?discovery_id=${discovery.id}&vote=DELETE"
                               class="btn btn-block btn-primary btn-danger"><span
                                    class="glyphicon glyphicon-trash"></span> </a>
                        </div>
                    </div>
                </div>
                </c:forEach>
                </c:if>


                <jsp:include page="WEB-INF/fragments/footer.jspf"/>

                <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
                <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
                <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
                        integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
                        crossorigin="anonymous"></script>
                <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
                <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
                <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
                <script>
                    $(function () {
                        $("#datepicker").datepicker();
                    });
                </script>
</body>
</html>

