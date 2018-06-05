<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<footer class="navbar navbar-inverse navbar-fixed-bottom">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-3">

				<div class="dropdown">
				
					<button class="btn btn-danger dropdown-toggle" type="button"
						id="dropdownMenuButton" data-toggle="dropdown"
						aria-haspopup="true" aria-expanded="false">
						<spring:message code="app.lang.title" />
					</button>
					
					<div class="dropdown-menu pull-right" aria-labelledby="dropdownMenuButton">
						<a class="dropdown-item btn-success"
							href="${contextPath}/${applicationScope.actions.HOME}?lang=en"><spring:message
								code="app.lang.english" /></a> <a class="dropdown-item btn-info"
							href="${contextPath}/${applicationScope.actions.HOME}?lang=fr"><spring:message
								code="app.lang.french" /></a>
					</div>
					
				</div>
				
			</div>
			<div class="col-md-7">
				<ul class="nav navbar-nav center">
					<li><p class="text-muted">2018 - computerdatabase - Excilys</p></li>
				</ul>
			</div>
			<div class="col-md-2"></div>
		</div>
	</div>
</footer>