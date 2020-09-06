$(document).ready(function (){
	if($("[name='chat']")!== null){

	$("textarea[name='text']").keyup(function(e){
		if(e.keyCode==13){
			chat();
			return false;
		}
	});
  var timeid =setInterval(getmessages,3000);
	}
});
function chat(){
   var data = $("textarea[name='text']").val();
   $.ajax({
	   type: 'post',
	   url: 'message',
	   data: {text:data},
	   success: function(data,dataType){
	   var table = $("<table/>");
       $(data).each(function(index,message){
           var row = $("<tr/>");
           $("<td>"+message.name+"</td><td>"+message.text+"</td><td>"+message.datetime+"</td>").appendTo(row);
           row.prependTo(table);
       });
       $("div#messages").empty().append(table);
       $("textarea[name='text']").val("");
	   },
	   dataType: 'json'
   });

}
function getmessages(){
	   $.ajax({
		   type: 'post',
		   url: 'message',
		   success: function(data,dataType){
		   var table = $("<table/>");
	       $(data).each(function(index,message){
	           var row = $("<tr/>");
	           $("<td>"+message.name+"</td><td>"+message.text+"</td><td>"+message.datetime+"</td>").appendTo(row);
	           row.prependTo(table);
	       });
	       $("div#messages").empty().append(table);
		   },
		   dataType: 'json'
	   });
}


