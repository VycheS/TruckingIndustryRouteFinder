class MapLegendControl {
    constructor(informationLayerManager, truckingIndustryLayerManager){
        this._mapLegend = new ymaps.control.ListBox({
            data: {
                content: 'Легенда карты'
            }
        });
        //получаем информационнный менеджер слоёв
        this._informationLayerManager = informationLayerManager;
        //получаем грузоперевозочный менеджер слоёв
        this._truckingIndustryLayerManager = truckingIndustryLayerManager;
        //массив типов менеджеров слоёв
        this._layerManagerTypesToCheck = ['information', 'truckingIndustry'];
    }

    returnListBox(){
        return this._mapLegend;
    }

    addItem(name, type){
        let layerManagerType;
        if (['point', 'line', 'arrow', 'broken_line'].includes(type)) {
            layerManagerType = 'information';
        } else if (['deliveryPoint','goods','truck','pointingArrow','route'].includes(type)){
            layerManagerType = 'truckingIndustry'
        } else throw Error(`Такого слоя с таким геообъектом:${type}, не существует`);
        let ico;//ссылка на иконку
        let heigtIcoPx = 15; //высота икнонки в пикселях
        let ln = '';//пробел
        if (type == 'point') {
            ico = '<img height="' + heigtIcoPx + '" src="/img/point.png" alt="точка">';
            ln = '&#8195'
        } else if(type == 'line') {
            ico = '<img height="' + heigtIcoPx + '" src="/img/line.png" alt="линия">';
        } else if(type == 'broken_line') {
            ico = '<img height="' + heigtIcoPx + '" src="/img/broken_line.png" alt="ломанная линия">';
        }
        let content = ln + ico + ln + ' - ' + name;
        let newItem = new ymaps.control.ListBoxItem({
            data: {
                content: content,
                type_action: name
            },
            state: {
                selected: true
            }
        });
        //для того чтобы отличать типы менеджеров слоёв
        newItem.layerManagerType = layerManagerType;
        //добавляем событие к элементу
        newItem.events.add('click', () => {
                //если выбран включаем слой если не выбран отключаем отображаемый слой
                if ( newItem.layerManagerType == 'information') {
                    if (newItem.isSelected()) {
                        this._informationLayerManager.off(name);
                    } else  {
                        this._informationLayerManager.on(name);
                    }
                } else if(newItem.layerManagerType == 'truckingIndustry') {
                    if (newItem.isSelected()) {
                        this._truckingIndustryLayerManager.off(name);
                    } else  {
                        this._truckingIndustryLayerManager.on(name);
                    }
                } else throw Error(`Такого менеджера слоёв с таким типом как:${newItem.data.layer_manager_type}, не сущесвует`);
        });
        //добавляем в наш лист бокс
        this._mapLegend.add(newItem);
    }

    // rmItem(name){

    // }
}