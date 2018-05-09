<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<c:if test="${not empty error}">
	<div class="alert-warning" id="error">
		<p>
			${error}
		</p>
	</div>
</c:if>
<c:if test="${not empty success}">
	<div class="alert-success" id="success">
		${success}
	</div>
</c:if>