$(document).ready(function (){
	if($("[name='chat']")!== null){
//テキストエリアにフォーカスがあっている時にエンターが押されるとチャットを送信する。
	$("textarea[name='text']").keyup(function(e){
		if(e.keyCode==13){
			chat();
			return false;
		}
	});
//3000ミリ秒おきにAjaxでメッセージを取得。
  var timeid =setInterval(getmessages,3000);
	}
});
//テキストエリアのデータをサーバーに送信し、応答のメッセージデータをDOMで反映させた後、テキストエリアを空にする。
function chat(){
   var data = $("textarea[name='text']").val();
   $.ajax({
	   type: 'post',
	   url: 'message',
	   data: {text:data},
	   success: function(data,dataType){
	   var $wrap = $("<div></div>",{
	   "class":"wrap"
	   });
       $(data).each(function(index,message){


         var $user = $("<div></div>",{
		"class":"user"
	});
	var $name = $("<div></div>",{
		"class":"name"
	});
	var $date = $("<div></div>",{
		"class":"date"
	});
	var $msg = $("<div></div>",{
		"class":"msg"
	});
	var $clear = $("<br/>",{
		"class":"clear"
	});
	var $img = $("<img/>",{
		src:"penguin.png"
	});

	$img.appendTo($user);
	$name.text(message.name).appendTo($user);
	$date.text(message.datetime).appendTo($user);

	var $content = $("<div></div>",{
		"class":"content"
	});

	$user.appendTo($content);

	$msg.text(message.text).appendTo($content);
	$clear.appendTo($content);
    $content.prependTo($wrap);

       });
       $("div#messages").empty().append($wrap);
       $("textarea[name='text']").val("");
	   },
	   dataType: 'json'
   });

}
//サーバーからメッセージを取得し、DOMに反映させる。
function getmessages(){
	   $.ajax({
		   type: 'post',
		   url: 'message',
		   success: function(data,dataType){
		    var $wrap = $("<div></div>",{
	   "class":"wrap"
	   });
	       $(data).each(function(index,message){


	                   var $user = $("<div></div>",{
		"class":"user"
	});
	var $name = $("<div></div>",{
		"class":"name"
	});
	var $date = $("<div></div>",{
		"class":"date"
	});
	var $msg = $("<div></div>",{
		"class":"msg"
	});
	var $clear = $("<br/>",{
		"class":"clear"
	});
	var $img = $("<img/>",{
		src:"penguin.png"
	});

	$img.appendTo($user);
	$name.text(message.name).appendTo($user);
	$date.text(message.datetime).appendTo($user);

	var $content = $("<div></div>",{
		"class":"content"
	});

	$user.appendTo($content);

	$msg.text(message.text).appendTo($content);
	$clear.appendTo($content);
    $content.prependTo($wrap);


	       });
	       $("div#messages").empty().append($wrap);
		   },
		   dataType: 'json'
	   });
}


