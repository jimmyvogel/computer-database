<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<nav class="navbar navbar-default">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="#">Actions</a>
		</div>
		<ul class="nav navbar-nav">

			<!--  NAVIGATION HOME -->
			<li class="active"><a href="computer">Home</a></li>

			<!--  NAVIGATION ADD COMPUTER -->
			<c:url value="computer/${applicationScope.actions.ADD_FORM_COMPUTER}" var="urlAddComputer"></c:url>
			<li><a href="${urlAddComputer}">Add entry</a></li>

			<!--  NAVIGATION LIST COMPUTER -->
			<c:url value="computer/${applicationScope.actions.LIST_COMPUTERS}" var="urlListComputers">
				<c:param name="page" value="1" />
				<c:param name="limit" value="20" />
			</c:url>
			<li><a href="${urlListComputers}">Computers</a></li>
			<!--  NAVIGATION LIST COMPANY -->
			<c:url value="company/${applicationScope.actions.LIST_COMPANIES}" var="urlListCompanies">
				<c:param name="page" value="1" />
				<c:param name="limit" value="20" />
			</c:url>
			<li><a href="${urlListCompanies}">Companies</a></li>
		</ul>
	</div>
</nav>