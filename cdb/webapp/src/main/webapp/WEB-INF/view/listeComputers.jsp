<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="../tags.tld" prefix="pg" %> 
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<h1 id="homeTitle" class="text-center">
    ${ page.count } <spring:message code="computers"/>
</h1>

<form id="deleteForm" action="${applicationScope.actions.DELETE_COMPUTER}" method="POST">
    <input type="hidden" name="selection" value="">
</form>
        
<div class="container" style="margin-top: 10px;">

    <form id="searchForm" action="${applicationScope.actions.SEARCH_COMPUTER}" method="GET" class="form-inline">
        <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search..." />
        <input type="submit" id="searchsubmit" value="Search" class="btn btn-primary" />
    </form>
         
	<table class="table table-striped table-bordered" id="tableau_computers">
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
				<th><spring:message code="computer.name"/></th>
				<th><spring:message code="computer.introduced"/></th>
				<th><spring:message code="computer.discontinued"/></th>
				<th><spring:message code="computer.company"/></th>
			</tr>
		</thead>
		<!-- Browse attribute computers -->
		<tbody id="results">
		    <c:forEach items="${page.objects}" var="computer">
				<tr>
					<td>
						<input type="checkbox" name="cb" class="cb" value="${computer.id}">
					</td>
					<c:url value="${applicationScope.actions.EDIT_FORM_COMPUTER}" var="urlEditFormComputer">
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
    <c:if test="${empty search}">
        <pg:pager action="${actionPagination}" page="${page}"> </pg:pager>
    </c:if>
    <c:if test="${not empty search}">
        <pg:pager action="${actionPagination}" page="${page}" params="search=${search}"> </pg:pager>
    </c:if>
</div>

