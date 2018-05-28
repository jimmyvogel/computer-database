<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<section id="main">
	<div class="container">	
	<spring:message code="home.welcome.title"/>
		<ul>
			<li><spring:message code="home.welcome.add"/></li>
			<li><spring:message code="home.welcome.listcomputer"/></li>
			<li><spring:message code="home.welcome.listcompany"/></li>
			<li><spring:message code="home.welcome.modify"/></li>
		</ul>
		<spring:message code="home.welcome.inprod"/>
	</div>
</section>