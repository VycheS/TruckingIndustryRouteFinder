//для строгой проверки типов
"use strict";

let app = new App();

//закрываем всё на всякий случай
location.hash = '#close';
//добавление события к форме модального окна
let modalNewInformationLayot = document.querySelector('#newInformationLayot');
modalNewInformationLayot.addEventListener('submit', function (e) {
    e.preventDefault();
    app.createLayer(this.name.value, this.type.value);
    location.hash = '#close';//закрываем
    modalNewInformationLayot.reset();//очищаем после ввода
});

let modalNewTruckingIndustryLayot = document.querySelector('#newTruckingIndustryLayot');
modalNewTruckingIndustryLayot.addEventListener('submit', function (e) {
    e.preventDefault();
    console.log(this.name.value + " " + this.type.value);
    app.createTruckingIndustryLayer(this.name.value, this.type.value);
    location.hash = '#close';//закрываем
    modalNewTruckingIndustryLayot.reset();//очищаем после ввода
});








