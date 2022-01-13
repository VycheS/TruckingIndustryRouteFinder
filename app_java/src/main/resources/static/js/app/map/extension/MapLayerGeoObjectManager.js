//для того чтобы модуль стрелки был перед использованием инициализирован
let moduleArrow = ymaps.modules.require(['geoObject.Arrow']);

class MapLayerGeoObjectManager {
        constructor(map, layerStorage) {
        //типы слоёв которые проходят проверку
        this._typesToCheck = ['point', 'line', 'arrow', 'broken_line'];
        //карта с которой взаимодействуем
        this._map = map;
        // коллекция типа Map. Хранит в себе все слои.
        this._layerStorage = layerStorage;
    }
    // добаление геообъекта на слой DTO и на карту
    addGeoObject(layerName, geoObjectDTO) {
        let checkedLayerName = this._layerExist(layerName);
        if (this._typesToCheck.includes(geoObjectDTO.type)) {
            // добавляем геооъект в слой во внутренний массив геообъектов
            this._layerStorage.get(checkedLayerName).arrGeoObjects.push(geoObjectDTO);
            let mapGeoObj = this._GeoObjDTOConvertToMapGeoObj(geoObjectDTO);
            this._addToMap(checkedLayerName, mapGeoObj.type, mapGeoObj.coordinate, mapGeoObj.properties, mapGeoObj.options);
        } else throw Error('неизвестный тип геообъекта');
    }
    addNewLayer(layerDTO) {
        let checkedLayerName = this._layerNotExist(layerDTO.name);
        if (this._typesToCheck.includes(layerDTO.type)) {
            // добавляем новый слой
            this._layerStorage.set(checkedLayerName, layerDTO);
        } else throw Error('неизвестный тип слоя');
    }
    //включаем отображение на карте
    on(layerName) {
        let checkedLayerName = this._layerExist(layerName);
        //для каждого элемента массива вызываем добавление на карту
        this._layerStorage.get(checkedLayerName).arrGeoObjects.forEach(geoObjectDTO => {
            let mapGeoObj = this._GeoObjDTOConvertToMapGeoObj(geoObjectDTO);
            this._addToMap(checkedLayerName, mapGeoObj.type, mapGeoObj.coordinate, mapGeoObj.properties, mapGeoObj.options);
        });
    }

    //отключаем отображение на карте
    off(layerName) {
        let checkedLayerName = this._layerExist(layerName);
        let it = this._map.geoObjects.getIterator();//возвращаем итератор объектов
        let obj; //объект временного хранения
        let rmList = new Array(); //собираем удаляемые элементы
        while ((obj = it.getNext()) != it.STOP_ITERATION) {
            if (obj.layerName == this._layerStorage.get(checkedLayerName).name) {
                rmList.push(obj);
            }
        }
        //удалаяем с карты собранные элементы
        while (rmList.length != 0) {
            this._map.geoObjects.remove(rmList.pop());
        }
    }
    // применить DTO слоя к карте
    applyLayerDTOToMap(layerName) {
        this.off(layerName);
        this.on(layerName);
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
            menu.addEventListener('submit',  e => {
                //забираем временно из объекта хранилилища, параметры геообъекта карты преобразовавывая strJson в objJson
                let objJson = JSON.parse(this._layerStorage.get(obj.layerName).arrGeoObjects[obj.indexId].strJson);
                //отключаем перезагрузку страницы при нажатии на submit
                e.preventDefault();
                //у окна линии нет названия объекта
                if (obj.geometry.getType() == 'Point') {
                    //меняем заголовок объекта
                    obj.properties.set('iconCaption', menu.iconText.value);
                    this._layerStorage.get(obj.layerName).arrGeoObjects[obj.indexId].name = menu.iconText.value; //в том числе и имя в geoObjDTO
                    objJson.properties.iconCaption = menu.iconText.value;
                }
                //меняем подсказку объекта
                obj.properties.set('hintContent', menu.hintText.value);
                objJson.properties.hintContent = menu.hintText.value;
                //меняем балун объекта
                obj.properties.set('balloonContent', menu.balloonText.value);
                objJson.properties.balloonContent = menu.balloonText.value;
                //возвращаем обратно в объект в хранилилище, изменения параметров геообъекта карты преобразовывая objJson обратно в strJson
                this._layerStorage.get(obj.layerName).arrGeoObjects[obj.indexId].strJson = JSON.stringify(objJson);
                //очищаем все поля ввода после применить
                menu.reset();
                //закрываем после ввода
                location.hash = '#close';
            }, { once: true });//TODO возможно он здесь и не нужен теперь

        });
    }
    //конвертирует с dto в mapGeoObj
    _GeoObjDTOConvertToMapGeoObj(geoObjectDTO) {
        let jsonObj = JSON.parse(geoObjectDTO.strJson); //преобразуем строковый JSON в объект JSON
        //добавляем параметры
        jsonObj.properties.iconCaption = geoObjectDTO.name;
        jsonObj.properties.hintContent = geoObjectDTO.description;
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
    _addToMap(layerName, typeGeoObj, coordinates, properties, options){
        if (typeGeoObj == 'arrow') {//только так можно удобно обработать модуль стрелки
            //при помощи модуля стрелки создаём её на карте
            moduleArrow.spread(Arrow => {
                let obj = new Arrow(coordinates, properties, options);
                //добавляем метод к геообъекту при помощи которого мы сможем его отличать от других слоёв
                obj.layerName = layerName;
                //присваиваем id по индексу в массиве !!!!!!!!!в последующем если удалять через delete arr[5], то length не поменяется
                obj.indexId = this._layerStorage.get(layerName).arrGeoObjects.length - 1;
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
            obj.layerName = layerName;
            //присваиваем id по индексу в массиве !!!!!!!!!в последующем если удалять через delete arr[5], то length не поменяется
            obj.indexId = this._layerStorage.get(layerName).arrGeoObjects.length - 1;
            //добавляем доп характеристики объекту
            this._addEvent(obj);
            //добавляем на карту
            this._map.geoObjects.add(obj)
        }
    }
    // проверка на существование слоя по имени
    _layerExist(layerName) {
        if (this.booleanExistenceCheck(layerName)) {
            return layerName;
        } else {
            throw Error(`Слоя с таким именем как:\"${layerName}\", не существует!!!`);
        }
    }
    // проверка на не существование слоя по имени
    _layerNotExist(layerName) {
        if (this.booleanExistenceCheck(layerName)) {
            throw Error(`Слой с таким именем как:\"${layerName}\", существует!!!`);
        } else {
            return layerName;
        }
    }
    // проверка на существование слоя по имени с возвращением булевого типа данных
    booleanExistenceCheck(layerName) {
        if (this._layerStorage.has(layerName)) {
            return true;
        } else {
            return false;
        }
    }
    //TODO реализовать методы, удаления геообъектов. А только есть методы добавления.
}