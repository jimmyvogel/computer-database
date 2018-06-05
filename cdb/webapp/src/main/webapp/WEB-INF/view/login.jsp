<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div class="container">
	<div class="row">
		<div class="col-xs-8 col-xs-offset-2 box">

			<form id="formLogin" action="${applicationScope.actions.LOGIN}"
				method="post" class="form-signin">
				<h2 class="form-signin-heading">Connection</h2>
				<label for="username" class="sr-only">Username</label> <input
					type="text" id="username" name="username" class="form-control"
					placeholder="Username" required autofocus> <label
					for="password" class="sr-only">Password</label> <input
					type="password" id="password" name="password" class="form-control"
					placeholder="Password" required>
				<button class="btn btn-lg btn-primary btn-block" type="submit">Login</button>
			</form>
		</div>
	</div>
</div>