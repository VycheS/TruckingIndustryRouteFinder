class App {
    constructor() {
        // Создание карты.
        this._map = new ymaps.Map("map", {
            // Координаты центра карты: «широта, долгота».
            center: [52.033973, 113.499432],
            // Уровень масштабирования: от 0 (весь мир) до 19.
            zoom: 13,
            //включение или отключение элементов управления карты
            controls: ['zoomControl',/* 'typeSelector', */'fullscreenControl'],
            //включение или отключение способов взаимодействия с картой
            behaviors: ['drag', 'scrollZoom']
        });
        //база данных для сохранения
        this._layerDB = new LayerCRUD("/client/1/layer_group/1/layers");// пока тестовое
        //кнопки выбора геообъектов;
        this._buttonGeoObj = new ManagerButtonsGeoObj();
        //буфер для хранения контролируемый кнопками выбора геообъекта
        this._bufferCoordinates = this._buttonGeoObj.getBuffer();
        //хранилище слоёв типа данных коллекции Map
        this._layerStorage = new Map();
        //менеджер слоёв и их геообъектов
        this._mapLayerGeoObjectManager = new MapLayerGeoObjectManager(this._map, this._layerStorage);
        //лист бокс для создания и редактирования информационного слоя
        this._editInformationLayersControl = new EditInformationLayersControl(this._buttonGeoObj);
        //лист бокс для создания и редактирования грузоперевозочного слоя
        this._editTruckingIndustryLayersControl = new EditTruckingIndustryLayersControl(this._buttonGeoObj);
        //лист бокс для выбора режима карты
        this._mapModes = new MapModesControl(this._map, this._editInformationLayersControl, this._editTruckingIndustryLayersControl);
        //легенда карты
        this._legendMap = new MapLegendControl(this._mapLayerGeoObjectManager);

        //добавление списков на карту
        this._map.controls.add(this._legendMap.returnListBox(), {
            float: 'right',
            floatIndex: 0
        });
        this._map.controls.add(this._mapModes.returnListBox(), {
            float: 'left',
            floatIndex: 100
        });

        //добавляем события к карте
        this._addEventToMap();
    }

    _addEventToMap() {
        let options = {
            geodesic: true,
            strokeWidth: 5,
            opacity: 0.5
        };
        let properties = {};
        //обработка событий мыши
        this._map.events.add(['mousedown', 'mouseup'], e => {
            let eType = e.get('type');
            switch (this._buttonGeoObj.getActiveTypeGeoObj()) {
                case "point":
                    if (eType == 'mouseup') {
                        
                        //TODO добавить сюда editTruckingIndustryLayerControl
                        if (this._editInformationLayersControl.getSelectedLayer() != undefined) {
                            this._mapLayerGeoObjectManager.addGeoObject(this._editInformationLayersControl.getSelectedLayer(), new GeoObjectDTO(
                                null, //id
                                null, //name
                                'point', //type
                                null, //forwardArrowDirection
                                [new CoordinateDTO(e.get('coords')[0], e.get('coords')[1])], //coordinate
                                null, //description
                                `{\"properties\":${JSON.stringify(properties)},\"options\":${JSON.stringify(options)}}`//strJson
                            ));  
                        } else if(this._editTruckingIndustryLayersControl.getSelectedLayer() != undefined) {
                            console.log(e.get('coords')); //для теста!!!
                        } else {
                            throw Error("Слой в контролах не выбран!!!")
                        }

                    }
                    break;

                case "arrow":
                    if (eType == 'mousedown') {
                        this._bufferCoordinates.push(e.get('coords'));
                    }
                    if (eType == 'mouseup') {
                        this._bufferCoordinates.push(e.get('coords'));
                        //создаём временный массив чтобы в последствии его передать
                        let tmpCoordinates = new Array;
                        //складываем в него элементы чтобы передать копию
                        this._bufferCoordinates.forEach(item => tmpCoordinates.push(new CoordinateDTO(item[0],item[1])));
                        //TODO добавить сюда editTruckingIndustryLayerControl
                        this._mapLayerGeoObjectManager.addGeoObject(this._editInformationLayersControl.getSelectedLayer(), new GeoObjectDTO(
                            null, //id
                            null, //name
                            'arrow', //type
                            true, //forwardArrowDirection
                            tmpCoordinates, //coordinate
                            null, //description
                            `{\"properties\":${JSON.stringify(properties)},\"options\":${JSON.stringify(options)}}`//strJson
                        ));
                        this._bufferCoordinates.length = 0;
                    }
                    break;

                case "line":
                    if (eType == 'mousedown') {
                        this._bufferCoordinates.push(e.get('coords'));
                    }
                    if (eType == 'mouseup') {
                        this._bufferCoordinates.push(e.get('coords'));
                        //создаём временный массив чтобы в последствии его передать
                        let tmpCoordinates = new Array;
                        //складываем в него элементы чтобы передать копию
                        this._bufferCoordinates.forEach(item => tmpCoordinates.push(new CoordinateDTO(item[0],item[1])));
                        //TODO добавить сюда editTruckingIndustryLayerControl
                        this._mapLayerGeoObjectManager.addGeoObject(this._editInformationLayersControl.getSelectedLayer(), new GeoObjectDTO(
                            null, //id
                            null, //name
                            'line', //type
                            null, //forwardArrowDirection
                            tmpCoordinates, //coordinate
                            null, //description
                            `{\"properties\":${JSON.stringify(properties)},\"options\":${JSON.stringify(options)}}`//strJson
                        ));
                        this._bufferCoordinates.length = 0;//обнуляем счётчик хранилища координат
                    }
                    break;

                case "broken_line"://TODO Сделать добавление не кусками а полностью
                    if (eType == 'mouseup') {
                        if (this._bufferCoordinates.length >= 2) {
                            this._bufferCoordinates.shift();
                        }
                        this._bufferCoordinates.push(e.get('coords'));
                        //создаём временный массив чтобы в последствии его передать
                        let tmpCoordinates = new Array;
                        //складываем в него элементы чтобы передать копию
                        this._bufferCoordinates.forEach(item => tmpCoordinates.push(new CoordinateDTO(item[0],item[1])));
                        //TODO добавить сюда editTruckingIndustryLayerControl
                        this._mapLayerGeoObjectManager.addGeoObject(this._editInformationLayersControl.getSelectedLayer(), new GeoObjectDTO(
                            null, //id
                            null, //name
                            'line', //type
                            null, //forwardArrowDirection
                            tmpCoordinates, //coordinate
                            null, //description
                            `{\"properties\":${JSON.stringify(properties)},\"options\":${JSON.stringify(options)}}`//strJson
                        ));
                    }
                    break;
            }


        });
    }
    // ДЛЯ ПРИВЯЗКИ К МОДАЛЬНОМУ ОКНУ
    // создание нового информационного слоя
    createLayer(name, type) {
        if (!this._mapLayerGeoObjectManager.booleanExistenceCheck(name)) {
            this._mapLayerGeoObjectManager.addNewLayer(new LayerDTO(null, type, name, null, null, new Array));
            this._editInformationLayersControl.addItem(name, type);
            this._legendMap.addItem(name, type);
        } else {
            // TODO ПЕРЕПИСАТЬ НА МОДАЛЬНОЕ ОКНО
            alert(`Слой ${name} существует`);
        }
    }
    // создание нового грузоперевозочного слоя
    createTruckingIndustryLayer(name, type) {
        if (!this._mapLayerGeoObjectManager.booleanExistenceCheck(name)) {
            this._mapLayerGeoObjectManager.addNewLayer(new LayerDTO(null, type, name, null, null, new Array));
            this._editTruckingIndustryLayerControl.addItem(name, type);
            this._legendMap.addItem(name, type);
        } else {
            // TODO ПЕРЕПИСАТЬ НА МОДАЛЬНОЕ ОКНО
            alert(`Слой ${name} существует`);
        }
    }

}