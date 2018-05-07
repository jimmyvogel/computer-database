//Formulaires


	//Formulaire Ajout Computer.
	$(document).ready(function() {
        
	   $("#formAjoutComputer").validate({
	      rules: {
	         computerName:{
	            required: true,
	            minlength: 3,
	            maxlength: 60
	         },
	         introduced:{
	        	 required: false,
	        	 regex: /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])$/
	         },
	         discontinued:{
	        	 required: false,
	        	 regex: /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])$/
	         }
	      },
	      messages: {
	          computerName: {
	              required: "Le nom est requis.",
	              minlength: "Au moins 3 caractères.",
	              maxlength: "Le nom doit faire au plus 60 caractères."
	          },
	          introduced: {
	        	  regex: "La date n'est pas conforme à yyyy-mm-dd"
	          },
	          discontinued: {
	        	  regex: "La date n'est pas conforme à yyyy-mm-dd"
	          }
	      },
	      submitHandler: function(form){
	    	  form.submit();
	      }
	   }),
	   $("#formEditComputer").validate({
		      rules: {
		         computerName:{
		            required: false,
		            minlength: 3,
		            maxlength: 60
		         },
		         introduced:{
		        	 required: false,
		        	 regex: /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])$/
		         },
		         discontinued:{
		        	 required: false,
		        	 regex: /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])$/
		         }
		      },
		      messages: {
		          computerName: {
		              minlength: "Au moins 3 caractères.",
		              maxlength: "Le nom doit faire au plus 60 caractères."
		          },
		          introduced: {
		        	  regex: "La date n'est pas conforme à yyyy-mm-dd"
		          },
		          discontinued: {
		        	  regex: "La date n'est pas conforme à yyyy-mm-dd"
		          }
		      },
		      submitHandler: function(form){
		    	  form.submit();
		      }
		   })
	});