$(function () {
    $("#datepicker").datepicker();
});

jQuery(document).ready(function ($) {

    var url = window.location.href;
    url = url.split('?')[0];
    window.history.replaceState({}, document.title, url);
    window.history.pushState({}, url);
});