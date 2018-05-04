<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<c:if test="${not empty error}">
	<div class="alert-warning">
		<p>
			${error}
		</p>
	</div>
</c:if>
<c:if test="${not empty success}">
	<div class="alert-success">
		<p>
			${success}
		</p>
	</div>
</c:if>