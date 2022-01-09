class ClientDTO extends DTO {
    constructor(id, surname, name, patronymic, password, email, numberphone, role, strJson) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.password = password;
        this.email = email;
        this.numberphone = numberphone;
        this.role = role;
        this.strJson = strJson;
    }

    getAllStrJson() {
        return super.getAllStrJson(new Map([
            ["id", this.id],
            ["surname", this.surname],
            ["name", this.name],
            ["patronymic", this.patronymic],
            ["password", this.password],
            ["email", this.email],
            ["numberphone", this.numberphone],
            ["role", this.role],
            ["strJson", this.strJson],
        ]));
    }
}