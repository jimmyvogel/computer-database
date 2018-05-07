<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<nav class="navbar navbar-default">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="#">Actions</a>
		</div>
		<ul class="nav navbar-nav">

			<!--  NAVIGATION HOME -->
			<c:choose>
				<c:when test="${empty etat or etat==applicationScope.actions.HOME}">
					<li class="active"><a href="computer">Home</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="computer">Home</a></li>
				</c:otherwise>
			</c:choose>

			<!--  NAVIGATION ADD COMPUTER -->
			<c:url value="computer" var="urlAddComputer">
				<c:param name="action"
					value="${applicationScope.actions.ADD_FORM_COMPUTER}" />
			</c:url>
			<c:choose>
				<c:when
					test="${not empty etat and etat==applicationScope.actions.ADD_FORM_COMPUTER}">
					<li class="active"><a href="${urlAddComputer}">Add entry</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="${urlAddComputer}">Add entry</a></li>
				</c:otherwise>
			</c:choose>

			<!--  NAVIGATION LIST COMPUTER -->
			<c:url value="computer" var="urlListComputers">
				<c:param name="action" value="${applicationScope.actions.LIST_COMPUTERS}" />
				<c:param name="page" value="1" />
			</c:url>
			<c:choose>
				<c:when
					test="${not empty etat and etat==applicationScope.actions.LIST_COMPUTERS}">
					<li class="active"><a href="${urlListComputers}">Computers</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="${urlListComputers}">Computers</a></li>
				</c:otherwise>
			</c:choose>

			<!--  NAVIGATION LIST COMPANY -->
			<c:url value="computer" var="urlListCompanies">
				<c:param name="action" value="${applicationScope.actions.LIST_COMPANIES}" />
				<c:param name="page" value="1" />
			</c:url>
			<c:choose>
				<c:when
					test="${not empty etat and etat==applicationScope.actions.LIST_COMPANIES}">
					<li class="active"><a href="${urlListCompanies}">Companies</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="${urlListCompanies}">Companies</a></li>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
</nav>