//для строгой проверки типов
"use strict";

$(document).ready(function () {
    $("#submit").click(function () {
        $("#erconts").fadeIn(500);
        $.ajax(
            {
                type: "POST",
                url: "/test", // Адрес обработчика
                data: $("#callbacks").serialize(),
                error: function () {
                    $("#erconts").html("Произошла ошибка!");
                },
                beforeSend: function () {
                    $("#erconts").html("Отправляем данные...");
                },
                success: function (result) {
                    $("#erconts").html(result);
                    checkThis();
                }
            });
        return false;
    });
});