<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h1 id="homeTitle">
    ${ nbCompanies } Companies
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
					<td><a href="editComputer.jsp" onclick="">${computer.name }</a></td>
					<td>${computer.introduced }</td>
					<td>${computer.discontinued }</td>
					<td>${computer.company}</td>
				</tr>
               </c:forEach>
		</tbody>
	</table>
</div>

<div class="navbar">
	<div class="container text-center">
		<ul class="pagination">
			<li><a href="#" aria-label="Previous"> <span
					aria-hidden="true">&laquo;</span>
			</a></li>
               <c:forEach items="${pages}" var="page" varStatus="status">
                    <c:url value="computer" var="urlListComputers">
		 				<c:param name="action" value="${applicationScope.actions.LIST_COMPUTERS}"/>
		 				<c:param name="page" value="${status.getCount()}"/>
	  				</c:url>
               		<li><a href="${urlListComputers}">${page}</a></li>
               </c:forEach>
			<li><a href="#" aria-label="Next"> <span aria-hidden="true">&raquo;</span>
			</a></li>
		</ul>

		<div class="btn-group btn-group-sm pull-right" role="group">
			<button type="button" class="btn btn-default">10</button>
			<button type="button" class="btn btn-default">50</button>
			<button type="button" class="btn btn-default">100</button>
		</div>
	</div>
</div>

