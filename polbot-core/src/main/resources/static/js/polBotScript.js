

function isEmpty(str) {
    return (!str || 0 === str.length);
}

$(document).ready(function () {

bindUserCurrencyEvent();
function bindUserCurrencyEvent() {
    $('[data-user-currency]').each(function () {
        $(this).off("click");
        $(this).on("click", handleUserCurrencyEvent);
    });
}

function handleUserCurrencyEvent(e) {
    var caller = e.target;
    var userId = $(caller).attr('data-user-currency');
    var postData = JSON.stringify({ "userId": parseInt(userId) });
    ajaxMethodCall(postData, "/ajax/GetUserCurrencies", function (data) {
        $("#userCurrencies").html(data);
    });
}


});

function ajaxMethodCall(postData, ajaxUrl, successFunction) {

    $.ajax({
        type: "POST",
        url: ajaxUrl,
        data: postData,
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        success: successFunction,
        error: function (jqXHR, exception) {
            console.error("parameters :" + postData);
            console.error("ajaxUrl :" + ajaxUrl);
            console.error("responseText :" + jqXHR.responseText);
            if (jqXHR.status === 0) {
                console.error('Not connect.\n Verify Network.');
            } else if (jqXHR.status == 404) {
                console.error('Requested page not found. [404]');
            } else if (jqXHR.status == 500) {
                console.error('Internal Server Error [500].');
            } else if (exception === 'parsererror') {
                console.error('Requested JSON parse failed.');
            } else if (exception === 'timeout') {
                console.error('Time out error.');
            } else if (exception === 'abort') {
                console.error('Ajax request aborted.');
            } else {
                console.error('Uncaught Error.\n' + jqXHR.responseText);
            }
        }
    });
}