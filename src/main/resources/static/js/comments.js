	function getWaitingComments(){	
		$.get("/HeapOfBooks/Comments/Waiting", function(response){	
			if(response.response == "ok"){
				var tblComment = $(".commentsADTable")
				
				var comments = response.comments
				for(var itComment in comments){
					tblComment.append(
							'<tr><td>'+ comments[itComment].user.userName +'</td>'
							+'<td>'+ comments[itComment].commentDate +'</td>'
							+'<td>'+ comments[itComment].comment +'</td>'
							+'<td><input type="checkbox" name="cbComment" value="'+ comments[itComment].id +'"></td></tr>'							
					)
				}			
			}	
		}) 
	}

$(document).ready(function(){	
		
	getWaitingComments();
	
	function createACC(){	
		var id = new Array();
		
		$("input:checkbox[name=cbComment]:checked").each(function(){
    		id.push($(this).val());
		});

		$.ajax({
			type:"POST",
			url:"/HeapOfBooks/Comments/Accept",
			data:{id: id},
			dataType:"text",
			traditional:true
		});			
	}
	
	function createDEC(){
		var id = new Array();
		
		$("input:checkbox[name=cbComment]:checked").each(function(){
    		id.push($(this).val());
		});

		$.ajax({
			type:"POST",
			url:"/HeapOfBooks/Comments/Decline",
			data:{id: id},
			dataType:"text",
			traditional:true
		});
	}
	
	$(document).on('click', '.Accept', function(){
		$("form").submit(createACC())
		window.location.replace("/HeapOfBooks/Books");
	});	
	
	$(document).on('click', '.Decline', function(){
		$("form").submit(createDEC())
		window.location.replace("/HeapOfBooks/Books");
	});	

});



