class MapModesControl {
    constructor(map, editInformationLayers, editTruckingIndustryLayers) {
        //для последующего отключения включение режима редактирования
        this._map = map;
        //получаем кнопку которую будем вкл/откл в зависимости от режима
        this._editInformationLayers = editInformationLayers;
        //получаем кнопку которую будем вкл/откл в зависимости от режима
        this._editTruckingIndustryLayers = editTruckingIndustryLayers;
        //создаём кнопку
        this._mapModes = new ymaps.control.ListBox({
            data: {
                content: 'Режимы взаимодействия'
            },
            items: this._createItems()
        });
    }

    _createItems() {
        // BEGIN --- VIEW ---
        //создаём элемент для включения режима просмотра карты
        let view = new ymaps.control.ListBoxItem({
            data: {
                content: 'Просмотр'
            },
            state: {
                selected: true
            },
        });
        view.events.add('click', () => {
            //это условие для того чтобы галочка не отключалась при повторном нажатии
            if (view.isSelected()) {
                view.deselect();
            }
            //удаляем с карты лист бокс "редактирование информационных слоёв"
            this._map.controls.remove(this._editInformationLayers.returnListBox(), {
                float: 'left',
                floatIndex: 50
            });
            //удаляем с карты лист бокс "редактирование грузоперевозочных слоёв"
            this._map.controls.remove(this._editTruckingIndustryLayers.returnListBox(), {
                float: 'left',
                floatIndex: 50
            });
            //включаем на карте режим перемещения
            this._map.behaviors.enable(['drag']);
            //снимаем галочки
            editInformation.deselect();
            editTruckingIndustry.deselect();
            //отключаем все галочки на лист боксе редактирования и блокируем кнопки
            this._editInformationLayers.deselectAll();
            this._editTruckingIndustryLayers.deselectAll();
        });
        // END --- VIEW ---

        // BEGIN --- EDIT_INFORMATION ---
        //создаём элемент для включения режима редактирования информационных слоёв
        let editInformation = new ymaps.control.ListBoxItem({
            data: {
                content: 'Редактирование слоёв информационных'
            },
            state: {
                selected: false
            },
        });
        editInformation.events.add('click', () => {
            //это условие для того чтобы галочка не отключалась при повторном нажатии
            if (editInformation.isSelected()) {
                editInformation.deselect();
            }
            //добавляем на карту лист бокс "редактирования информационных слоёв"
            this._map.controls.add(this._editInformationLayers.returnListBox(), {
                float: 'left',
                floatIndex: 50
            });
            //удаляем с карты лист бокс "редактирование грузоперевозочных слоёв"
            this._map.controls.remove(this._editTruckingIndustryLayers.returnListBox(), {
                float: 'left',
                floatIndex: 50
            });
            //отключаем на карте режим перемещения
            this._map.behaviors.disable(['drag']);
            //снимаем галочки
            view.deselect();
            editTruckingIndustry.deselect();
            //отключаем все галочки на лист боксе редактирования и блокируем кнопки
            this._editTruckingIndustryLayers.deselectAll();
        });
        // END --- EDIT_INFORMATION ---

        // BEGIN --- EDIT_TRUCKING_INDUSTRY ---
        //создаём элемент для включения режима редактирования слоёв грузоперевозок
        let editTruckingIndustry = new ymaps.control.ListBoxItem({
            data: {
                content: 'Редактирование слоёв грузоперевозок'
            },
            state: {
                selected: false
            },
        });
        editTruckingIndustry.events.add('click', () => {
            //это условие для того чтобы галочка не отключалась при повторном нажатии
            if (editInformation.isSelected()) {
                editInformation.deselect();
            }
            //добавляем на карту лист бокс "редактирования грузоперевозочных слоёв"
            this._map.controls.add(this._editTruckingIndustryLayers.returnListBox(), {
                float: 'left',
                floatIndex: 50
            });
            //удаляем с карты лист бокс "редактирование информационных слоёв"
            this._map.controls.remove(this._editInformationLayers.returnListBox(), {
                float: 'left',
                floatIndex: 50
            });
            //отключаем на карте режим перемещения
            this._map.behaviors.disable(['drag']);
            //снимаем галочку с режима просмотра
            view.deselect();
            editInformation.deselect();
            //отключаем все галочки на лист боксе редактирования и блокируем кнопки
            this._editInformationLayers.deselectAll();
        });
        // END --- EDIT_TRUCKING_INDUSTRY ---

        return [view, editInformation, editTruckingIndustry];
    }

    returnListBox() {
        return this._mapModes;
    }
}