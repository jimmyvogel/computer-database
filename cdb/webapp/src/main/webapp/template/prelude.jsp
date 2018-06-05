<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Computer Database</title>
<!-- Bootstrap -->
<link href="<c:url value="/static/css/bootstrap.min.css" />"
	rel="stylesheet">
<link href="<c:url value="/static/css/font-awesome.css" />"
	rel="stylesheet">
<link href="<c:url value="/static/css/main.css" />" rel="stylesheet">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-3">
					<a class="navbar-brand" href="dashboard.html"> Application -
						Computer Database </a>
				</div>
				<div class="col-md-7">
					<ul class="nav navbar-nav">
						<c:set var="contextPath"
							value="${pageContext.request.contextPath}" />
						<li><a href="${contextPath }/index">Home</a></li>
						<li><a
							href="${contextPath }/computer/${applicationScope.actions.ADD_FORM_COMPUTER}">Add
								entry</a></li>
						<li><a
							href="${contextPath }/computer/${applicationScope.actions.LIST_COMPUTERS}?page=1&limit=20">Computers</a></li>
						<li><a
							href="${contextPath }/company/${applicationScope.actions.LIST_COMPANIES}?page=1&limit=20">Companies</a></li>

					</ul>
				</div>
				<div class="col-md-2">
					<form class="navbar-form navbar-right"
						action="${pageContext.request.contextPath}/${applicationScope.actions.LOGOUT}"
						method="post">
						<input class="form-control btn pull-right" value="Logout" type="submit">
					</form>
				</div>
			</div>
		</div>
	</header>