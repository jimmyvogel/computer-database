<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="../tags.tld" prefix="pg" %> 
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<h1 id="homeTitle" class="text-center">
    ${ page.totalElements } <spring:message code="companies"/>
</h1>
<div class="container" style="margin-top: 10px;">
	
    <form id="searchForm" action="${applicationScope.actions.SEARCH_COMPANY}" method="GET" class="form-inline">
        <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search..." />
        <input type="submit" id="searchsubmit" value="Search" class="btn btn-primary" />
    </form>
    
	<table class="table table-striped table-bordered">
		<thead>
			<tr>
				<th><spring:message code="company.id"/></th>
				<th><spring:message code="company.name"/></th>
			</tr>
		</thead>
		<!-- Browse attribute computers -->
		<tbody id="results">
		    <c:forEach items="${page.content}" var="company">
				<tr>
					<td>${company.id }</td>
					<td>${company.name}</td>
				</tr>
            </c:forEach>
		</tbody>
	</table>
</div>

<div class="navbar">
    <c:if test="${empty search}">
        <pg:pager action="${actionPagination}" page="${page}"> </pg:pager>
    </c:if>
    <c:if test="${not empty search}">
        <pg:pager action="${actionPagination}" page="${page}" params="search=${search}"> </pg:pager>
    </c:if>
</div>

