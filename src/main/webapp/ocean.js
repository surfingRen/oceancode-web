$(document).keydown(function(e) {
	if (e.which === 27) {

	}
});
$(function() {
	$.ajax({
		url : 'rs/test/hasLogin',
		type : 'GET',
		cache : false,
		success : function(vo) {
			if ('1' != vo) {
				alert('登录信息超时，请重新登录!');
				window.location.href = 'login.html?t=' + new Date();
			}
		}
	});
	if ($.cookie('oceancode_lastPage'))
		load($.cookie('oceancode_lastPage'));
})

function load(url) {
	$.cookie('oceancode_lastPage', url);
	$('.content-wrapper').html('<div class="overlay"><i class="fa fa-refresh fa-spin"></i></div>');
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
			$('.content-wrapper').html(h);
		}
	});
	return false;
}
