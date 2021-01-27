function klik()
				{ 
				var x = document.getElementById("options");
				  if (x.style.display === "block") {
				    x.style.display = "none";
				  } else {
				    x.style.display = "block";
				  }
				
}

function klikSearch()
				{ 
				var x = document.getElementById("searchContainer");
				var y = document.getElementById("sortContainer");
				  if (x.style.display === "block") {				  	
				    x.style.display = "none";
				    y.style.display = "none";
				  } else {
				    x.style.display = "block";
				    y.style.display = "none";
				  }
				
}
				


function klikSort()
				{ 
				var x = document.getElementById("sortContainer");
				var y = document.getElementById("searchContainer");
				  if (x.style.display === "block") {
				  	y.style.display = "none";
				    x.style.display = "none";
				  } else {
				    x.style.display = "block";
				    y.style.display = "none";
				  }
				
}
