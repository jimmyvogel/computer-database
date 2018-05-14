<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="container">
   <div class="row">
       <div class="col-xs-8 col-xs-offset-2 box">
           <h1>Form edit computer</h1>

           <form id="formEditComputer" action="computer" method="POST">
               <input type="hidden" value="${computer.id}" name="id" id="id"/>
               <fieldset>
                   <div class="form-group">
                       <label for="computerName">Computer name</label>
                       <input type="text" class="form-control" name="computerName" 
                       			id="computerName" placeholder="${computer.name}">
                   </div>
                   <div class="form-group">
                       <label for="introduced">Introduced date</label>
                       <input type="date" class="form-control" name="introduced" id="introduced" 
                       placeholder="${computer.introduced }">
                   </div>
                   <div class="form-group">
                       <label for="discontinued">Discontinued date</label>
                       <input type="date" class="form-control" name="discontinued" id="discontinued" 
                       placeholder="${computer.discontinued }">
                   </div>
                   <div class="form-group">
                       <label for="companyId">Company</label>
                       <select class="form-control" id="companyId" name="companyId">
                         <option value="-1">NO CHANGE</option>
                         <option value="0">NONE</option>
                         <c:forEach items="${companies}" var="company">
                         <c:choose>
						    <c:when test="${company.id==computer.companyId}">
						    	<option value="${company.id}" selected>${company.name}</option>
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
                	<input type="hidden" value="${applicationScope.actions.EDIT_COMPUTER}" name="action"/>
                    <input type="submit" value="Edit" id="buttonEdit" class="btn btn-primary">
                </div>
            </form>
        </div>
    </div>
</div>