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
        this._layerStorageMap = new Map();
        //база данных для сохранения
        this._layerDB = new LayerCRUD("/client/1/layer_group/1/layers");
        //кнопки выбора геообъектов;
        this._buttonGeoObj = new ManagerButtonsGeoObj();
        //буфер для хранения контролируемый кнопками выбора геообъекта
        this._bufferCoordinates = this._buttonGeoObj.getBuffer();
        //менеджер слоёв
        this._layerManager = new LayerManager(this._map, this._layerStorageCRUD);
        //лист бокс для создания и редактирования информационного слоя
        this._editInformationLayersControl = new EditInformationLayersControl(this._buttonGeoObj);
        //лист бокс для создания и редактирования грузоперевозочного слоя
        this._editTruckingIndustryLayersControl = new EditTruckingIndustryLayersControl(this._buttonGeoObj);
        //лист бокс для выбора режима карты
        this._mapModes = new MapModesControl(this._map, this._editInformationLayersControl, this._editTruckingIndustryLayersControl);
        //легенда карты
        this._legendMap = new MapLegendControl(this._layerManager);

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
        //обработка событий мыши
        this._map.events.add(['mousedown', 'mouseup'], e => {
            let eType = e.get('type');
            switch (this._buttonGeoObj.getActiveTypeGeoObj()) {
                case "point":
                    if (eType == 'mouseup') {
                        let layer = this._layerManager.getLayer(this._editInformationLayersControl.getSelectedLayer());//TODO добавить сюда editTruckingIndustryLayerControl
                        layer.add('point', e.get('coords'));
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
                        this._bufferCoordinates.forEach(item => tmpCoordinates.push(item));
                        let layer = this._layerManager.getLayer(this._editInformationLayersControl.getSelectedLayer());//TODO добавить сюда editTruckingIndustryLayerControl
                        layer.add('arrow', tmpCoordinates, {}, options);
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
                        this._bufferCoordinates.forEach(item => tmpCoordinates.push(item));
                        let layer = this._layerManager.getLayer(this._editInformationLayersControl.getSelectedLayer());//TODO добавить сюда editTruckingIndustryLayerControl
                        layer.add('line', tmpCoordinates, {}, options);
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
                        this._bufferCoordinates.forEach(item => tmpCoordinates.push(item));
                        let layer = this._layerManager.getLayer(this._editInformationLayersControl.getSelectedLayer());//TODO добавить сюда editTruckingIndustryLayerControl
                        layer.add('line', tmpCoordinates, {}, options); 
                    }
                    break;
            }


        });
    }
    // для привязки к модальному окну (создание нового информационного слоя)
    createLayer(name, type) {
        if (this._layerManager.add(name, type)) {
            this._editInformationLayersControl.addItem(name, type);
            this._legendMap.addItem(name, type);
        } else {
            // TODO ПЕРЕПИСАТЬ НА МОДАЛЬНОЕ ОКНО
            alert(`Слой ${name} существует`);
        }
    }
    // для привязки к модальному окну (создание нового грузоперевозочного слоя)
    createTruckingIndustryLayer(name, type) {
        if (this._layerManager.add(name, type)) {
            this._editTruckingIndustryLayerControl.addItem(name, type);
            this._legendMap.addItem(name, type);
        } else {
            // TODO ПЕРЕПИСАТЬ НА МОДАЛЬНОЕ ОКНО
            alert(`Слой ${name} существует`);
        }
    }
    //test
    output() {
        let it = this._map.geoObjects.getIterator();
        let obj;
        while ((obj = it.getNext()) != it.STOP_ITERATION) {
            console.log(obj);
        }
    }

    //это для тестирования, смысла пока не имеет
    _sendToServer() {
        let data = {
            name: 'nn',
            val: 'vv'
        }

        //отправление данных на сервер
        fetch('test.php', { // файл-обработчик
            method: 'POST',
            headers: {
                'Content-Type': 'application/json', // отправляемые данные
            },
            body: JSON.stringify(data)
        })
            .then(response => alert('Сообщение отправлено'))
            .catch(error => console.error(error))
    }

}