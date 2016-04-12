$(document).keydown(function(e) {
	if (e.which === 27) {

	}
});
$(function() {
	if ($.cookie('oceancode_admin_lastPage'))
		load($.cookie('oceancode_admin_lastPage'));
	else
		load('admin.index.html');
})
function loading(){
	$('#content-wrapper').show();
}
function cancelLoading(){
	$('#content-wrapper').hide();
}
function load(url,contr) {
	cancelLoading();
	if(!contr){
		contr = '.content-wrapper';
		$.cookie('oceancode_admin_lastPage', url);
	}
	$(contr).html('<div class="overlay"><i class="fa fa-refresh fa-spin"></i></div>');
	$.ajax({
		url : 'rs/oc/loadHtml?path=' + url,
		type : 'GET',
		cache : false,
		success : function(h) {
			if (h == '{msg:0}') {
				alert('登录信息超时，请重新登录!');
				window.location.href = 'login.html';
				return;
			}
			$(contr).html(h);
		}
	});
	return false;
}
