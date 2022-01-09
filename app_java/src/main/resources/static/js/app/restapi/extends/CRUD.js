class CRUD {
    constructor(url) {
        this.url = url;
    }

    create(strJsonBody) {
        return this.__sendToServer(this.url, "POST", {"Content-Type": "application/json"}, strJsonBody);
    }

    read(id) {
        return this.__sendToServer(this.url + `/${id}`, "GET", {}, "");
    }

    readAll() {
        return this.__sendToServer(this.url, "GET", {}, "");
    }

    update(id, strJsonBody) {
        return this.__sendToServer(this.url + `/${id}`, "PUT", {"Content-Type": "application/json"}, strJsonBody);
    }

    delete(id) {
        return this.__sendToServer(this.url + `/${id}`, "DELETE", {"Content-Type": "application/json"}, false);
    }

    async __sendToServer(url, method, headers, body) {
            let httpParam;
            if (body === "" || body === false) {
                httpParam = {"method": method,"headers": headers}
            } else {
                httpParam = {"method": method,"headers": headers,"body": body}
            }
            const response = await fetch(url, httpParam);
            if (!(response.ok) ) {
                throw new Error(`HTTP error! status: ${response.status}`);
            } else {
                return response;
                // return await response.json(); //extract JSON from the http response
                //TODO подумать может просто responce возвращать!!!
            }
        
    }
}