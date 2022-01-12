class ClientCRUD extends CRUD {
    constructor() {
        super("/clients");
    }

    create(client) {
        return super.create(JSON.stringify(client));
    }

    read(id) {
        return super.read(id);
    }

    readAll() {
        return super.readAll();
    }

    update(id, client) {
        return super.update(id, JSON.stringify(client));
    }

    delete(id) {
        return super.delete(id);
    }


}