<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="alert-warning" id="error">
	<c:if test="${not empty error}">
		<p>${error}</p>
	</c:if>
</div>
<div class="alert-success" id="success">
	<c:if test="${not empty success}">
		<p>${success}</p>
	</c:if>
</div>