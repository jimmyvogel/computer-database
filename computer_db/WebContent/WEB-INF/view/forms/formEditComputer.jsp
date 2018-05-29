<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div class="container">
   <div class="row">
       <div class="col-xs-8 col-xs-offset-2 box">
           <h1>Form edit computer</h1>

           <form id="formEditComputer" action="${applicationScope.actions.EDIT_COMPUTER}" method="POST">
           
               <fieldset>
                   <input type="hidden" value="${computer.id}" name="id" id="id"/>
                   <div class="form-group">
                       <label for="computerName"><spring:message code="computer.name"/></label>
                       <input type="text" class="form-control" name="name" 
                       			id="name" placeholder="${computer.name}">
                   </div>
                   <div class="form-group">
                       <label for="introduced"><spring:message code="computer.introduced"/></label>
                       <input type="date" class="form-control" name="introduced" id="introduced" 
                       placeholder="${computer.introduced }">
                   </div>
                   <div class="form-group">
                       <label for="discontinued"><spring:message code="computer.discontinued"/></label>
                       <input type="date" class="form-control" name="discontinued" id="discontinued" 
                       placeholder="${computer.discontinued }">
                   </div>
                   <div class="form-group">
                       <label for="companyId"><spring:message code="computer.company"/></label>
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
                    <input type="submit" value="Edit" id="buttonEdit" class="btn btn-primary">
                </div>
            </form>
        </div>
    </div>
</div>