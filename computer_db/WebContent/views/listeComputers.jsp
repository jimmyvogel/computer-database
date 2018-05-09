<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="../WEB-INF/tags.tld" prefix="pg" %> 
 
<h1 id="homeTitle" class="text-center">
    ${ page.count } Computers
</h1>

<form id="deleteForm" action="computer" method="POST">
    <input type="hidden" value="${applicationScope.actions.DELETE_COMPUTER}" name="action"/>
    <input type="hidden" name="selection" value="">
</form>
        
<div class="container" style="margin-top: 10px;">

    <form id="searchForm" action="computer" method="GET" class="form-inline">
    	<input type="hidden" value="${applicationScope.actions.SEARCH_COMPUTER}" name="action"/>
        <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search..." />
        <input type="submit" id="searchsubmit" value="Search" class="btn btn-primary" />
    </form>
         
	<table class="table table-striped table-bordered">
		<thead>
			<tr>
				<th> 
					<input type="checkbox" id="selectall" /> 
                    <span style="vertical-align: top;"> -  
						<a href="#" id="deleteSelected" onclick="$.fn.deleteSelected();">
							<i class="fa fa-trash-o fa-lg"></i>
						</a>
					</span>
				</th>
				<th>Computer name</th>
				<th>Introduced date</th>
				<th>Discontinued date</th>
				<th>Company</th>
			</tr>
		</thead>
		<!-- Browse attribute computers -->
		<tbody id="results">
		    <c:forEach items="${page.objects}" var="computer">
				<tr>
					<td>
						<input type="checkbox" name="cb" class="cb" value="${computer.id}">
					</td>
					<c:url value="computer" var="urlEditFormComputer">
		 				<c:param name="action" value="${applicationScope.actions.EDIT_FORM_COMPUTER}"/>
		 				<c:param name="id" value="${computer.id}"/>
	  				</c:url>
					<td><a href="${urlEditFormComputer}">${computer.name}</a></td>
					<td>${computer.introduced }</td>
					<td>${computer.discontinued }</td>
					<td>${computer.company}</td>
				</tr>
               </c:forEach>
		</tbody>
	</table>
</div>

<div class="navbar">
	<pg:pager target="computer" action="${actionPagination}" page="${page}"> </pg:pager>
</div>

