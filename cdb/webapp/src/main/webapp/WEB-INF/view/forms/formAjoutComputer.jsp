<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div class="container">
    <div class="row">
        <div class="col-xs-8 col-xs-offset-2 box">
            <h1>Add Computer</h1>
            <form id="formAjoutComputer" action="${applicationScope.actions.ADD_COMPUTER}" method="POST">
                <fieldset>
                    <div class="form-group">
                        <label for="computerName"><spring:message code="computer.name"/></label>
                        <input type="text" class="form-control" name="name" id="name" placeholder="Computer name">
                    </div>
                    <div class="form-group">
                        <label for="introduced"><spring:message code="computer.introduced"/></label>
                        <input type="date" class="form-control" name="introduced" id="introduced" 
                        placeholder="Introduced date (yyyy-mm-dd)">
                    </div>
                    <div class="form-group">
                        <label for="discontinued"><spring:message code="computer.discontinued"/></label>
                        <input type="date" class="form-control" name="discontinued" id="discontinued" 
                        placeholder="Discontinued date (yyyy-mm-dd)">
                    </div>
                    <div class="form-group">
                        <label for="companyId"><spring:message code="computer.company"/></label>
                        
                        <select class="form-control" id="companyId" name="companyId">
                          <option value="-1">- - - - - - - -</option>
                          <c:forEach items="${companies}" var="company">
                          		<option value="${company.id}">${company.name}</option>
                          </c:forEach>
                        </select>
                    </div>                  
                </fieldset>
                
                <div class="actions pull-right">
                    <input type="submit" value="Ajouter" class="btn btn-primary" id="buttonAdd" name="buttonAdd">
                </div>
            </form>
        </div>
    </div>
</div>