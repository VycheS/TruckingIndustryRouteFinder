class EditTruckingIndustryLayersControl {
    constructor(buttonsGeoObj) {
        this.buttonsGeoObj = buttonsGeoObj;//получаем менеджера кнопок выбора типа активного геообъекта
        this.selectedLayer = undefined;
        this._newLayerItem = this._createNewLayerItem();//создаём кнопку по которой будем добавлять новые слои
        this._editTruckingIndustryLayersControl = new ymaps.control.ListBox({
            data: {
                content: 'Редактор слоёв грузоперевозки'
            },
            items: [this._newLayerItem]
        });
        this._selectedTypeGeoObj = undefined; //выбранный тип геообъектов
    }

    returnListBox(){
        return this._editTruckingIndustryLayersControl;
    }

    _createNewLayerItem() {
        let itemNewLayot = new ymaps.control.ListBoxItem({
            data: {
                content: '<strong>+</strong> добавить слой грузоперевозок',
                type_action: 'add_layot'//при помощи этого дополнительного св-ва мы cможем его отличать от других элементов
            },
            state: {
                selected: false
            },
        });

        itemNewLayot.events.add('click', () => {
            location.hash = "openModalTruckingIndustry"; //переход к id(открытие модального окна)
            itemNewLayot.select(); // ставит галочку, но в данном случае она просто её отключает повторным нажатием
        });

        return itemNewLayot;
    }

    //действие выбранного элемента листбокса
    _selectItem(selectedItem) {
        //возвращаем итератор элементов
        let it = this._editTruckingIndustryLayersControl.getIterator();
        let item;//хранит элементы списка
        //отключаем галочки не выбранных элементов
        while ((item = it.getNext()) != it.STOP_ITERATION) {
            if (item != selectedItem) {
                item.deselect();
            }
        }
        //это условие чтобы при повторном нажатии откл/вкл режим редактирования выбранного слоя 
        if (selectedItem.isSelected()) {
            //обнуляем выбранный слой
            this.selectedLayer = undefined;
            //обнуляем выбранный тип слоя для кнопок, запрещаем кнопки
            this.buttonsGeoObj.setTypeLayer(undefined);
        } else {
            //высталяем имя выбранного слоя
            this.selectedLayer = selectedItem.data.get('content');
            //выставляем кнопки в зависимости от типа слоя
            this.buttonsGeoObj.setTypeLayer(selectedItem.data.get('type_layer'));
            //выставляем в зависимости от типа геообъектов
            this._selectedTypeGeoObj = selectedItem.data.get('type_geoobj');
        }

    }

    deselectAll(){
        //возвращаем итератор элементов
        let it = this._editTruckingIndustryLayersControl.getIterator();
        let item;//хранит элементы списка
        
        //отключаем галочки на всех элементах
        while ((item = it.getNext()) != it.STOP_ITERATION) {
                item.deselect();
        }

        //обнуляем выбранный слой
        this.selectedLayer = undefined;
        //обнуляем выбранный тип слоя для кнопок, запрещаем кнопки
        this.buttonsGeoObj.setTypeLayer(undefined);
    }

    addItem(name, type_geoobj) {
        let type_layer = this.getConvertToStandartType(type_geoobj);
        let newItem = new ymaps.control.ListBoxItem({
            data: {
                content: name,
                //для удобства добавляем собственное св-во, по которому будет знать тип слоя и тип геообъетов
                type_layer: type_layer,
                type_geoobj: type_geoobj
            },
            state: {
                selected: false
            }
        });
        //добавляем событие к элементу
        newItem.events.add('click', () => {
            //выполняем при нажатии
            this._selectItem(newItem);
            //выделяем цветом актвный
            // this.highlightColor(name);//TODO придумать что с этим делать возможно вынести в App либо в менеджер слоёв
        });
        //добавляем новый элемент в лист бокс
        this._editTruckingIndustryLayersControl.add(newItem);

        //ПЕРЕМЕЩАЕМ КНОПКУ НА САМЫЙ НИЗ
        //удаляем кнопку добавления новых слоёв
        this._editTruckingIndustryLayersControl.remove(this._newLayerItem);
        //добавляем обратно кнопку добавления новыйх слоёв
        this._editTruckingIndustryLayersControl.add(this._newLayerItem);

        //выполняем событие при нажатии на ноый добавленый элемент
        this._selectItem(newItem);
        //ставим галочку чтобы при добавлении сразу был выбран слой
        newItem.select();
    }

    getConvertToStandartType(type) {
        if (['deliveryPoint', 'truck', 'goods', ].includes(type)) {
            return 'point';
        } else if (['pointingArrow', 'route'].includes(type)) {
            return 'line'
        } else throw Error(`Такого типа геообъектов:\"${type}\" для грузоперевозок, не существует`);
    }

    //возвращаем выбранный слой
    getSelectedLayer() {
        return this.selectedLayer;
    }
    //возвращаем тип геообъектов
    getSelectedTypeGeoObj() {
        return this._selectedTypeGeoObj;
    }
}