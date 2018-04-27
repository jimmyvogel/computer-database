<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="../WEB-INF/tags.tld" prefix="pg" %> 
<h1 id="homeTitle">
    ${ nbCompanies } Companies
</h1>
<div class="container" style="margin-top: 10px;">
	<table class="table table-striped table-bordered">
		<thead>
			<tr>
				<th>Id company</th>
				<th>Name company</th>
			</tr>
		</thead>
		<!-- Browse attribute computers -->
		<tbody id="results">
		    <c:forEach items="${companies}" var="company">
				<tr>
					<td>${company.id }</td>
					<td>${company.name}</td>
				</tr>
            </c:forEach>
		</tbody>
	</table>
</div>

<div class="navbar">
	<div class="container text-center">
		<ul class="pagination">
            <c:url value="computer" var="urlListCompanies">
 				<c:param name="action" value="${applicationScope.actions.LIST_COMPANIES}"/>
 				<c:param name="page" value="1"/>
			</c:url>
			<li><a href="${urlListCompanies}" aria-label="Previous"> <span
					aria-hidden="true">&laquo;</span>
			</a></li>
               <c:forEach items="${pages}" var="element" varStatus="status">
                    <c:url value="computer" var="urlListCompanies">
		 				<c:param name="action" value="${applicationScope.actions.LIST_COMPANIES}"/>
		 				<c:param name="page" value="${element}"/>
	  				</c:url>
               		<li><a href="${urlListCompanies}">${element}</a></li>
               </c:forEach>
			<li>
	            <c:url value="computer" var="urlListCompanies">
	 				<c:param name="action" value="${applicationScope.actions.LIST_COMPANIES}"/>
	 				<c:param name="page" value="${maxPage}"/>
				</c:url>
				<a href="${urlListCompanies}" aria-label="Next"> <span aria-hidden="true">&raquo;</span></a>
			</li>
		</ul>

		<div class="btn-group btn-group-sm pull-right" role="group">
			<button type="button" class="btn btn-default">10</button>
			<button type="button" class="btn btn-default">50</button>
			<button type="button" class="btn btn-default">100</button>
		</div>
	</div>
</div>

