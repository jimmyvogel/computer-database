<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="../tags.tld" prefix="pg" %> 
<h1 id="homeTitle" class="text-center">
    ${ page.count } Companies
</h1>
<div class="container" style="margin-top: 10px;">
	
    <form id="searchForm" action="${applicationScope.actions.SEARCH_COMPANY}" method="GET" class="form-inline">
        <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search..." />
        <input type="submit" id="searchsubmit" value="Search" class="btn btn-primary" />
    </form>
    
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
	<pg:pager action="${actionPagination}" page="${page}"> </pg:pager>
</div>

