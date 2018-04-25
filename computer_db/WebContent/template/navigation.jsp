<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">Actions</a>
    </div>
    <ul class="nav navbar-nav">
      <li class="active"><a href="computer">Home</a></li>
      <c:url value="computer" var="urlAddComputer">
 				<c:param name="action" value="${applicationScope.actions.ADD_COMPUTER}"/>
	  </c:url>
      <li><a href="${urlAddComputer}">Add entry</a></li>
      
      <c:url value="computer" var="urlListComputers">
 				<c:param name="action" value="${applicationScope.actions.LIST_COMPUTERS}"/>
 				<c:param name="page" value="1"/>
	  </c:url>
      <li><a href="${urlListComputers}">Computers</a></li>
      
      <c:url value="computer" var="urlListCompanies">
 				<c:param name="action" value="${applicationScope.actions.LIST_COMPANIES}"/>
 				<c:param name="page" value="1"/>
	  </c:url>
      <li><a href="${urlListCompanies}">Companies</a></li>
      <li>
         <form id="searchForm" action="#" method="GET" class="form-inline">

             <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search..." />
             <input type="submit" id="searchsubmit" value="Search"
             class="btn btn-primary" />
         </form>
       </li>
    </ul>
  </div>
</nav>