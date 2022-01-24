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
        //кнопки выбора геообъектов;
        this._buttonGeoObj = new ManagerButtonsGeoObj();
        //буфер для хранения контролируемый кнопками выбора геообъекта
        this._bufferCoordinates = this._buttonGeoObj.getBuffer();
        //хранилище слоёв типа данных коллекции Map
        this._layerStorage = new Map();
        this._layerStorage.set('information', new Map()); //хранилище информационных слоёв
        this._layerStorage.set('truckingIndustry', new Map()); //хранилище грузоперевозочных слоёв
        //менеджер слоёв и их геообъектов
        this._mapLayerGeoObjectManager = new MapLayerGeoObjectManager(this._map, this._layerStorage.get('information'));
        //менеджер грузоперевозочных слоёв унаследованный от менеджера слоёв
        this._mapTruckingIndustryManager = new MapTruckingIndustryManager(this._map, this._layerStorage.get('truckingIndustry'));
        //лист бокс для создания и редактирования информационного слоя
        this._editInformationLayersControl = new EditInformationLayersControl(this._buttonGeoObj);
        //лист бокс для создания и редактирования грузоперевозочного слоя
        this._editTruckingIndustryLayersControl = new EditTruckingIndustryLayersControl(this._buttonGeoObj);
        //лист бокс для выбора режима карты
        this._mapModes = new MapModesControl(this._map, this._editInformationLayersControl, this._editTruckingIndustryLayersControl);
        //легенда карты
        this._legendMap = new MapLegendControl(this._mapLayerGeoObjectManager, this._mapTruckingIndustryManager);

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

    async clearRoutes(clientId = 1, layerGroupId = 4) {
        const layerCrud = new LayerCRUD(clientId,layerGroupId);
        const responceAfterReadAll = layerCrud.readAll();
        try {
            if ((await responceAfterReadAll).ok) {
                (await responceAfterReadAll).json().then(async json => {
                    const arr = new Array();
                    json.forEach(item => {
                        if (item.typeObj == 'line') {
                            arr.push(item.id);
                        }
                    });
                    arr.forEach(async element => {
                        const responceAfterDelete = layerCrud.delete(element);
                        if ((await responceAfterDelete).ok) {
                            console.log("deleted route");
                        }
                    });
                });
                return true;
            } 
        } catch (error) {
            console.log(error);
        }
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
                            let objTruckingIndustry = {type: this._editTruckingIndustryLayersControl.getSelectedTypeGeoObj()}
                            this._mapTruckingIndustryManager.addGeoObject(this._editTruckingIndustryLayersControl.getSelectedLayer(), new GeoObjectDTO(
                                null, //id
                                null, //name
                                'point', //type
                                null, //forwardArrowDirection
                                [new CoordinateDTO(e.get('coords')[0], e.get('coords')[1])], //coordinate
                                null, //description
                                `{\"properties\":${JSON.stringify(properties)},\"options\":${JSON.stringify(options)}, \"trucking_industry\":${JSON.stringify(objTruckingIndustry)}}`//strJson
                            ));  
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

    applyAllGeoObjectsOnTheMap(layerManagerType = "truckingIndustry") {
        let layers;
        if ('information' === layerManagerType) {
            layers = {
                storage: this._layerStorage.get("information"),
                mapManager: this._mapLayerGeoObjectManager,
                // managerControl: this._editInformationLayersControl
            }
        } else if (('truckingIndustry' === layerManagerType)) {
            layers = {
                storage: this._layerStorage.get("truckingIndustry"),
                mapManager: this._mapTruckingIndustryManager,
                // managerControl: this._editTruckingIndustryLayersControl
            }
        } else throw Error(`Такого типа хранилища слоёв как:${layerManagerType}, не существует.`);
        layers.storage.forEach(layer => {
            layers.mapManager.off(layer.name);
            layers.mapManager.on(layer.name);
            // layers.managerControl.deselectAll();
        });
    }

    async findRoute(clientId = 1, layerGroupId = 4) {
        const findRoute = new FindRouteCRUD(clientId,layerGroupId);
        findRoute.sendCommandToServer()
            .then(async response => {
                if ((await response).ok) {
                    console.log("Поиск завершён")
                } else console.log(response.status);
            }).catch(async e => console.log(e));
    }

    async generateGeoObject(clientId = 1, layerGroupId = 4) {
        const rg = new RandomlyGenerateCRUD(clientId,layerGroupId);
        rg.sendCommandToServer()
            .then(async response => {
                if ((await response).ok) {
                    console.log("Геообъекты сгенерированы, сохранены в базу.")
                } else console.log(response.status);
            }).catch(async e => console.log(e));
    }

    async getLayersFromTheServer(clientId = 1, layerGroupId = 4, layerManagerType = 'truckingIndustry') {
        const layerCrud = new LayerCRUD(clientId,layerGroupId);
        layerCrud.readAll()
            .then(async response => {
                if (response.ok) {
                    (await response).json()
                        .then(async jsonLayers => {
                            jsonLayers.forEach(async layer => {
                                const geoObjectCrud = new GeoObjectCRUD(clientId,layerGroupId, layer.id);
                                const arrGeoObjects = new Array();
                                geoObjectCrud.readAll()
                                    .then(async response => {
                                        if (response.ok){
                                            (await response).json()
                                                .then(async jsonGeoObjects => {
                                                    jsonGeoObjects.forEach(async geoObject => {
                                                        const strJson = (["", null].includes(geoObject.strJson)) ? JSON.stringify({options:{geodesic: true,strokeWidth: 5,opacity: 0.5}, properties:{}}) : geoObject.strJson;
                                                        const arrCoordinatesDTO = new Array();
                                                        if (geoObject.coordinates != null && Array.isArray(geoObject.coordinates)) {
                                                            geoObject.coordinates.forEach(coord => {
                                                                arrCoordinatesDTO.push(new CoordinateDTO(coord.latitude, coord.longitude));
                                                            });    
                                                        }
                                                        arrGeoObjects.push(new GeoObjectDTO(geoObject.id
                                                            , geoObject.name
                                                            , geoObject.type
                                                            , geoObject.forwardArrowDirection
                                                            , arrCoordinatesDTO
                                                            , geoObject.description
                                                            , strJson
                                                            ));
                                                    });
                                                })
                                        } else console.log(response.status)
                                    }).catch(e => console.log(e))
                                    .then(async () => {
                                        const layerDTO = new LayerDTO(layer.id, layer.typeObj, layer.name, layer.description, layer.strJson, arrGeoObjects);
                                        if ('information' === layerManagerType) {
                                            this.createLayer(layer.name
                                                , layer.typeObj
                                                , layerDTO
                                            );
                                        } else if ('truckingIndustry' === layerManagerType) {
                                            this.createTruckingIndustryLayer(layer.name
                                                , layer.typeObj
                                                , layerDTO
                                            );
                                        } else throw Error(`Такого типа менеджера как:${layerManagerType}, не существует`);

                                    }).then(async () => {
                                        if ('information' === layerManagerType) {
                                            return {
                                                layerManager: this._mapLayerGeoObjectManager,
                                                layerManagerControl: this._editInformationLayersControl
                                            }
                                        } else if (('truckingIndustry' === layerManagerType)) {
                                            return {
                                                layerManager: this._mapTruckingIndustryManager,
                                                layerManagerControl: this._editTruckingIndustryLayersControl
                                            }
                                        } else throw Error(`Такого типа хранилища слоёв как:${layerManagerType}, не существует.`);
                                    }).then(async (action) => {
                                        // action.layerManager.applyLayerDTOToMap(layer.name); //TODO почему то не работает!!!
                                        action.layerManagerControl.deselectAll();
                                    });
                            });
                        });
                } else console.log(response.status);
            }).catch(e => console.log(e));
    }

    async clearLayersOnTheServer(clientId = 1, layerGroupId = 4) {
        const layerCrud = new LayerCRUD(clientId,layerGroupId);
        const responceAfterReadAll = layerCrud.readAll();
        try {
            if ((await responceAfterReadAll).ok) {
                (await responceAfterReadAll).json().then(async json => {
                    const arr = new Array();
                    json.forEach(item => {
                        arr.push(item.id);
                    });
                    arr.forEach(async element => {
                        const responceAfterDelete = layerCrud.delete(element);
                        if ((await responceAfterDelete).ok) {
                            console.log("succes");
                        }
                    });
                });
                return true;
            } 
        } catch (error) {
            console.log(error);
        }
    }

    async sendingLayersToServer(clientId = 1, layerGroupId = 4, layerManagerType = 'truckingIndustry') {
        const layerCrud = new LayerCRUD(clientId,layerGroupId);
        let layerManager;
        if (layerManagerType === 'information') {
            layerManager = this._layerStorage.get('information');
        } else if (layerManagerType === 'truckingIndustry') {
            layerManager = this._layerStorage.get('truckingIndustry');
        } else throw Error(`Такого типа менеджера как:${layerManagerType}, не существует`);

        layerManager.forEach(async layer => {
            //копия слоя без массива геообъекта и переименование его св-ва с type на typeObj
            const copyLayerWithoutArrGeoObj = {
                typeObj: layer.type,
                name: layer.name,
                //обработка  null c последующим вкладыванием своего значения
                description: (layer.description == null) ? layer.type : layer.description,
                strJson: (layer.strJson == null) ? JSON.stringify({properties: {}, options: {}}) : layer.strJson
            }
            layerCrud.create(copyLayerWithoutArrGeoObj)
                .then(async response => {
                    if ((await response).ok) {
                        response.json()
                            .then(async layerUuid => {
                                const layerCrud = new GeoObjectCRUD(clientId, layerGroupId, layerUuid);
                                let geoObjIndex = 0 //нужен для того чтобы присвоить имя если его нет
                                layer.arrGeoObjects.forEach(async geoObject => {
                                    geoObjIndex += 1;
                                    //если имени нет то мы его присваиваем в том числе и для описания
                                    geoObject.name = (geoObject.name == null) ? layer.name + geoObjIndex : geoObject.name;
                                    geoObject.description = (geoObject.description == null) ? layer.name + geoObjIndex : geoObject.description;

                                    geoObject.coordinates = geoObject.coordinate;
                                    layerCrud.create(geoObject)
                                        .then(async response => {
                                            if ((await response).ok) {
                                                console.log('succes');
                                            } else (await response).text().then(txt => console.log(txt));
                                        })
                                        .catch(e => console.error(e))
                                });
                            });
                    // } else console.log((await response).status);
                    } else (await response).text().then(txt => console.log(txt));
                })

                .catch(e => console.error(e))
            
        });//TODO доделать!
    }


    outputDtoToConsole (){
        console.log(this._layerStorage);
    }
    // ДЛЯ ПРИВЯЗКИ К МОДАЛЬНОМУ ОКНУ
    // создание нового информационного слоя
    createLayer(name, type, layerDTO = null) {
        if (layerDTO == null) {
            if (!this._mapLayerGeoObjectManager.booleanExistenceCheck(name)) {
                this._mapLayerGeoObjectManager.addNewLayer(new LayerDTO(
                    null, //id
                    type, //type
                    name, //name
                    null, //description
                    null, //strJson
                    new Array
                ));
                this._editInformationLayersControl.addItem(name, type);
                this._legendMap.addItem(name, type);
            } else {
                // TODO ПЕРЕПИСАТЬ НА МОДАЛЬНОЕ ОКНО
                alert(`Слой ${name} существует`);
            }
        } else {
            if (!this._mapLayerGeoObjectManager.booleanExistenceCheck(layerDTO.name)) {
                this._mapLayerGeoObjectManager.addNewLayer(layerDTO);
                this._editInformationLayersControl.addItem(name, type);
                this._legendMap.addItem(name, type);
            }
        }
    }
    // создание нового грузоперевозочного слоя
    createTruckingIndustryLayer(name, type, layerDTO = null) {
        if (layerDTO == null) {
            if (!this._mapTruckingIndustryManager.booleanExistenceCheck(name)) {
                let trucking_industry = {
                    type: type
                }; 
                //не забываем конвертировать тип слоя, чтобы он конкретно работал с менеджером слоёв
                this._mapTruckingIndustryManager.addNewLayer(new LayerDTO(
                    null, //id
                    this._editTruckingIndustryLayersControl.getConvertToStandartType(type), //type
                    name, //name
                    null, //description
                    `{\"trucking_industry\":${JSON.stringify(trucking_industry)}}`, //strJson
                    new Array
                ));
                this._editTruckingIndustryLayersControl.addItem(name, type);
                this._legendMap.addItem(name, type);
            } else {
                // TODO ПЕРЕПИСАТЬ НА МОДАЛЬНОЕ ОКНО
                alert(`Слой ${name} существует`);
            }
            
        } else {
            if (!this._mapTruckingIndustryManager.booleanExistenceCheck(layerDTO.name)) {
                let newType;
                if (['point', 'line'].includes(type)) {
                    let objJson = JSON.parse(layerDTO.strJson);
                    newType = objJson.trucking_industry.type;
                } else {
                    newType = type;
                }
                this._mapTruckingIndustryManager.addNewLayer(layerDTO);
                this._editTruckingIndustryLayersControl.addItem(name, newType);
                this._legendMap.addItem(name, newType);
            }
        }
    }

}