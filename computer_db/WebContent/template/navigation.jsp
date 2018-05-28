<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<nav class="navbar navbar-default">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="#">-></a>
		</div>
		<ul class="nav navbar-nav">

			<!--  NAVIGATION HOME -->
			<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
			<li><a href="${contextPath }/index">Home</a></li>
			<li><a href="${contextPath }/computer/${applicationScope.actions.ADD_FORM_COMPUTER}">Add entry</a></li>
			<li><a href="${contextPath }/computer/${applicationScope.actions.LIST_COMPUTERS}?page=1&limit=20">Computers</a></li>
			<li><a href="${contextPath }/company/${applicationScope.actions.LIST_COMPANIES}?page=1&limit=20">Companies</a></li>
		</ul>
	</div>
</nav>