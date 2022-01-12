//для того чтобы модуль стрелки был перед использованием инициализирован
let moduleArrow = ymaps.modules.require(['geoObject.Arrow']);

class MapLayerGeoObjectManager {
        constructor(map) {
        //карта с которой взаимодействуем
        this._map = map;
        //хранимый слой
        this._layerDTO = null;
    }
    // добаление геообъекта на слой DTO и на карту
    addGeoObject(geoObjectDTO) {
        if (['point', 'line', 'arrow', 'broken_line'].includes(geoObjectDTO.type)) {
            // добавляем геооъект в слой во внутренний массив геообъектов
            this._layerDTO.arrGeoObjects.push(geoObjectDTO);
            let mapGeoObj = this._GeoObjDTOConvertToMapGeoObj(geoObjectDTO);
            this._addToMap(mapGeoObj.type, mapGeoObj.coordinate, mapGeoObj.properties, mapGeoObj.options);
        } else console.error('неизвестный тип геообъекта');
    }
    //включаем отображение на карте
    on() {
        //для каждого элемента массива вызываем добавление на карту
        this._layerDTO.arrGeoObjects.forEach(geoObjectDTO => {
            let mapGeoObj = this._GeoObjDTOConvertToMapGeoObj(geoObjectDTO);
            this._addToMap(mapGeoObj.type, mapGeoObj.coordinate, mapGeoObj.properties, mapGeoObj.options);
        });
    }

    //отключаем отображение на карте
    off() {
        let it = this._map.geoObjects.getIterator();//возвращаем итератор объектов
        let obj; //объект временного хранения
        let rmList = new Array(); //собираем удаляемые элементы
        while ((obj = it.getNext()) != it.STOP_ITERATION) {
            if (obj.layerName == this._layerDTO.name) {
                rmList.push(obj);
            }
        }
        //удалаяем с карты собранные элементы
        while (rmList.length != 0) {
            this._map.geoObjects.remove(rmList.pop());
        }
    }
    // применить DTO слоя к карте
    applyLayerDTOToMap() {
        this.off();
        this.on();
    }
    // вернуть DTO слоя
    getLayer() {
        if (this._layerDTO == null) {
            throw Error("NullPointerException");
        } else {
            return this._layerDTO; 
        }
    }
    // установить DTO слоя
    setLayer(layerDTO) {
        if (layerDTO == null) {
            throw Error("NullPointerException");
        } else {
            this._layerDTO = layerDTO; 
        }
    }
    // добавляем события к геообектам
    _addEvent(obj) {
        obj.events.add('contextmenu', e => {
            //тип открывающегося меню
            let typeMenu;
            //в зависимости от типа выбираем нужное окно
            if (obj.geometry.getType() == 'Point') {
                typeMenu = 'menuPoint';
            } else {
                typeMenu = 'menuLine';
            }
            //возвращаем в зависимости от типа
            let menu = document.querySelector('#' + typeMenu);
            //меняем координаты окна перед появлением
            menu.style.left = e.get('pagePixels')[0] + 'px';
            menu.style.top = e.get('pagePixels')[1] + 'px';
            //открываем окно "настройки геообъекта"
            location.hash = typeMenu;
            //добавляем обработку события для окна добавления опции к объекту
            menu.addEventListener('submit', function (e) {
                //отключаем перезагрузку страницы при нажатии на submit
                e.preventDefault();
                //у окна линии нет названия объекта
                if (obj.geometry.getType() == 'Point') {
                    //меняем заголовок объекта
                    obj.properties.set('iconCaption', this.iconText.value);
                    //TODO придумать что нибудь в тех местах где подписано "так же в хранилище", так как он изменяет вложенный слой, а должен менять тот к которому принадлежит.
                    this._layerDTO.arrGeoObjects[obj.indexId].properties['iconCaption'] = this.iconText.value;//также в хранилище
                }
                //меняем подсказку объекта
                obj.properties.set('hintContent', this.hintText.value);
                this._layerDTO.arrGeoObjects[obj.indexId].properties['hintContent'] = this.hintText.value;//также в хранилище
                //меняем балун обЪекта
                obj.properties.set('balloonContent', this.balloonText.value);
                this._layerDTO.arrGeoObjects[obj.indexId].properties['balloonContent'] = this.balloonText.value;//также в хранилище
                //закрываем после ввода
                location.hash = '#close';
            }, { once: true });//TODO возможно он здесь и не нужен теперь

        });
    }
    //конвертирует с dto в mapGeoObj
    _GeoObjDTOConvertToMapGeoObj(geoObjectDTO) {
        let jsonObj = JSON.parse(geoObjectDTO.strJson); //преобразуем строковый JSON в объект JSON
        //добавляем параметры
        jsonObj.iconCaption = geoObjectDTO.name;
        jsonObj.hintContent = geoObjectDTO.description;

        let arrOfCoordinates = new Array;
        //в зависимости от типа геобъекта преобразуем массив геобъектов в массив массивов или массив
        if (geoObjectDTO.type == "point") {
            arrOfCoordinates.push(geoObjectDTO.coordinate[0].latitude);
            arrOfCoordinates.push(geoObjectDTO.coordinate[0].longitude);
        } else {
            geoObjectDTO.coordinate.forEach(coordinate => {
                arrOfCoordinates.push([coordinate.latitude, coordinate.longitude]);
            });   
        }

        return {
            type: geoObjectDTO.type,
            coordinate: arrOfCoordinates,
            properties: jsonObj.properties,
            options: jsonObj.options
        };
    }
    // добавляем на карту вкладываемый геообъект
    _addToMap(typeGeoObj, coordinates, properties, options){
        if (typeGeoObj == 'arrow') {//только так можно удобно обработать модуль стрелки
            //при помощи модуля стрелки создаём её на карте
            moduleArrow.spread(Arrow => {
                let obj = new Arrow(coordinates, properties, options);
                //добавляем метод к геообъекту при помощи которого мы сможем его отличать от других слоёв
                obj.layerName = this._layerDTO.name;
                //присваиваем id по индексу в массиве !!!!!!!!!в последующем если удалять через delete arr[5], то length не поменяется
                obj.indexId = this._layerDTO.arrGeoObjects.length - 1;
                //добавляем доп характеристики объекту
                this._addEvent(obj);
                //добавляем на карту
                this._map.geoObjects.add(obj);
                
            });

        } else {

            let obj;
            if (typeGeoObj == 'point') {
                obj = new ymaps.Placemark(coordinates, properties, options);
            } else if (typeGeoObj == 'line') {
                obj = new ymaps.Polyline(coordinates, properties, options);
            } else if (typeGeoObj == 'broken_line') {
                obj = new ymaps.Polyline(coordinates, properties, options);
            }
            //добавляем метод к геообъекту при помощи которого мы сможем его отличать от других слоёв
            obj.layerName = this._layerDTO.name;
            //присваиваем id по индексу в массиве !!!!!!!!!в последующем если удалять через delete arr[5], то length не поменяется
            obj.indexId = this._layerDTO.arrGeoObjects.length - 1;
            //добавляем доп характеристики объекту
            this._addEvent(obj);
            //добавляем на карту
            this._map.geoObjects.add(obj)
        }
    }
    //TODO реализовать методы, удаления геообъектов. А только есть методы добавления.
}