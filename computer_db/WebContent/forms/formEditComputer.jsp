<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="container">
   <div class="row">
       <div class="col-xs-8 col-xs-offset-2 box">
           <div class="label label-default pull-right">
               ${computer.id}
           </div>
           <h1>Form edit computer</h1>

           <form action="editComputer" method="POST">
               <input type="hidden" value="0" id="id"/> <!-- TODO: Change this value with the computer id -->
               <fieldset>
                   <div class="form-group">
                       <label for="computerName">Computer name</label>
                       <input type="text" class="form-control" id="computerName" placeholder="${computer.name}">
                   </div>
                   <div class="form-group">
                       <label for="introduced">Introduced date</label>
                       <input type="date" class="form-control" id="introduced" placeholder="${computer.introduced }">
                   </div>
                   <div class="form-group">
                       <label for="discontinued">Discontinued date</label>
                       <input type="date" class="form-control" id="discontinued" placeholder="${computer.discontinued }">
                   </div>
                   <div class="form-group">
                       <label for="companyId">Company</label>
                       <select class="form-control" id="companyId" >
                         <c:forEach items="${companies}" var="company">
                         <c:choose>
					    <c:when test="${company.id==computer.companyId}">
					    	<option value="${company.id} selected">${company.name}</option>
					    </c:when>
					    <c:otherwise>
					        <option value="${company.id}">${company.name}</option>
					    </c:otherwise>
					  </c:choose>
                         </c:forEach>
                        </select>
                    </div>            
                </fieldset>
                <div class="actions pull-right">
                    <input type="submit" value="Edit" class="btn btn-primary">
                    or
                    <a href="dashboard.html" class="btn btn-default">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>