<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<footer>
    <!-- Dropdown for selecting language -->
    <div class="dropdown">
       <button class="btn btn-danger dropdown-toggle" type="button" id="dropdownMenuButton"
          data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><spring:message code="app.lang.title"/></button>
       <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
          <a class="dropdown-item" href="${contextPath}/${applicationScope.actions.HOME}?lang=en"><spring:message code="app.lang.english"/></a> 
          <a class="dropdown-item" href="${contextPath}/${applicationScope.actions.HOME}?lang=fr"><spring:message code="app.lang.french"/></a>
       </div>
    </div>
    <div class="text-center">
    	<p>
    		2018 - computerdatabase - Excilys
    	</p>
    </div>
</footer>