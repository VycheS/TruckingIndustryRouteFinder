//для строгой проверки типов
"use strict";

let app = new App();

//закрываем всё на всякий случай
location.hash = '#close';
//добавление события к форме модального окна
let modalNewInformationLayot = document.querySelector('#newInformationLayot');
modalNewInformationLayot.addEventListener('submit', function (e) {
    e.preventDefault();
    app.createLayer(this.name.value, this.type.value)
    location.hash = '#close';
});

let modalNewTruckingIndustryLayot = document.querySelector('#newTruckingIndustryLayot');
modalNewTruckingIndustryLayot.addEventListener('submit', function (e) {
    e.preventDefault();
    app.createTruckingIndustryLayer(this.name.value, this.type.value)
    location.hash = '#close';
});








