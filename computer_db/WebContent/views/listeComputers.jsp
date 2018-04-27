<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="../WEB-INF/tags.tld" prefix="pg" %> 
 
<h1 id="homeTitle">
    ${ nbComputers } Computers
</h1>
<div class="container" style="margin-top: 10px;">
	<table class="table table-striped table-bordered">
		<thead>
			<tr>
				<th class="editMode" style="width: 60px; height: 22px;"><input
					type="checkbox" id="selectall" /> <span
					style="vertical-align: top;"> - <a href="#"
						id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
							class="fa fa-trash-o fa-lg"></i>
					</a>
				</span></th>
				<th>Computer name</th>
				<th>Introduced date</th>
				<th>Discontinued date</th>
				<th>Company</th>
			</tr>
		</thead>
		<!-- Browse attribute computers -->
		<tbody id="results">
		    <c:forEach items="${computers}" var="computer">
				<tr>
					<td class="editMode">
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
	<pg:pager target="computer" action="${applicationScope.actions.LIST_COMPUTERS}" page="${page}"> </pg:pager>
	<div class="btn-group btn-group-sm pull-right" role="group">
		<button type="button" class="btn btn-default">10</button>
		<button type="button" class="btn btn-default">50</button>
		<button type="button" class="btn btn-default">100</button>
	</div>
</div>

