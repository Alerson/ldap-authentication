$(function() {	
	var env = null;
	var envText = null;
	
	$( document ).ready(function() {
		envText = sessionStorage.getItem("envInfo");
		if(envText != "null"){
		    env = document.getElementById("envId");
		    env.value = sessionStorage.getItem("envInfo");
		}
	});
	
	$("#envId").change(function(e){
		env = document.getElementById("envId");
		envText = env.options[env.selectedIndex].text;
		if (envText != sessionStorage.getItem("envInfo")) {
			alert("Environment: "+envText+ " selected!");
		}
		sessionStorage.setItem("envInfo", envText);
		
	});

	$("form").submit(function(e){
		env = document.getElementById("envId");
		envText = env.options[env.selectedIndex].text;
		$('input[name="environment"]').val(envText);
	});
	
});