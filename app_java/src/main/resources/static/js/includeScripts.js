//для строгой проверки типов
"use strict";

//---------------ПОДКЛЮЧЕНИЕ СКРИПТОВ----------------------
//подключение api карты
addScripts('', ['https://api-maps.yandex.ru/2.1/?apikey=c7544186-fcd7-4a87-a03e-1d3b6eda314d&load=package.full&lang=ru_RU'], { async: true })
    //подключение своих модулей карты
    .then(script => addScripts('./js/app/map/module/', ['arrow.js',]))
    /* Функция ymaps.ready() будет вызвана, когда загрузятся 
    все компоненты API, а также когда будет готово DOM-дерево. */
    .then(script => ymaps.ready()
        .then(script => addScripts('./js/app/dto/nested_database_entities/',
            [
                'CoordinateDTO.js',
                'ClientDTO.js',
                'GeoObjectDTO.js',
                'LayerGroupDTO.js',
                'LayerDTO.js'
            ]
        ))
        .then(script => addScripts('./js/app/dto/business_entity/',
            [
                'PointingArrowDTO.js',
                'GoodsDTO.js',
                'RouteDTO.js',
                'StoreDTO.js',
                'TruckDTO.js'
            ]
        ))
        .then(script => addScripts('./js/app/restapi/extends/',['CRUD.js']))
        .then(script => addScripts('./js/app/restapi/',
            [
                'ClientCRUD.js',
                'GeoObjectCRUD.js',
                'LayerGroupCRUD.js',
                'LayerCRUD.js'
            ]
        ))
        .then(script => addScripts('./js/app/map/extension/',
            [
                'MapLayerGeoObjectManager.js',
                'ManagerButtonsGeoObj.js'
            ]
        ))
        .then(script => addScripts('./js/app/map/extension/control/',
            [
                'MapModesControl.js',
                'EditInformationLayersControl.js',
                'EditTruckingIndustryLayersControl.js',
                'MapLegendControl.js'
            ]
        ))
        //подключение главного класса
        .then(script => addScripts('./js/app/', ['App.js']))
        //подключение главного стартового скрипта
        .then(script => addScripts('./js/', ['main.js']))
    )


//---------------ФУНКЦИЯ ДОБАВЛЕНИЯ СКРИПТОВ----------------------
async function addScripts(path, scripts, options = { async: true, appendTo: 'body' }) {
    //возвращаем промисс чтобы подключить следующий скрипт после подключения этого
    return await new Promise((resolve, reject) => {
        //локальные стандартные настройки добавления скриптов
        let addOptions = {
            async: false,
            appendTo: 'body'
        }
        //защита от дурака!!! Проверка типа options и его элементов,
        //если всё соответствует меняем элементы стандартной настройки добавления
        if ((typeof (options) == 'object')) {
            if ((options.appendTo !== undefined) && ((options.appendTo === 'head') || (options.appendTo === 'body'))) {
                addOptions.appendTo = options.appendTo;
            }
            if ((options.async !== undefined) && (typeof (options.async) == 'boolean')) {
                addOptions.async = options.async;
            }
        }
        //добавляем scripts лежащие в path 
        for (let i = 0; i < scripts.length; i++) {
            let script = document.createElement('script');
            script.src = path + scripts[i];
            script.async = addOptions.async;

            script.onload = () => resolve(script);//возвращаем в случае успешного подключения
            script.onerror = () => reject(new Error(`Ошибка загрузки скрипта ${script.src}`)); //если ошибка при подключении

            document[addOptions.appendTo].appendChild(script);
        }
    });
}

//этот скрипт закоментированный решил оставить. Возможно может пригодиться
// document.addEventListener("DOMContentLoaded", () => {});

