//Formulaires

	//Formulaire Ajout Computer.
	$(document).ready(function() {
	   $("#formAjoutComputer").validate({
	      rules: {
	         computerName:{
	            required: true,
	            minlength: 3,
	            maxlength: 60
	         }
	      },
	      messages: {
	          computerName: {
	              required: "Le nom est requis.",
	              minlength: "Au moins 3 caractères.",
	              maxlength: "Le nom doit faire au plus 60 caractères."
	          }
	      },
	      submitHandler: function(form){
	    	  form.submit();
	      }
	   })
	});