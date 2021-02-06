
$(document).ready(function(){	
	function getAcceptedComments(){
		var book = $(".bookID");
	
		$.get("/HeapOfBooks/Comments/Accepted?isbn="+ book.text() +"", function(response){
			if(response.response == "ok"){
				var tblComment = $(".tableComments")

				var comments = response.comments
				for(var itComment in comments){
					tblComment.append(
						'<tr><th>Ocena</th><td>' + comments[itComment].rating + '</td></th></tr><tr><th>Komentar</th><td>' + comments[itComment].comment + '</td></th></tr>'
						+ '<tr><th>Datum</th><td>' + comments[itComment].commentDate+ '</td></th></tr>'
					)
				}			
			}	
		}) 
	}		
	getAcceptedComments();
	
	function createComment(){
		var tableComment = $(".tableComm");
		
		var ratingInput = tableComment.find("input[name=rating]");
		var rating = ratingInput.val();
		
		var commentInput = tableComment.find("input[name=comment]");
		var comment = commentInput.val();
		
		var bookInput = tableComment.find("input[name=isbn]");
		var bookISBN = bookInput.val();
		
		var params = {
			rating: rating,
			comment: comment,
			isbn: bookISBN
		}
		
		$.post("/HeapOfBooks/Comments/Create", params)
		
		return false
	};

	$(".commentForm").submit(createComment)

});