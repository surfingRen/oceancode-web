<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	session.setAttribute("shopId", request.getParameter("shopid"));
	if(session.getAttribute("userId")==null){
		response.sendRedirect("login.html");
	}
%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>OC | User Client</title>
<script src="plugins/jQuery/jQuery-2.2.0.min.js"></script>
<script>
$.ajax({
	url : 'rs/user/list/<%=session.getAttribute("shopId") %>',
	type : 'get',
	cache : false,
	success : function(h) {
		if (h == '{msg:0}') {
			alert('登录信息超时，请重新登录!');
			window.location.href = 'login.html';
			return;
		}
		$(h.list).each(function(i,e){
			
			$('body').append(e.name+'<div style="'+e.photos.replace('http:','')+'"></div><br/>'+e.note+'<br/>');
// 			id: 31,
// 			cat_id: 7,
// 			name: "黄焖鸡米饭（中份）微辣",
// 			note: "均选用上等实材，每份均包含一份米饭",
// 			photos: "background: url(http://img.waimai.bdimg.com/pb/0aed6d4e2f5a8939b02f7d5ab07c17c1d5@s_0,w_172,h_172,q_100) center center no-repeat;background-size:cover;-webkit-background-size:cover;-o-background-size:cover;-moz-background-size:cover;-ms-background-size:cover;",
// 			money: 0,
// 			money2: "19",
// 			ctime: 1460276201000,
// 			status: "1",
// 			id3: "1432878727",
// 			sales_count: "已售431份",
// 			recommend: "234人推荐",
// 			shop_id: 295
// 			},
		})
	}
});
</script>
</head>
<body>

</body>
</html>