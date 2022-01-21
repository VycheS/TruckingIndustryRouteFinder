class MapTruckingIndustryManager extends MapLayerGeoObjectManager {
    constructor(map, layerStorage) {
        super(map, layerStorage);
        //контекстное меню товаров
        this._menuGoods = document.querySelector('#menuGoods');
        //контекстное меню грузоперевозчика
        this._menuTruck = document.querySelector('#menuTruck');
        //контекстное меню пункта доставки товара
        this._menuDeliveryPoint = document.querySelector('#menuDeliveryPoint');

    }

    _addProperties(obj, convertedDto) {
        //добавляем геообъекту дополнительное грузоперевозочное св-во через который в последующем будем работать
        obj.trucking_industry = convertedDto.params.trucking_industry;
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
            //в зависимости от типа выбираем нужное окно
            if (obj.trucking_industry.type == 'deliveryPoint') {
                hintDescription = "Пункт доставки";
            } else if (obj.trucking_industry.type == 'truck') {
                hintDescription = "Грузоперевозчик";
            } else if (obj.trucking_industry.type == 'goods') {
                hintDescription = "Товар/Груз";
            } else if (obj.trucking_industry.type == 'route') {
                hintDescription = "Маршрут";
            } else throw Error(`Такого типа геообъектов:\"${obj.trucking_industry.type}\" для грузоперевозок, не существует`);
            obj.properties.set('hintContent', hintDescription);
            this._layerStorage.get(obj.layerName).arrGeoObjects[obj.indexId].description = hintDescription; //в том числе и имя в geoObjDTO
        } else {
            hintDescription = convertedDto.description;
            obj.properties.set('hintContent', convertedDto.description);
        }
        //балун геообъекта
        let newBalloon = `<li>Название: ${hintDescription} №${nameGeoObj}</li>`
                    + `<li>Координаты: ${obj.geometry.getCoordinates()}</li>`;
        //если нет балуна основы то создаём её
        if ([undefined, null, ''].includes(obj.trucking_industry.balloon)) {
            //меняем балун объекта
            obj.properties.set('balloonContent', newBalloon);
            //создаём объект balloon для прицепа
            obj.trucking_industry.balloon = {};
            //прицепляем балун основа, для последующей обработки
            obj.trucking_industry.balloon.basis = newBalloon;
        } else if ([undefined, null, ''].includes(obj.trucking_industry.balloon.full)) { //если балун основа есть то её применяем к балуну объекта
            obj.properties.set('balloonContent', obj.trucking_industry.balloon.basis);
        } else  obj.properties.set('balloonContent', obj.trucking_industry.balloon.full) //если есть полный балун то её применяем к балуну объекта
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
            if (obj.trucking_industry.type == 'deliveryPoint') {
                typeMenu = '#menuDeliveryPoint';
                menu = this._menuDeliveryPoint;
            } else if (obj.trucking_industry.type == 'truck') {
                typeMenu = '#menuTruck';
                menu = this._menuTruck;
            } else if (obj.trucking_industry.type == 'goods') {
                typeMenu = '#menuGoods';
                menu = this._menuGoods;
            } else throw Error(`Такого типа геообъектов:\"${obj.trucking_industry.type}\" для грузоперевозок, не существует`);
            //меняем координаты окна перед появлением
            menu.style.left = e.get('pagePixels')[0] + 'px';
            menu.style.top = e.get('pagePixels')[1] + 'px';
            //открываем окно "настройки геообъекта"
            location.hash = typeMenu;
            //добавляем обработку события для окна добавления опции к объекту
            menu.addEventListener('submit',  e => {
                //отключаем перезагрузку страницы при нажатии на submit
                e.preventDefault();
                if (obj.trucking_industry.type == 'truck') {
                    //получаем грузоподъёмность грузоперевозчика
                    obj.trucking_industry.carrying = menu.carrying.value;
                    //создаём полный балун
                    obj.trucking_industry.balloon.full = obj.trucking_industry.balloon.basis + `<li>Грузоподъёмность в кг: ${menu.carrying.value}</li>`;
                    //меняем балун объекта
                    obj.properties.set('balloonContent', obj.trucking_industry.balloon.full);
                } else if (obj.trucking_industry.type == 'goods') {
                    //получаем вес груза
                    obj.trucking_industry.weight = menu.weight.value;
                    //получаем имя пункта доставки
                    obj.trucking_industry.deliveryPoint = menu.deliveryPoint.value;
                    //создаём полный балун
                    obj.trucking_industry.balloon.full = obj.trucking_industry.balloon.basis + `<li>Вес в кг: ${menu.weight.value}</li>` + `<li>Пункт доставки: ${menu.deliveryPoint.value}</li>`;
                    //меняем балун объекта
                    obj.properties.set('balloonContent', obj.trucking_industry.balloon.full);
                }

                let objJson = JSON.parse(this._layerStorage.get(obj.layerName).arrGeoObjects[obj.indexId].strJson);
                
                objJson.trucking_industry = obj.trucking_industry;
                //применяем к strJson все изменения
                this._layerStorage.get(obj.layerName).arrGeoObjects[obj.indexId].strJson = JSON.stringify(objJson);
                //очищаем все поля ввода после применить
                menu.reset();
                //закрываем после ввода
                location.hash = '#close';
            }, { once: true });//TODO возможно он здесь и не нужен теперь

        });
    }
}