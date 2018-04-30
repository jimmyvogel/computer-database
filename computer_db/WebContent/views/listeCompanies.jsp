<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="../WEB-INF/tags.tld" prefix="pg" %> 
<h1 id="homeTitle">
    ${ page.count } Companies
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
		    <c:forEach items="${page.objects}" var="company">
				<tr>
					<td>${company.id }</td>
					<td>${company.name}</td>
				</tr>
            </c:forEach>
		</tbody>
	</table>
</div>

<div class="navbar">
	<pg:pager target="computer" action="${applicationScope.actions.LIST_COMPANIES}" page="${page}"> </pg:pager>
</div>

