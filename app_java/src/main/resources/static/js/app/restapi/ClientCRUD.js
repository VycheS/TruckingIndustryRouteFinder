class ClientCRUD extends CRUD {
    constructor() {
        super("/clients");
    }

    create(client) {
        return super.create(client.getAllStrJson());
    }

    read(id) {
        return super.read(id);
    }

    readAll() {
        return super.readAll();
    }

    update(id, client) {
        return super.update(id, client.getStrJson());
    }

    delete(id) {
        return super.delete(id);
    }


}