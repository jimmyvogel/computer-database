<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="container">
    <div class="row">
        <div class="col-xs-8 col-xs-offset-2 box">
            <h1>Add Computer</h1>
            <form id="formAjoutComputer" action="${applicationScope.actions.ADD_COMPUTER}" method="POST">
                <fieldset>
                    <div class="form-group">
                        <label for="computerName">Computer name</label>
                        <input type="text" class="form-control" name="computerName" id="computerName" placeholder="Computer name">
                    </div>
                    <div class="form-group">
                        <label for="introduced">Introduced date</label>
                        <input type="date" class="form-control" name="introduced" id="introduced" 
                        placeholder="Introduced date (yyyy-mm-dd)">
                    </div>
                    <div class="form-group">
                        <label for="discontinued">Discontinued date</label>
                        <input type="date" class="form-control" name="discontinued" id="discontinued" 
                        placeholder="Discontinued date (yyyy-mm-dd)">
                    </div>
                    <div class="form-group">
                        <label for="companyId">Company</label>
                        
                        <select class="form-control" id="companyId" name="companyId">
                          <option value="-1">- - - - - - - -</option>
                          <c:forEach items="${companies}" var="company">
                          		<option value="${company.id}">${company.name}</option>
                          </c:forEach>
                        </select>
                    </div>                  
                </fieldset>
                
                <div class="actions pull-right">
                    <input type="submit" value="Ajouter" class="btn btn-primary" id="buttonAjout" name="buttonAjout">
                </div>
            </form>
        </div>
    </div>
</div>