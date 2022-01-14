class MapTruckingIndustryManager extends MapLayerGeoObjectManager {
    constructor(map, layerStorage) {
        super(map, layerStorage);
        //контекстное меню товаров
        this._menuGoods = document.querySelector('#menuGoods');
        //контекстное меню грузоперевозчика
        this._menuTruck = document.querySelector('#menuTruck');
        //контекстное меню пункта доставки товара
        this._menuDeliveryPoint = document.querySelector('#menuDeliveryPoint');
        //имя типа геообъекта в балуне на русском
        this._typeNameInnerBalloon;
        //параметры для геообъекта грузоперевозок
        this._paramsTruckingIndustry;

    }

    _addProperties(obj, convertedDto) {
        //применяем
        this._paramsTruckingIndustry = convertedDto.params.trucking_industry;
        //в зависимости от типа выбираем нужное окно
        if (this._paramsTruckingIndustry.type == 'deliveryPoint') {
            this._typeNameInnerBalloon = "Пункт доставки";
        } else if (this._paramsTruckingIndustry.type == 'truck') {
            this._typeNameInnerBalloon = "Грузоперевозчик";
        } else if (this._paramsTruckingIndustry.type == 'goods') {
            this._typeNameInnerBalloon = "Товар/Груз";
        } else throw Error(`Такого типа геообъектов:\"${this._paramsTruckingIndustry}\" для грузоперевозок, не существует`);
        let nameGeoObj; //имя геообъекта
        //заголовком будет индекс геообъекта в его слое.  
        if ([null, ''].includes(convertedDto.name)) {
            nameGeoObj = obj.indexId;
            obj.properties.set('iconCaption', nameGeoObj);
            this._layerStorage.get(obj.layerName).arrGeoObjects[obj.indexId].name = nameGeoObj; //в том числе и имя в geoObjDTO
        } else {
            nameGeoObj = convertedDto.name;
            obj.properties.set('iconCaption', nameGeoObj);
        }
        let hintDescription; //описание подсказка
        // меняем подсказку объекта
        if ([null, ''].includes(convertedDto.description)) {
            hintDescription = this._typeNameInnerBalloon;
            obj.properties.set('hintContent', hintDescription);
            this._layerStorage.get(obj.layerName).arrGeoObjects[obj.indexId].description = hintDescription; //в том числе и имя в geoObjDTO
        } else {
            hintDescription = convertedDto.description;
            obj.properties.set('hintContent', convertedDto.description);
        }
        //балун геообъекта
        let balloon = `<li>Название: ${hintDescription} №${nameGeoObj}</li>`
                    + `<li>Координаты: ${obj.geometry.getCoordinates()}</li>`;
        //меняем балун объекта
        obj.properties.set('balloonContent', balloon);
    }
    // добавляем события к геообектам
    _addEvent(obj) {
        //добавляем события при нажатии ПКМ
        obj.events.add('contextmenu', e => {
            //ссылка контекстного меню
            let menu;
            //тип открывающегося меню который ложиться в URL
            let typeMenu;
            //в зависимости от типа выбираем нужное окно
            if (this._paramsTruckingIndustry.type == 'deliveryPoint') {
                typeMenu = '#menuDeliveryPoint';
                menu = this._menuDeliveryPoint;
            } else if (this._paramsTruckingIndustry.type == 'truck') {
                typeMenu = '#menuTruck';
                menu = this._menuTruck;
            } else if (this._paramsTruckingIndustry.type == 'goods') {
                typeMenu = '#menuGoods';
                menu = this._menuGoods;
            } else throw Error(`Такого типа геообъектов:\"${this._paramsTruckingIndustry}\" для грузоперевозок, не существует`);
            //меняем координаты окна перед появлением
            menu.style.left = e.get('pagePixels')[0] + 'px';
            menu.style.top = e.get('pagePixels')[1] + 'px';
            //открываем окно "настройки геообъекта"
            location.hash = typeMenu;
            //добавляем обработку события для окна добавления опции к объекту
            this._menu.addEventListener('submit',  e => {
                //отключаем перезагрузку страницы при нажатии на submit
                e.preventDefault();
                if (this._paramsTruckingIndustry.type == 'truck') {
                    //получаем грузоподъёмность грузоперевозчика
                    this._paramsTruckingIndustry.carrying = this._menu.carrying.value;
                } else if (this._paramsTruckingIndustry.type == 'goods') {
                    //получаем вес груза
                    this._paramsTruckingIndustry.weight = this._menu.weight.value;
                    //получаем имя пункта доставки
                    this._paramsTruckingIndustry.deliveryPoint = this._menu.deliveryPoint.value;
                }

                let objJson = JSON.parse(this._layerStorage.get(obj.layerName).arrGeoObjects[obj.indexId].strJson);

                objJson.trucking_industry = this._paramsTruckingIndustry;

                this._layerStorage.get(obj.layerName).arrGeoObjects[obj.indexId].strJson = JSON.stringify(objJson);
                //очищаем все поля ввода после применить
                this._menu.reset();
                //закрываем после ввода
                location.hash = '#close';
            }, { once: true });//TODO возможно он здесь и не нужен теперь

        });
    }
}